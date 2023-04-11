package com.chaudhary.chaudharycattle.model.supplier;

import com.chaudhary.chaudharycattle.entities.farm.SupplierRepayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentStatementTableView {
    private String supplier;
    private double amount;
    private LocalDate date;

    public RepaymentStatementTableView (SupplierRepayment supplierRepayment){
        this.supplier = supplierRepayment.getBId().getName();
        this.amount = supplierRepayment.getAmount();
        this.date = supplierRepayment.getCreatedDate();
    }
}
