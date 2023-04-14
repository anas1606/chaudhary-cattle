package com.chaudhary.chaudharycattle.service.reports;

import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;

import java.time.LocalDate;
import java.util.List;

public interface UsageReportService {
    List<FoodUsageTableView> getFilterUsageReport (LocalDate startDate, LocalDate endDate, String food, String shift, int pageNo, int maxSize);
    long getFilterUsageReportTotalCount (LocalDate startDate, LocalDate endDate, String food, String shift);
    List<DashboardTableView> getFilterFoodAmounts (LocalDate startDate, LocalDate endDate, String food, String shift);
    double getFilterFoodsTotalAmount (LocalDate startDate, LocalDate endDate, String food, String shift);
}
