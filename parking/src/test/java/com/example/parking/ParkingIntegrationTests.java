package com.example.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.model.Branch;
import com.example.parking.model.Spot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParkingIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BranchRepository branchRepo;

    @Autowired
    private SpotRepository spotRepo;

    // Store the branch created for the current test
    private Branch branch;


    @BeforeEach
    void setUp() {

        // Create a branch in the temporary H2 database
        branch = new Branch();
        branch.setLocation("Test Ramallah");
        branch = branchRepo.save(branch);

        // Create an available CAR spot in that branch
        Spot spot = new Spot();
        spot.setAvailable(true);
        spot.setBranch(branch);
        spot.setSpotNumber(110);
        spot.setType(VehicleType.CAR);

        spotRepo.save(spot);
    }


    @Test
    void testEntry() throws Exception {

        int branchId = branch.getBranchId();

        mockMvc.perform(
            post("/api/parking/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "licencePlate": "12-345-68",
                        "vehicleType": "CAR",
                        "branchId": %d
                    }
                    """.formatted(branchId))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.licencePlate").value("12-345-68"))
        .andExpect(jsonPath("$.branchId").value(branchId))
        .andExpect(jsonPath("$.assignedSpot").value(110));
    }


    @Test
    void testExit() throws Exception {

        int branchId = branch.getBranchId();

        // ARRANGE:
        // First enter the vehicle so that it has an active ticket.
        mockMvc.perform(
            post("/api/parking/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "licencePlate": "12-345-60",
                        "vehicleType": "CAR",
                        "branchId": %d
                    }
                    """.formatted(branchId))
        )
        .andExpect(status().isOk());


        // ACT:
        // Now exit the same vehicle from the same branch.
        mockMvc.perform(
            post("/api/parking/exit/12-345-60")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "branchId": %d
                    }
                    """.formatted(branchId))
        )

        // ASSERT
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.plateNumber").value("12-345-60"));
    }
}