package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.MedicalTableView;

import java.time.LocalDate;
import java.util.List;

public interface MedicalService {
    boolean submit(String description, String supplier, double amount, LocalDate now, String paymentMode);
    boolean validSupplier (String name);
    List<MedicalTableView> getTableData (int pageNo, int maxSize);
    List<DashboardTableView> getRecTableData ();
    long  getTableDataCount ();
}
