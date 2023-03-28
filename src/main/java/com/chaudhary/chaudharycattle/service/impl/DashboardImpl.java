package com.chaudhary.chaudharycattle.service.impl;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.repositories.farm.FoodRepository;
import com.chaudhary.chaudharycattle.repositories.farm.MilkRepository;
import com.chaudhary.chaudharycattle.repositories.farm.SupplierRepository;
import com.chaudhary.chaudharycattle.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardImpl implements DashboardService {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MilkRepository milkRepository;
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
}
