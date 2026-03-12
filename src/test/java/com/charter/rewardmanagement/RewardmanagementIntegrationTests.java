package com.charter.rewardmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for Reward Management APIs.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RewardManagementIntegrationTests {

    private final MockMvc mockMvc;

    public RewardManagementIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void testRewardsEndpoint() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testRewardsEndpointWithPagination() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31")
                        .param("page","0")
                        .param("size","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageSize").value(2));
    }

    @Test
    void testRewardsEndpointResponseStructure() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].customerId").exists())
                .andExpect(jsonPath("$.content[0].customerName").exists())
                .andExpect(jsonPath("$.content[0].monthlyRewards").isArray())
                .andExpect(jsonPath("$.content[0].totalPoints").exists());
    }

    @Test
    void testRewardsEndpointWithInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","01-01-2024")
                        .param("end","2024-03-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRewardsEndpointWithMissingStartDate() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("end","2024-03-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRewardsEndpointWithMissingEndDate() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRewardsEndpointWithEndDateBeforeStartDate() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-03-31")
                        .param("end","2024-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void testRewardsEndpointWithFutureDates() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2025-01-01")
                        .param("end","2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void testRewardsEndpointWithSameDateRange() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-01-01"))
                .andExpect(status().isOk());
    }

    @Test
    void testRewardsEndpointWithLargePagination() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31")
                        .param("page","0")
                        .param("size","100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageSize").value(100));
    }

    @Test
    void testRewardsEndpointWithDefaultPagination() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0));
    }

    @Test
    void testRewardsEndpointMonthlyRewardsCalculation() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].monthlyRewards[*].month").exists())
                .andExpect(jsonPath("$.content[*].monthlyRewards[*].points").exists());
    }

    @Test
    void testRewardsEndpointTotalPointsNotNull() throws Exception {
        mockMvc.perform(get("/api/rewards/customers/getRewards")
                        .param("start","2024-01-01")
                        .param("end","2024-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].totalPoints", everyItem(notNullValue())));
    }
}
