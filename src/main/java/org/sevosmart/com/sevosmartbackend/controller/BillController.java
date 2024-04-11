package org.sevosmart.com.sevosmartbackend.controller;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.BillBenefitsRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.BillCalculatorRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.BillBenefitsResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.BillCalculatorResponse;
import org.sevosmart.com.sevosmartbackend.service.BillCalculatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {
    private final BillCalculatorService billCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<BillCalculatorResponse> bill(@RequestBody BillCalculatorRequest request){
        return ResponseEntity.ok(billCalculatorService.calculateBill(request));
    }

    @PostMapping("/benefits")
    public ResponseEntity<BillBenefitsResponse> calculateBenefits(@RequestBody BillBenefitsRequest request){
        return ResponseEntity.ok(billCalculatorService.calculateBillBenefits(request));
    }


}
