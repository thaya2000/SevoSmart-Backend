package org.sevosmart.com.sevosmartbackend.service;

import lombok.RequiredArgsConstructor;
import org.sevosmart.com.sevosmartbackend.dto.request.BillBenefitsRequest;
import org.sevosmart.com.sevosmartbackend.dto.request.BillCalculatorRequest;
import org.sevosmart.com.sevosmartbackend.dto.response.BillBenefitsResponse;
import org.sevosmart.com.sevosmartbackend.dto.response.BillCalculatorResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillCalculatorServiceImpl implements BillCalculatorService {

    @Override
    public BillCalculatorResponse calculateBill(BillCalculatorRequest request) {
        Double monthlyBill = request.getMonthlyBill();
        double solarPower;
        int unitPerMonth;

        if(monthlyBill < 1290){
            if(monthlyBill < 390){
                unitPerMonth = (int) ((monthlyBill - 150) /8);
            }else{
                unitPerMonth = (int) ((monthlyBill - 390 - 300) / 20.00 + 30);
            }
        }else if (monthlyBill < 2800) {
            unitPerMonth = (int) ((monthlyBill - 1500 - 400 ) / 30.00 + 60);
        }else if (monthlyBill < 5300) {
            unitPerMonth = (int) ((monthlyBill - 2800 - 1000) /50.00 + 90);
        } else if (monthlyBill < 9800) {
            unitPerMonth = (int) ((monthlyBill - 5300 - 1500) / 50.00 + 120);
        }else{
            unitPerMonth = (int) ((monthlyBill - 11800) / 75.00 + 180);
        }

        solarPower = (unitPerMonth) / (8*0.4*30);

        return BillCalculatorResponse.builder()
                .recommendedSolarPower(solarPower)
                .unitPerMonth(unitPerMonth)
                .build();

    }



    @Override
    public BillBenefitsResponse calculateBillBenefits(BillBenefitsRequest request) {

        BillCalculatorRequest billCalculatorRequest = BillCalculatorRequest.builder().monthlyBill(request.getMonthlyBill()).build();
        BillCalculatorResponse billCalculatorResponse = calculateBill(billCalculatorRequest);
        Integer customerUsedUnitPerMonth = billCalculatorResponse.getUnitPerMonth();
        Integer selectedSolarPower = request.getSelectedSolarPower();
        Integer solarGeneratedUnitPerMonth = (int) (selectedSolarPower * 30 * 8 * 0.4);

        if (solarGeneratedUnitPerMonth >= customerUsedUnitPerMonth){
            return BillBenefitsResponse.builder().earnMoney((double) ((solarGeneratedUnitPerMonth - customerUsedUnitPerMonth)*36)).build();
        }else {
            int usedUnit;
            double customerMonthlyBillAfterSolarInstallation;
            usedUnit = customerUsedUnitPerMonth - solarGeneratedUnitPerMonth;

            if(usedUnit < 60){
                if (usedUnit < 30 ){
                    customerMonthlyBillAfterSolarInstallation = usedUnit * 8 + 150;
                }else {
                    customerMonthlyBillAfterSolarInstallation = (usedUnit-30)*20 + 390 + 300;
                }
            }else{
                if(usedUnit < 90){
                    customerMonthlyBillAfterSolarInstallation = (usedUnit - 60)*30 + 400 +1500;
                } else if (usedUnit < 120) {
                    customerMonthlyBillAfterSolarInstallation = (usedUnit - 90)*50 + 1000 + 2800;
                } else if (usedUnit < 180) {
                    customerMonthlyBillAfterSolarInstallation = (usedUnit - 120)*50 + 1500 + 5300;
                }else{
                    customerMonthlyBillAfterSolarInstallation = (usedUnit - 180)*75 + 2000 + 9800;
                }
            }
            return BillBenefitsResponse.builder().saveMoney(request.getMonthlyBill() - customerMonthlyBillAfterSolarInstallation).build();
        }
    }
}
