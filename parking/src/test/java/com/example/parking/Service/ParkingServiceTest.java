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
import com.example.parking.DTO.UserResponse;
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

    @InjectMocks
    private UserService userService;

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

        // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12-345-6A");
        v.setType(VehicleType.TRUCK);


        // Existing active ticket
        ParkingTicket ticket = new ParkingTicket();
        ticket.setEntryTime(LocalDateTime.now());
        ticket.setVehicle(v);

        // Tell mock vehicle repository that the vehicle exists
        when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12-345-6A");
        request.setVehicleType(VehicleType.TRUCK);
        request.setBranchId(1);

        assertThrows(
                AlreadyParkedException.class,
                () -> parkingService.enterVehicle(request)
        );
    }

    @Test
    void VehicleEntryTest_correct(){

        // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12-345-6A");
        v.setType(VehicleType.TRUCK);

        // Available truck spot
        Spot s = new Spot();
        s.setAvailable(true);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);

         when(spotRepository.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(VehicleType.TRUCK,1))
                .thenReturn(s);

        // Tell mock vehicle repository that the vehicle exists
        when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle does not have an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(null);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12-345-6A");
        request.setVehicleType(VehicleType.TRUCK);
        request.setBranchId(1);

         VehicleEntryResponse response =
            parkingService.enterVehicle(request);

        // Check the result
        assertEquals("12-345-6A", response.getLicencePlate());
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
        assertNotNull(savedTicket.getEntryTime());
    }

    @Test
    void VehicleEntryTest_noAvailableSpots(){

        when(spotRepository.findFirstByTypeAndBranchBranchIdAndIsAvailableTrue(VehicleType.MOTORCYCLE,1))
            .thenReturn(null);

        VehicleEntryRequest request = new VehicleEntryRequest();
        request.setLicencePlate("12-345-6A");
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

        request.setLicencePlate("12-35-6");
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

        request.setLicencePlate("12-345-6A");
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
        v.setLicencePlate("12-345-6A");
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
        when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has an active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);


        VehicleExitResponse response = parkingService.exitVehicle("12-345-6A", new VehicleExitRequest(branch.getBranchId()));

        assertTrue(s.isAvailable());

        assertEquals("12-345-6A", response.getPlateNumber());

        assertNotNull(ticket.getExitTime());

        assertNotEquals(0, ticket.getFee());
    }

    @Test
    void VehicleExitTest_ticketDoesNotExist(){
         // Existing vehicle
        Vehicle v = new Vehicle();
        v.setLicencePlate("12-345-6A");
        v.setType(VehicleType.TRUCK);

         when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        // Tell mock ticket repository that vehicle has no active ticket
        when(ticketRepository.findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(null);

        assertThrows(NoTicketFoundException.class, () -> parkingService.exitVehicle("12-345-6A", new VehicleExitRequest(1)));
    }

    @Test
    void VehicleExitTest_vehicleDoesNotExist(){
         when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(null);
        assertThrows(NoVehicleFoundException.class, ()-> parkingService.exitVehicle("12-345-6A", new VehicleExitRequest(1)));
    }
    @Test
    void VehicleExitTest_wrongBranch() {

        Vehicle v = new Vehicle();
        v.setLicencePlate("12-345-6A");
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

        when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        when(ticketRepository
                .findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        when(branchRepository.findById(2))
                .thenReturn(Optional.of(requestedBranch));

        assertThrows(
                NoTicketFoundException.class,
                () -> parkingService.exitVehicle(
                        "12-345-6A",
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
        v.setLicencePlate("12-345-6A");
        v.setType(VehicleType.TRUCK);

        Spot s = new Spot();
        s.setAvailable(false);
        s.setSpotNumber(199);
        s.setType(VehicleType.TRUCK);

        ParkingTicket ticket = new ParkingTicket();
        ticket.setVehicle(v);
        ticket.setSpot(s);
        ticket.setEntryTime(LocalDateTime.now().minusHours(2));

        when(vehicleRepository.findByLicencePlate("12-345-6A"))
                .thenReturn(v);

        when(ticketRepository
                .findByVehicleVehicleIdAndExitTimeIsNull(v.getVehicleId()))
                .thenReturn(ticket);

        when(branchRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                DoesNotExistException.class,
                () -> parkingService.exitVehicle(
                        "12-345-6A",
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
        vehicle.setLicencePlate("12-345-6A");
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
                "12-345-6A",
                result.getContent().get(0).getLicencePlate()
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

    //searching vehicles
    @Test
    void SearchVehiclesTest_correct() {

        Vehicle v1 = new Vehicle();
        v1.setLicencePlate("12-345-6A");
        v1.setType(VehicleType.CAR);

        Vehicle v2 = new Vehicle();
        v2.setLicencePlate("12-345-6B");
        v2.setType(VehicleType.TRUCK);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Vehicle> page =
                new PageImpl<>(List.of(v1, v2), pageable, 2);

        when(vehicleRepository.findByLicencePlateContainingIgnoreCase(
                "12",
                pageable
        )).thenReturn(page);

        Page<Vehicle> result =
                parkingService.searchVehicles("12", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals("12-345-6A",
                result.getContent().get(0).getLicencePlate());

        assertEquals("12-345-6B",
                result.getContent().get(1).getLicencePlate());
    }
    @Test
    void SearchVehiclesTest_noResults() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Vehicle> emptyPage =
                new PageImpl<>(
                        List.of(),
                        pageable,
                        0
                );

        when(vehicleRepository.findByLicencePlateContainingIgnoreCase(
                "ZZZ",
                pageable
        )).thenReturn(emptyPage);

        Page<Vehicle> result =
                parkingService.searchVehicles("ZZZ", pageable);

        assertNotNull(result);

        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(vehicleRepository)
                .findByLicencePlateContainingIgnoreCase(
                        "ZZZ",
                        pageable
                );
    }
    @Test
    void SearchVehiclesTest_caseInsensitiveSearch() {

        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate("AB-123-CD");
        vehicle.setType(VehicleType.CAR);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Vehicle> page =
                new PageImpl<>(
                        List.of(vehicle),
                        pageable,
                        1
                );

        when(vehicleRepository.findByLicencePlateContainingIgnoreCase(
                "abc",
                pageable
        )).thenReturn(page);

        Page<Vehicle> result =
                parkingService.searchVehicles("abc", pageable);

        assertEquals(1, result.getTotalElements());

        assertEquals(
                "AB-123-CD",
                result.getContent().get(0).getLicencePlate()
        );

        verify(vehicleRepository)
                .findByLicencePlateContainingIgnoreCase(
                        "abc",
                        pageable
                );
    }
    @Test
    void SearchVehiclesTest_pagination() {

        Pageable pageable = PageRequest.of(2, 5);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate("12-ABC-34");
        vehicle.setType(VehicleType.CAR);

        Page<Vehicle> page =
                new PageImpl<>(
                        List.of(vehicle),
                        pageable,
                        11
                );

        when(vehicleRepository.findByLicencePlateContainingIgnoreCase(
                "ABC",
                pageable
        )).thenReturn(page);

        Page<Vehicle> result =
                parkingService.searchVehicles("ABC", pageable);

        assertEquals(11, result.getTotalElements());
        assertEquals(2, result.getNumber());
        assertEquals(5, result.getSize());

        verify(vehicleRepository)
                .findByLicencePlateContainingIgnoreCase(
                        "ABC",
                        pageable
                );
    }
    //searching spots
    @Test
    void SearchSpotsTest_correct() {

        Branch ramallah = new Branch();
        ramallah.setLocation("Ramallah");

        Spot s1 = new Spot();
        s1.setSpotNumber(10);
        s1.setType(VehicleType.CAR);
        s1.setBranch(ramallah);

        Spot s2 = new Spot();
        s2.setSpotNumber(20);
        s2.setType(VehicleType.TRUCK);
        s2.setBranch(ramallah);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Spot> page =
                new PageImpl<>(List.of(s1, s2), pageable, 2);

        when(spotRepository.findByBranchLocationContainingIgnoreCase(
                "Ramallah",
                pageable
        )).thenReturn(page);

        Page<Spot> result =
                parkingService.searchSpots("Ramallah", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(
                10,
                result.getContent().get(0).getSpotNumber()
        );

        assertEquals(
                20,
                result.getContent().get(1).getSpotNumber()
        );
    }
    @Test
    void SearchTicketsTest_correct() {

        Vehicle v1 = new Vehicle();
        v1.setLicencePlate("12-ABC-345");
        v1.setType(VehicleType.CAR);

        Vehicle v2 = new Vehicle();
        v2.setLicencePlate("ABC-777");
        v2.setType(VehicleType.TRUCK);

        ParkingTicket t1 = new ParkingTicket();
        t1.setVehicle(v1);

        ParkingTicket t2 = new ParkingTicket();
        t2.setVehicle(v2);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ParkingTicket> page =
                new PageImpl<>(List.of(t1, t2), pageable, 2);

        when(ticketRepository.findByVehicleLicencePlateContainingIgnoreCase(
                "ABC",
                pageable
        )).thenReturn(page);

        Page<ParkingTicket> result =
                parkingService.searchTickets("ABC", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(
                "12-ABC-345",
                result.getContent().get(0)
                        .getVehicle()
                        .getLicencePlate()
        );

        assertEquals(
                "ABC-777",
                result.getContent().get(1)
                        .getVehicle()
                        .getLicencePlate()
        );
    }
    @Test
    void SearchBranchesTest_correct() {

        Branch b1 = new Branch();
        b1.setLocation("Ramallah");

        Branch b2 = new Branch();
        b2.setLocation("Ramallah Downtown");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Branch> page =
                new PageImpl<>(List.of(b1, b2), pageable, 2);

        when(branchRepository.findByLocationContainingIgnoreCase(
                "Ramallah",
                pageable
        )).thenReturn(page);

        Page<Branch> result =
                parkingService.searchBranches("Ramallah", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(
                "Ramallah",
                result.getContent().get(0).getLocation()
        );

        assertEquals(
                "Ramallah Downtown",
                result.getContent().get(1).getLocation()
        );
     }


     @Test
     void SearchUsersTest_correct() {

        Users u1 = new Users();
        u1.setUsername("rubanabhan");

        Users u2 = new Users();
        u2.setUsername("rubaAdmin");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Users> page =
                new PageImpl<>(List.of(u1, u2), pageable, 2);

        when(userRepository.findByUsernameContainingIgnoreCase(
                "ruba",
                pageable
        )).thenReturn(page);

        Page<UserResponse> result =
                userService.searchUsers("ruba", pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        assertEquals(
                "rubanabhan",
                result.getContent().get(0).getUsername()
        );

        assertEquals(
                "rubaAdmin",
                result.getContent().get(1).getUsername()
        );
    }
}
