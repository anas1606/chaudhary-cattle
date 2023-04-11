package com.chaudhary.chaudharycattle.service.supplier;

import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;

import java.time.LocalDate;
import java.util.List;

public interface RepaymentStatementService {
    List<RepaymentStatementTableView> getRepaymentStatement (LocalDate startDate, LocalDate endDate, String supplier, int pageNo, int maxSize);
    long getTableDataCount(LocalDate startDate, LocalDate endDate);
    double getRepaymentStatementTotal (LocalDate startDate, LocalDate endDate, String supplier);
}
