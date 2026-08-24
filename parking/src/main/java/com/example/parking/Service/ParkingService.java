package com.example.parking.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.parking.VehicleType;
import com.example.parking.DTO.SpotAddRequest;
import com.example.parking.DTO.SpotUpdateRequest;
import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitRequest;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Exceptions.AlreadyParkedException;
import com.example.parking.Exceptions.DoesNotExistException;
import com.example.parking.Exceptions.DuplicateEntityException;
import com.example.parking.Exceptions.InvalidVehicleTypeException;
import com.example.parking.Exceptions.NoAvailableSpotsException;
import com.example.parking.Exceptions.NoTicketFoundException;
import com.example.parking.Exceptions.NoVehicleFoundException;
import com.example.parking.Exceptions.SpotAlreadyExistsException;
import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.Repository.TicketRepository;
import com.example.parking.Repository.UserRepository;
import com.example.parking.Repository.VehicleRepository;
import com.example.parking.Strategy.FeeStrategy;
import com.example.parking.Strategy.TypesFeeStrategy;
import com.example.parking.model.Branch;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Users;
import com.example.parking.model.Vehicle;

@Service
public class ParkingService {
    // TODO: add a searching endpoint
    private final VehicleRepository vehicleRepo;
    private final SpotRepository spotRepo;
    private final TicketRepository ticketRepo;
    private final BranchRepository branchRepo;
    private final UserRepository userRepo;

    public ParkingService(VehicleRepository vehicleRepo, SpotRepository spotRepo, TicketRepository ticketRepo, BranchRepository branchRepo,UserRepository userRepo){
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
        this.ticketRepo = ticketRepo;
        this.branchRepo= branchRepo;
        this.userRepo = userRepo;
    }

    public VehicleEntryResponse enterVehicle(VehicleEntryRequest request){
        String plate = request.getLicencePlate();
        VehicleType type = request.getVehicleType();

        Vehicle vehicle = vehicleRepo.findByLicencePlate(plate);

        if(vehicle==null){
            vehicle = new Vehicle();
            vehicle.setLicencePlate(plate);
            vehicle.setType(type);
            vehicleRepo.save(vehicle);
        }
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Users user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        
        ParkingTicket ticket = ticketRepo.findByVehicleVehicleIdAndExitTimeIsNull(vehicle.getVehicleId());
        if(ticket != null){
            throw new AlreadyParkedException("Vehicle Already has an ongoing ticket that didn't exit");
        }
        Spot spot = spotRepo.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(type,request.getBranchId());
        if(spot==null){
            throw new NoAvailableSpotsException("No spots are available for this vehicle type");
        }
        spot.setAvailable(false);
        spotRepo.save(spot);

        ticket = new ParkingTicket();
        ticket.setSpot(spot);
        ticket.setVehicle(vehicle);
        ticket.setUser(user);
        ticket.setEntryTime(LocalDateTime.now());
        ticketRepo.save(ticket);

        return new VehicleEntryResponse(request.getBranchId(),plate, spot.getSpotNumber(), ticket.getEntryTime());
    }

    public VehicleExitResponse exitVehicle(String plate, VehicleExitRequest request){
        if(!plate.matches("[A-Za-z0-9]{2}-[A-Za-z0-9]{3}-[A-Za-z0-9]{2}")){
            throw new InvalidVehicleTypeException("plate number must follow format XX-XXX-XX");
        }
        Vehicle vehicle = vehicleRepo.findByLicencePlate(plate);

        if(vehicle == null){//vehicle doesnt have a ticket
            throw new NoVehicleFoundException("Vehicle not found in parking");
        }
        ParkingTicket ticket = ticketRepo.findByVehicleVehicleIdAndExitTimeIsNull(vehicle.getVehicleId());
        if(ticket == null){
            throw new NoTicketFoundException("Ticket either doesn't exist or already exited");
        }
        Branch branch = branchRepo.findById(request.getBranchId()).orElseThrow(() -> new DoesNotExistException("Branch does not exist"));
        if(!ticket.getSpot().getBranch().equals(branch)){
            throw new NoTicketFoundException("Ticket doesn't belong to this branch");
        }
        ticket.setExitTime(LocalDateTime.now());
        Long duration = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes();
        TypesFeeStrategy typesStrategy = new TypesFeeStrategy();
        FeeStrategy strategy = typesStrategy.getFeeStrategy(vehicle.getType());
        double fee = strategy.calculateFee(duration);
        
        ticket.setFee(fee);
        ticketRepo.save(ticket);

        Spot spot = ticket.getSpot();
        spot.setAvailable(true);
        spotRepo.save(spot);

        return new VehicleExitResponse(plate, ticket.getEntryTime(), ticket.getExitTime(), (double)(duration/60.0), fee);
    }
    public Page<Spot> getAllSpots(Pageable pageable){
        return this.spotRepo.findAll(pageable);
    }
    public Page<Spot> getAvailableSpots(Pageable pageable){
        return this.spotRepo.findAllByIsAvailableTrue(pageable);
    }
    public Page<Spot> searchSpots(String location, Pageable pageable) {
        return spotRepo.findByBranchLocationContainingIgnoreCase(location, pageable);
    }
    public Page<Vehicle> findAllVehicles(Pageable pageable) {
        return this.vehicleRepo.findAll(pageable);
    }
    public Page<Vehicle> findAllVehiclesByType(VehicleType type, Pageable pageable) {
        return this.vehicleRepo.findAllByType(type, pageable);
    }
    public Page<Vehicle> searchVehicles(String plate,Pageable pageable) {
        return this.vehicleRepo.findByLicencePlateContainingIgnoreCase(plate, pageable);
    }
    public Page<ParkingTicket> findAllTickets(Pageable pageable){
        return this.ticketRepo.findAll(pageable);
    }
    public Page<ParkingTicket> findAllOngoingTickets(Pageable pageable){
        return this.ticketRepo.findAllByExitTimeIsNull(pageable);
    }
    public Page<ParkingTicket> searchTickets(String licencePlate, Pageable pageable) {

        return ticketRepo.findByVehicleLicencePlateContainingIgnoreCase(licencePlate,pageable);
    }

    public Spot addNewSpot(SpotAddRequest request){
        Branch branch = branchRepo.findById(request.getBranchId()).orElseThrow(() -> new DoesNotExistException("Branch does not exist"));

        Spot spot = spotRepo.findBySpotNumberAndBranchBranchId(request.getSpotNumber(),request.getBranchId());

        if(spot!=null){
            throw new SpotAlreadyExistsException("Another spot number already exists in same branch");
        }

        spot = new Spot();
        spot.setBranch(branch);
        spot.setSpotNumber(request.getSpotNumber());
        spot.setType(request.getType());
        return this.spotRepo.save(spot);
    }

    public Spot UpdateSpot(int id, SpotUpdateRequest request){
        Spot spot = spotRepo.findById(id).orElseThrow(() -> new DoesNotExistException("Spot id does not exist"));
        
        if(request.getSpotNumber()!=0){
            Spot s = spotRepo.findBySpotNumberAndBranchBranchId(request.getSpotNumber(), spot.getBranch().getBranchId());
            if(s!= null&& s.getSpotId() != spot.getSpotId()){
                throw new SpotAlreadyExistsException("Another Spot number already exists in same branch");
            }
            spot.setSpotNumber(request.getSpotNumber());
        }
        if(request.getType()!= null){
            if(!request.getType().equals(VehicleType.CAR)&&!request.getType().equals(VehicleType.TRUCK)&&!request.getType().equals(VehicleType.MOTORCYCLE)){
                throw new InvalidVehicleTypeException("Spot Vehicle Type is invalid");
            }
            spot.setType(request.getType());
        }
        if(request.getAvailable()!=null){
            spot.setAvailable(request.getAvailable().booleanValue());
        }
        return this.spotRepo.save(spot);
    }
    public Branch addBranch(String location){
        Branch b = branchRepo.findByLocation(location);

        if(b!=null){
            throw new DuplicateEntityException("There is already a branch in that location");
        }
        b = new Branch();
        b.setLocation(location);
        return this.branchRepo.save(b);
    }
    public Page<Branch> searchBranches(String location,Pageable pageable) {

        return branchRepo.findByLocationContainingIgnoreCase(location,pageable);
    }
}
