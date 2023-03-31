package com.chaudhary.chaudharycattle.service.impl;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.repositories.farm.*;
import com.chaudhary.chaudharycattle.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MilkRepository milkRepository;
    @Autowired
    private FoodUsageRepository foodUsageRepository;
    @Autowired
    private MedicalRepository medicalRepository;
    private static final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    private static final LocalDate endDate = startDate.plusMonths(1).minusDays(1);
    @Override
    public List<DashboardTableView> getStockTable() {
        return foodRepository.findAllOrderByStockAsc().stream().map(DashboardTableView::new).collect(Collectors.toList());
    }
    @Override
    public List<DashboardTableView> getSupplierTable() {
        return supplierRepository.findAll().stream().map(DashboardTableView::new).collect(Collectors.toList());
    }
    @Override
    public Double getTotalMilkCountByCode(String code, Shift Shift) {
        return milkRepository.sumOfLitersByCreatedDateBetweenAndShiftAndCode(startDate,endDate, Shift.getShift(), Collections.singletonList(code));
    }
    @Override
    public Map<String, String> getProfitRecord() {
        Map<String,String> map = new HashMap<>();
        map.put("income",milkRepository.sumOfAmountByCreatedDateBetween(startDate,endDate).toString());
        map.put("foodExp",foodUsageRepository.sumOfAmountByCreatedDateBetween(startDate,endDate).toString());
        map.put("medExp",medicalRepository.sumOfAmountByCreatedDateBetween(startDate,endDate).toString());
        return map;
    }

    @Override
    public Double getTotalRemPayAmount() {
        return supplierRepository.sumOfAmount();
    }

    @Override
    public Double getCashBalance() {
        Double income = milkRepository.sumOfAmount();
        Double medicalCashPayment = medicalRepository.sumOfAmountByPymentMode(PaymentMode.CASH);
        return income - (medicalCashPayment);
    }
}
