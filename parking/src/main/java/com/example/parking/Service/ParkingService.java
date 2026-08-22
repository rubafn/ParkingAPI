package com.example.parking.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
import com.example.parking.model.Vehicle;

@Service
public class ParkingService {
    private final VehicleRepository vehicleRepo;
    private final SpotRepository spotRepo;
    private final TicketRepository ticketRepo;
    private final BranchRepository branchRepo;
    private final UserRepository userRepo;

    public ParkingService(VehicleRepository vehicleRepo, SpotRepository spotRepo, TicketRepository ticketRepo, BranchRepository branchRepo, UserRepository userRepo){
        this.spotRepo = spotRepo;
        this.vehicleRepo = vehicleRepo;
        this.ticketRepo = ticketRepo;
        this.branchRepo= branchRepo;
        this.userRepo = userRepo;
    }

    public VehicleEntryResponse enterVehicle(VehicleEntryRequest request){
        //see if user exists
        //see if user is logged


        String plate = request.getLicencePlate();
        VehicleType type = request.getVehicleType();

        //create new vehicle or use existing vehicle
        Vehicle vehicle = vehicleRepo.findByLicencePlate(plate);

        if(vehicle==null){
            vehicle = new Vehicle();
            vehicle.setLicencePlate(plate);
            vehicle.setType(type);
            vehicleRepo.save(vehicle);
        }
        ParkingTicket ticket = ticketRepo.findByVehicleVehicleIdAndExitTimeIsNull(vehicle.getVehicleId());
        if(ticket != null){
            throw new AlreadyParkedException("Vehicle Already has an ongoing ticket that didn't exit");
        }
        //check for available spots with the suitable type 
        Spot spot = spotRepo.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(type,request.getBranchId());
        //no available spots
        if(spot==null){
            throw new NoAvailableSpotsException("No spots are available for this vehicle type");
        }
        spot.setAvailable(false);
        spotRepo.save(spot);

        ticket = new ParkingTicket();
        ticket.setSpot(spot);
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(LocalDateTime.now());
        ticketRepo.save(ticket);

        return new VehicleEntryResponse(request.getBranchId(),plate, spot.getSpotNumber(), LocalDateTime.now());
    }

    public VehicleExitResponse exitVehicle(String plate, VehicleExitRequest request){
        //see if user exists
        //see if user is logged


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
    public List<Spot> getAllSpots(){
        return this.spotRepo.findAll();
    }
    public List<Spot> getAvailableSpots(){
        return this.spotRepo.findAllByIsAvailableTrue();
    }

    public List<Vehicle> findAllVehicles() {
        return this.vehicleRepo.findAll();
    }
    public List<Vehicle> findAllVehiclesByType(VehicleType type){
        return this.vehicleRepo.findAllByType(type);
    }
    public List<ParkingTicket> findAllTickets(){
        //maybe add an admin check here
        return this.ticketRepo.findAll();
    }
    public List<ParkingTicket> findAllOngoingTickets(){
        return this.ticketRepo.findAllByExitTimeIsNull();
    }

    public Spot addNewSpot(SpotAddRequest request){
        //see if user is logged in 


            //throw exception no allowed 401

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
        //see if user is logged in 

        
            //throw exception no allowed 401
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
        //check if theres a user, if they are logged in, and if they are a admin
        Branch b = branchRepo.findByLocation(location);

        if(b!=null){
            throw new DuplicateEntityException("There is already a branch in that location");
        }
        b = new Branch();
        b.setLocation(location);
        return this.branchRepo.save(b);
    }
}
