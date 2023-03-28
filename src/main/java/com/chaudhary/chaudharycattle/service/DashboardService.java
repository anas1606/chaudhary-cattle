package com.chaudhary.chaudharycattle.service;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import java.util.List;

public interface DashboardService {
    List<DashboardTableView> getStockTable ();
    List<DashboardTableView> getSupplierTable();
    Double getTotalMilkCountByCode (String code, Shift Shift);
}
