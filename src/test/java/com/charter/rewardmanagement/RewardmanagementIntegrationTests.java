package com.charter.rewardmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for Reward Management APIs.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RewardManagementIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests the rewards API endpoint.
     */
    @Test
    void testRewardsEndpoint() throws Exception {

        mockMvc.perform(get("/api/rewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk());
    }
}