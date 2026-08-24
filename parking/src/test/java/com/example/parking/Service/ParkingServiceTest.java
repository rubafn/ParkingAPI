package com.example.parking.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
import com.example.parking.Exceptions.NoAvailableSpotsException;
import com.example.parking.Exceptions.NoTicketFoundException;
import com.example.parking.Exceptions.NoVehicleFoundException;
import com.example.parking.Exceptions.SpotAlreadyExistsException;
import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.Repository.TicketRepository;
import com.example.parking.Repository.UserRepository;
import com.example.parking.Repository.VehicleRepository;
import com.example.parking.model.Branch;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Users;
import com.example.parking.model.Vehicle;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

//TODO: fix tests (user related)

@ExtendWith(MockitoExtension.class)//this is to create mock repositories for testing
public class ParkingServiceTest {
    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ParkingService parkingService;

    void setUpSecurityContext() {

        SecurityContextHolder.clearContext();

         SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn("rubanabhan");
    }
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
    //entry

    @Test
    void VehicleEntryTest_shouldNotEnterDuplicate() {
        setUpSecurityContext();

        Users user = new Users();
        user.setUsername("rubanabhan");
        when(userRepository.findByUsername("rubanabhan")).thenReturn(Optional.of(user));

        // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);


        // Existing active ticket
        ParkingTicket ticket = new ParkingTicket();
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setVehicle(v);

        // Tell mock vehicle repository that the vehicle exists
        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12345");
        request.setVehicleType(VehicleType.TRUCK);
        request.setBranchId(1);

        assertThrows(
                AlreadyParkedException.class,
                () -> parkingService.enterVehicle(request)
        );
    }

    @Test
    void VehicleEntryTest_correct(){
        setUpSecurityContext();
        Users user = new Users();
        user.setUsername("rubanabhan");
        when(userRepository.findByUsername("rubanabhan")).thenReturn(Optional.of(user));

        // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);

        // Available truck spot
        Spot s = new Spot();
        s.setAvailable(true);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);

         when(spotRepository.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(VehicleType.TRUCK,1))
                .thenReturn(s);

        // Tell mock vehicle repository that the vehicle exists
        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle does not have an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(null);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12345");
        request.setVehicleType(VehicleType.TRUCK);
        request.setBranchId(1);

         VehicleEntryResponse response =
            parkingService.enterVehicle(request);

        // Check the result
        assertEquals("12345", response.getLicencePlate());
        assertEquals(199, response.getAssignedSpot());
        assertNotNull(response.getEntryTime());
        // Check that the spot became unavailable
        assertFalse(s.isAvailable());
         // Verify ticket that was saved
        ArgumentCaptor<ParkingTicket> ticketCaptor =
                ArgumentCaptor.forClass(ParkingTicket.class);

        verify(ticketRepository, times(1))
                .save(ticketCaptor.capture());

        ParkingTicket savedTicket = ticketCaptor.getValue();

        // Verify ticket information
        assertEquals(v, savedTicket.getVehicle());
        assertEquals(s, savedTicket.getSpot());
        assertEquals(user, savedTicket.getUser());
        assertNotNull(savedTicket.getEntryTime());
    }

    @Test
    void VehicleEntryTest_noAvailableSpots(){
        setUpSecurityContext();
        Users user = new Users();
        user.setUsername("rubanabhan");
        when(userRepository.findByUsername("rubanabhan")).thenReturn(Optional.of(user));

        when(spotRepository.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(VehicleType.MOTORCYCLE,1))
            .thenReturn(null);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12345");
        request.setVehicleType(VehicleType.MOTORCYCLE);
        request.setBranchId(1);

        assertThrows(
                NoAvailableSpotsException.class,
                () -> parkingService.enterVehicle(request)
        );
    }
    @Test
    void VehicleEntryTest_invalidLicencePlatePattern() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        VehicleEntryRequest request = new VehicleEntryRequest();

        request.setLicencePlate("12345");
        request.setVehicleType(VehicleType.CAR);
        request.setBranchId(1);

        Set<ConstraintViolation<VehicleEntryRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }
    @Test
    void VehicleEntryTest_validLicencePlatePattern() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        VehicleEntryRequest request = new VehicleEntryRequest();

        request.setLicencePlate("12-345-67");
        request.setVehicleType(VehicleType.CAR);
        request.setBranchId(1);

        Set<ConstraintViolation<VehicleEntryRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
    @Test
    void SpotTest_invalidSpotNumber() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        SpotAddRequest request = new SpotAddRequest();

        request.setSpotNumber(0);
        request.setType(VehicleType.CAR);
        request.setBranchId(1);

        Set<ConstraintViolation<SpotAddRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    //exit
    @Test
    void VehicleExitTest_correct(){
        // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);

        //truck spot
        Spot s = new Spot();
        s.setAvailable(false);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);

        // Existing active ticket
        ParkingTicket ticket = new ParkingTicket();
        ticket.setEntryTime(LocalDateTime.now().minusHours(5));
        ticket.setVehicle(v);
        ticket.setSpot(s);

        Branch branch = new Branch();
        branch.setLocation("Ramallah");

        when(branchRepository.findById(anyInt()))
                .thenReturn(Optional.of(branch));

        s.setBranch(branch);

        // Tell mock vehicle repository that the vehicle exists
        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);


        VehicleExitResponse response = parkingService.exitVehicle("12345", new VehicleExitRequest(branch.getBranchId()));

        assertTrue(s.isAvailable());

        assertEquals("12345", response.getPlateNumber());

        assertNotNull(ticket.getExitTime());

        assertNotEquals(0, ticket.getFee());
    }

    @Test
    void VehicleExitTest_ticketDoesNotExist(){
         // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);

         when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has no active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(null);

        assertThrows(NoTicketFoundException.class, () -> parkingService.exitVehicle("12345", new VehicleExitRequest(1)));
    }

    @Test
    void VehicleExitTest_vehicleDoesNotExist(){
         when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(null);
        assertThrows(NoVehicleFoundException.class, ()-> parkingService.exitVehicle("12345", new VehicleExitRequest(1)));
    }
    @Test
    void VehicleExitTest_wrongBranch() {

        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);

        Branch actualBranch = new Branch();
        actualBranch.setLocation("Ramallah");

        Branch requestedBranch = new Branch();
        requestedBranch.setLocation("Nablus");

        Spot s = new Spot();
        s.setAvailable(false);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);
        s.setBranch(actualBranch);

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(v);
        ticket.setSpot(s);
        ticket.setEntryTime(LocalDateTime.now().minusHours(2));

        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        when(ticketRepository
                .findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        when(branchRepository.findById(2))
                .thenReturn(Optional.of(requestedBranch));

        assertThrows(
                NoTicketFoundException.class,
                () -> parkingService.exitVehicle(
                        "12345",
                        new VehicleExitRequest(2)
                )
        );

        // Spot should remain unavailable
        assertFalse(s.isAvailable());

        // Ticket should still be ongoing
        assertTrue(ticket.getExitTime() == null);
    }
    @Test
    void VehicleExitTest_branchDoesNotExist() {

        Vehicle v = new Vehicle();
        v.setLicencePlate("12345");
        v.setType(VehicleType.TRUCK);

        Spot s = new Spot();
        s.setAvailable(false);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(v);
        ticket.setSpot(s);
        ticket.setEntryTime(LocalDateTime.now().minusHours(2));

        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(v);

        when(ticketRepository
                .findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        when(branchRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                DoesNotExistException.class,
                () -> parkingService.exitVehicle(
                        "12345",
                        new VehicleExitRequest(999)
                )
        );
    }

    //pagination
    @Test
    void getAllSpots_returnsPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Spot spot = new Spot();
        spot.setSpotNumber(1);

        Page<Spot> page =new PageImpl<>(List.of(spot));

        when(spotRepository.findAll(pageable)).thenReturn(page);

        Page<Spot> result =parkingService.getAllSpots(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getSpotNumber());

        verify(spotRepository).findAll(pageable);
    }
    @Test
    void findAllVehiclesByType_returnsPage() {

        Pageable pageable = PageRequest.of(0, 5);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate("12-345-67");
        vehicle.setType(VehicleType.CAR);

        Page<Vehicle> page =
                new PageImpl<>(List.of(vehicle));

        when(vehicleRepository.findAllByType(
                VehicleType.CAR,
                pageable))
                .thenReturn(page);

        Page<Vehicle> result =
                parkingService.findAllVehiclesByType(
                        VehicleType.CAR,
                        pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(
                "12-345-67",
                result.getContent().get(0).getLicencePlate()
        );
    }

    //users
    @Test
    void VehicleEntryTest_loggedInUserDoesNotExist() {
        setUpSecurityContext();

        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate("12345");
        vehicle.setType(VehicleType.CAR);

        when(vehicleRepository.findByLicencePlate("12345"))
                .thenReturn(vehicle);

        when(userRepository.findByUsername("rubanabhan"))
                .thenReturn(Optional.empty());

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12345");
        request.setVehicleType(VehicleType.CAR);
        request.setBranchId(1);

        assertThrows(
                RuntimeException.class,
                () -> parkingService.enterVehicle(request)
        );
    }
    //admin operations
    @Test
    void AddBranchTest_correct() {

        when(branchRepository.findByLocation("Ramallah"))
                .thenReturn(null);

        Branch savedBranch = new Branch();
        savedBranch.setLocation("Ramallah");

        when(branchRepository.save(any(Branch.class)))
                .thenReturn(savedBranch);

        Branch result = parkingService.addBranch("Ramallah");

        assertNotNull(result);
        assertEquals("Ramallah", result.getLocation());
    }
    @Test
    void AddBranchTest_duplicateBranch() {

        Branch existingBranch = new Branch();
        existingBranch.setLocation("Ramallah");

        when(branchRepository.findByLocation("Ramallah"))
                .thenReturn(existingBranch);

        assertThrows(
                DuplicateEntityException.class,
                () -> parkingService.addBranch("Ramallah")
        );
    }
    @Test
    void AddSpotTest_correct() {

        Branch branch = new Branch();
        branch.setLocation("Ramallah");

        SpotAddRequest request = new SpotAddRequest();
        request.setBranchId(1);
        request.setSpotNumber(10);
        request.setType(VehicleType.CAR);

        when(branchRepository.findById(1))
                .thenReturn(Optional.of(branch));

        when(spotRepository.findBySpotNumberAndBranchBranchId(10, 1))
                .thenReturn(null);

        Spot savedSpot = new Spot();
        savedSpot.setBranch(branch);
        savedSpot.setSpotNumber(10);
        savedSpot.setType(VehicleType.CAR);

        when(spotRepository.save(any(Spot.class)))
                .thenReturn(savedSpot);

        Spot result = parkingService.addNewSpot(request);

        assertNotNull(result);
        assertEquals(10, result.getSpotNumber());
        assertEquals(VehicleType.CAR, result.getType());
        assertEquals(branch, result.getBranch());
    }
    @Test
    void AddSpotTest_branchDoesNotExist() {

        SpotAddRequest request = new SpotAddRequest();
        request.setBranchId(99);
        request.setSpotNumber(10);
        request.setType(VehicleType.CAR);

        when(branchRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                DoesNotExistException.class,
                () -> parkingService.addNewSpot(request)
        );
    }
    @Test
    void AddSpotTest_duplicateSpot() {

        Branch branch = new Branch();
        branch.setLocation("Ramallah");

        Spot existingSpot = new Spot();
        existingSpot.setSpotNumber(10);
        existingSpot.setBranch(branch);

        SpotAddRequest request = new SpotAddRequest();
        request.setBranchId(1);
        request.setSpotNumber(10);
        request.setType(VehicleType.CAR);

        when(branchRepository.findById(1))
                .thenReturn(Optional.of(branch));

        when(spotRepository.findBySpotNumberAndBranchBranchId(10, 1))
                .thenReturn(existingSpot);

        assertThrows(
                SpotAlreadyExistsException.class,
                () -> parkingService.addNewSpot(request)
        );
    }
    @Test
    void UpdateSpotTest_correct() {

        Spot spot = new Spot();
        spot.setSpotNumber(10);
        spot.setType(VehicleType.CAR);
        spot.setAvailable(true);

        Branch branch = new Branch();
        branch.setLocation("Ramallah");
        spot.setBranch(branch);

        when(spotRepository.findById(1))
                .thenReturn(Optional.of(spot));

        when(spotRepository.findBySpotNumberAndBranchBranchId(20, 0))
                .thenReturn(null);

        SpotUpdateRequest request = new SpotUpdateRequest();
        request.setSpotNumber(20);
        request.setType(VehicleType.TRUCK);
        request.setAvailable(false);

        when(spotRepository.save(any(Spot.class)))
                .thenReturn(spot);

        Spot result = parkingService.UpdateSpot(1, request);

        assertNotNull(result);
        assertEquals(20, result.getSpotNumber());
        assertEquals(VehicleType.TRUCK, result.getType());
        assertFalse(result.isAvailable());
    }
    @Test
    void UpdateSpotTest_spotDoesNotExist() {

        when(spotRepository.findById(1))
                .thenReturn(Optional.empty());

        SpotUpdateRequest request = new SpotUpdateRequest();
        request.setSpotNumber(20);

        assertThrows(
                DoesNotExistException.class,
                () -> parkingService.UpdateSpot(1, request)
        );
    }
    @Test
    void UpdateSpotTest_duplicateSpotNumber() {
        Branch branch = new Branch();
        branch.setLocation("Ramallah");

        Spot currentSpot = mock(Spot.class);
        Spot existingSpot = mock(Spot.class);

        when(currentSpot.getSpotId()).thenReturn(1);
        when(existingSpot.getSpotId()).thenReturn(2);

        when(currentSpot.getBranch()).thenReturn(branch);

        when(spotRepository.findById(1))
                .thenReturn(Optional.of(currentSpot));

        when(spotRepository.findBySpotNumberAndBranchBranchId(20, 0))
                .thenReturn(existingSpot);

        SpotUpdateRequest request = new SpotUpdateRequest();
        request.setSpotNumber(20);

        assertThrows(
                SpotAlreadyExistsException.class,
                () -> parkingService.UpdateSpot(1, request)
        );
    }
}
