package com.chaudhary.chaudharycattle.service.supplier;

import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;

import java.time.LocalDate;
import java.util.List;

public interface SupplierRepaymentService {
    List<RepaymentStatementTableView> getSupplierRepayments (int pageNo, int maxSize);
    long getTableDataCount();
    double getRepaymentTotal ();
    double getSupplieroldBalance (String supplier);
    void submit (String supplier, LocalDate date, double amount);
}
