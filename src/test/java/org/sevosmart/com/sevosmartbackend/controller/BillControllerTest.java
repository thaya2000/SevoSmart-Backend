package org.sevosmart.com.sevosmartbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sevosmart.com.sevosmartbackend.dto.request.BillCalculatorRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.BillBenefitsRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.BillCalculatorResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.BillBenefitsResponse;
import org.sevosmart.com.sevosmartbackend.service.BillCalculatorService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class BillControllerTest {

    @Mock
    private BillCalculatorService billCalculatorService;

    @InjectMocks
    private BillController billController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
    }

    @Test
    void bill() throws Exception {
        // BillCalculatorRequest request = new BillCalculatorRequest(1500.0);
        BillCalculatorResponse response = new BillCalculatorResponse(2.0, 50);

        given(billCalculatorService.calculateBill(any(BillCalculatorRequest.class))).willReturn(response);

        mockMvc.perform(post("/bill/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"monthlyBill\": 1500.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unitPerMonth").value(50))
                .andExpect(jsonPath("$.recommendedSolarPower").value(2.0))
                .andDo(print());

        verify(billCalculatorService).calculateBill(any(BillCalculatorRequest.class));
    }

    @Test
    void calculateBenefits() throws Exception {
        // BillBenefitsRequest request = new BillBenefitsRequest(1500.0, 5);
        BillBenefitsResponse response = new BillBenefitsResponse(100.0, null);

        given(billCalculatorService.calculateBillBenefits(any(BillBenefitsRequest.class))).willReturn(response);

        mockMvc.perform(post("/bill/benefits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"monthlyBill\": 1500.0, \"selectedSolarPower\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.earnMoney").value(100.0))
                .andDo(print());

        verify(billCalculatorService).calculateBillBenefits(any(BillBenefitsRequest.class));
    }
}
