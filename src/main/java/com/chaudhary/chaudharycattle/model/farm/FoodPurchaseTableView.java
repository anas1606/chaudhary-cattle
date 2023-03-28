package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.farm.SupplierLedger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FoodPurchaseTableView {
    private String food;
    private String buyer;
    private Double rate;
    private Double qty;
    private Double amount;
    private LocalDate date;

    public FoodPurchaseTableView(SupplierLedger supplierLedger) {
        this.food = supplierLedger.getFId().getName();
        this.buyer = supplierLedger.getBId().getName();
        this.rate = supplierLedger.getRate();
        this.qty = supplierLedger.getQty();
        this.amount = supplierLedger.getAmount();
        this.date = supplierLedger.getCreatedDate();
    }
}
