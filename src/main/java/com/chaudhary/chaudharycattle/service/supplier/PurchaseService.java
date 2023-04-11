package com.chaudhary.chaudharycattle.service.supplier;

import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseService {
    List<FoodPurchaseTableView> getFilteredPurchaseRecords (LocalDate startDate, LocalDate endDate, String supplier, int pageNo, int maxSize);
    double getFilteredPurchaseRecordsTotal (LocalDate startDate, LocalDate endDate, String supplier);
    long getTableDataCount(LocalDate startDate, LocalDate endDate);
}
