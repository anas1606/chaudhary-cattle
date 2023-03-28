package com.chaudhary.chaudharycattle.service;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    List<DashboardTableView> getStockTable ();
    List<DashboardTableView> getSupplierTable();
    Double getTotalMilkCountByCode (String code, Shift Shift);
    Map<String, String> getProfitRecord ();
}
