package org.sevosmart.com.sevosmartbackend.service;

import org.sevosmart.com.sevosmartbackend.dto.request.BillBenefitsRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.BillCalculatorRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.BillBenefitsResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.BillCalculatorResponse;

public interface BillCalculatorService {
    public BillCalculatorResponse calculateBill(BillCalculatorRequest request);

    public BillBenefitsResponse calculateBillBenefits(BillBenefitsRequest request);
}

