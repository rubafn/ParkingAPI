package com.example.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.parking.Repository.BranchRepository;
import com.example.parking.Repository.SpotRepository;
import com.example.parking.Repository.UserRepository;
import com.example.parking.model.Branch;
import com.example.parking.model.Spot;
import com.example.parking.model.Users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Store the branch created for the current test
    private Branch branch;
    private Spot spot;


    @BeforeEach
    void setUp() {

        // Create a branch in the temporary H2 database
        branch = new Branch();
        branch.setLocation("Test Ramallah");
        branch = branchRepo.save(branch);

        // Create an available CAR spot in that branch
        spot = new Spot();
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
        Spot updatedSpot = spotRepo.findById(spot.getSpotId())
        .orElseThrow();
        assertFalse(updatedSpot.isAvailable());
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
        Spot updatedSpot = spotRepo.findById(spot.getSpotId())
        .orElseThrow();
        assertTrue(updatedSpot.isAvailable());
    }

    @Test
    void testAddSpot() throws Exception{
        int branchId = branch.getBranchId();
        mockMvc.perform(
            post("/api/parking/spots")
            .with(user("admin").roles("ADMIN"))

            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                    "branchId": %d,
                    "spotNumber": 205,
                    "vehicleType": "TRUCK"
                    }
                    """.formatted(branchId))
        )
        .andExpect(authenticated().withUsername("admin"))
        .andExpect(authenticated().withRoles("ADMIN"))
        .andExpect(status().isOk());
    }
    @Test
    void testUpdateSpot() throws Exception{
        mockMvc.perform(
            patch("/api/parking/spots/"+spot.getSpotId())
            .with(user("admin").roles("ADMIN"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                "type": "TRUCK",
                "isAvailable": true,
                "spotNumber": 110
            }
            """)
        ).andExpect(authenticated().withUsername("admin"))
        .andExpect(authenticated().withRoles("ADMIN"))
        .andExpect(status().isOk());

        Spot updatedSpot = spotRepo.findById(spot.getSpotId())
        .orElseThrow();

        assertEquals(VehicleType.TRUCK, updatedSpot.getType());
    }

    @Test 
    void testUserCannotAddSpot() throws Exception{
        int branchId = branch.getBranchId();
        mockMvc.perform(
            post("/api/parking/spots")
            .with(user("normal").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                    "branchId": %d,
                    "spotNumber": 205,
                    "vehicleType": "TRUCK"
                    }
                    """.formatted(branchId))
        )
        .andExpect(authenticated().withUsername("normal"))
        .andExpect(authenticated().withRoles("USER"))
        .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidEntry() throws Exception {

        mockMvc.perform(
            post("/api/parking/entry")
                .with(user("employee").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "licencePlate": "BAD",
                        "vehicleType": "CAR",
                        "branchId": %d
                    }
                    """.formatted(branch.getBranchId()))
        )
        .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin() throws Exception {

        // ARRANGE: create a real user in H2
        Users user = new Users();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(UserType.USER);

        userRepo.save(user);


        // ACT + ASSERT: call the real login endpoint
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "testuser",
                        "password": "Password123!"
                    }
                    """)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.type").value("USER"))
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.token").isNotEmpty());
    }

}

   