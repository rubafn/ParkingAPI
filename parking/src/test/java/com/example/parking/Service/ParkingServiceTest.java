package com.example.parking.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.parking.VehicleType;
import com.example.parking.DTO.VehicleEntryRequest;
import com.example.parking.DTO.VehicleEntryResponse;
import com.example.parking.DTO.VehicleExitRequest;
import com.example.parking.DTO.VehicleExitResponse;
import com.example.parking.Exceptions.AlreadyParkedException;
import com.example.parking.Exceptions.NoAvailableSpotsException;
import com.example.parking.Exceptions.NoTicketFoundException;
import com.example.parking.Exceptions.NoVehicleFoundException;
import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.Repository.TicketRepository;
import com.example.parking.Repository.VehicleRepository;
import com.example.parking.model.Branch;
import com.example.parking.model.ParkingTicket;
import com.example.parking.model.Spot;
import com.example.parking.model.Vehicle;

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


    @InjectMocks
    private ParkingService parkingService;

    //entry

    @Test
    void VehicleEntryTest_shouldNotEnterDuplicate() {

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

        // Check that the spot became unavailable
        assertFalse(s.isAvailable());
        }

    @Test
    void VehicleEntryTest_noAvailableSpots(){
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

        assertNotEquals(ticket.getFee(),0);
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
}
