package org.sevosmart.com.sevosmartbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sevosmart.com.sevosmartbackend.dto.request.BillBenefitsRequest;
// import org.sevosmart.com.sevosmartbackend.dto.request.BillCalculatorRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.BillBenefitsResponse;
// import org.sevosmart.com.sevosmartbackend.dto.response.BillCalculatorResponse;

import static org.junit.jupiter.api.Assertions.*;

class BillCalculatorServiceImplTest {

    private BillCalculatorServiceImpl billCalculatorService;

    @BeforeEach
    void setUp() {
        billCalculatorService = new BillCalculatorServiceImpl();
    }

    @Test
    void calculateBillBenefits_EnoughSolarPower() {
        BillBenefitsRequest request = new BillBenefitsRequest(4000.0, 200);
        BillBenefitsResponse response = billCalculatorService.calculateBillBenefits(request);
        assertNotNull(response.getEarnMoney());
        assertTrue(response.getEarnMoney() > 0);
    }

    @Test
    void calculateBillBenefits_ExactlyZeroSolar() {
        BillBenefitsRequest request = new BillBenefitsRequest(4000.0, 0);
        BillBenefitsResponse response = billCalculatorService.calculateBillBenefits(request);
        assertEquals(0, response.getSaveMoney(), 0.01);
    }
}
