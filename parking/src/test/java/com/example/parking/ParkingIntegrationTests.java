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


    @BeforeEach
    void setUp(){
        Branch branch = new Branch();
        branch.setLocation("Test Ramallah");
        branchRepo.save(branch);

        Spot spot = new Spot();
        spot.setAvailable(true);
        spot.setBranch(branch);
        spot.setSpotNumber(110);
        spot.setType(VehicleType.CAR);
        spotRepo.save(spot);
    }
    @Test
    void testEntry() throws Exception{
        mockMvc.perform(post("/api/parking/entry").contentType(MediaType.APPLICATION_JSON).content("""
            {
                "licencePlate":"12-345-69",
                "vehicleType": "CAR",
                "branchId": 1
            }
        """)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.licencePlate").value("12-345-69"))
        .andExpect(jsonPath("$.branchId").value(1))
        .andExpect(jsonPath("$.assignedSpot").value(110));
    }
    @Test
    void testExit() throws Exception{
        
    // ARRANGE: vehicle enters first
    mockMvc.perform(
        post("/api/parking/entry")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "licencePlate": "12-345-69",
                    "vehicleType": "CAR",
                    "branchId": 1
                }
            """)
    )
    .andExpect(status().isOk());

    // ACT + ASSERT: now exit it
    mockMvc.perform(
        post("/api/parking/exit/12-345-69")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "branchId": 1
                }
            """)
    )
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.plateNumber").value("12-345-69"));
    }


}
