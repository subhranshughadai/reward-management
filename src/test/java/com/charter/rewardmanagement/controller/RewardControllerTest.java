package com.charter.rewardmanagement.controller;

import com.charter.rewardmanagement.dto.MonthlyReward;
import com.charter.rewardmanagement.dto.RewardResponse;
import com.charter.rewardmanagement.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RewardController.
 */
class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnRewardsSuccessfully() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(120);
        response.setMonthlyRewards(Arrays.asList(
                new MonthlyReward("2024-01", 120)
        ));

        Page<RewardResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().getContent().size());
        assertEquals(1L, result.getBody().getContent().get(0).getCustomerId());
    }

    @Test
    void shouldReturnEmptyPageWhenNoRewards() {
        Page<RewardResponse> emptyPage = new PageImpl<>(Collections.emptyList());

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(emptyPage);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().getContent().isEmpty());
    }

    @Test
    void shouldHandleMultipleCustomersInResponse() {
        RewardResponse response1 = new RewardResponse();
        response1.setCustomerId(1L);
        response1.setTotalPoints(120);
        response1.setMonthlyRewards(Arrays.asList(new MonthlyReward("2024-01", 120)));

        RewardResponse response2 = new RewardResponse();
        response2.setCustomerId(2L);
        response2.setTotalPoints(50);
        response2.setMonthlyRewards(Arrays.asList(new MonthlyReward("2024-01", 50)));

        Page<RewardResponse> page = new PageImpl<>(Arrays.asList(response1, response2));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2, result.getBody().getContent().size());
    }

    @Test
    void shouldHandlePaginationParameters() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(100);

        Page<RewardResponse> page = new PageImpl<>(
                Collections.singletonList(response),
                org.springframework.data.domain.PageRequest.of(1, 5),
                10
        );

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                1,
                5
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                1,
                5
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().getNumber());
        assertEquals(5, result.getBody().getSize());
        assertEquals(10, result.getBody().getTotalElements());
    }

    @Test
    void shouldHandleMultipleMonthlyRewards() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(300);
        response.setMonthlyRewards(Arrays.asList(
                new MonthlyReward("2024-01", 100),
                new MonthlyReward("2024-02", 100),
                new MonthlyReward("2024-03", 100)
        ));

        Page<RewardResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(3, result.getBody().getContent().get(0).getMonthlyRewards().size());
        assertEquals(300, result.getBody().getContent().get(0).getTotalPoints());
    }

    @Test
    void shouldHandleSameDateRange() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(90);

        Page<RewardResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 15),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 15),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(1, result.getBody().getContent().size());
    }

    @Test
    void shouldHandleZeroRewardPoints() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(0);
        response.setMonthlyRewards(Arrays.asList(new MonthlyReward("2024-01", 0)));

        Page<RewardResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(0, result.getBody().getContent().get(0).getTotalPoints());
    }

    @Test
    void shouldHandleDefaultPaginationValues() {
        RewardResponse response = new RewardResponse();
        response.setCustomerId(1L);
        response.setTotalPoints(150);

        Page<RewardResponse> page = new PageImpl<>(Collections.singletonList(response));

        when(rewardService.getAllCustomerRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        )).thenReturn(page);

        ResponseEntity<Page<RewardResponse>> result = rewardController.getRewards(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31),
                0,
                10
        );

        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
    }
}
