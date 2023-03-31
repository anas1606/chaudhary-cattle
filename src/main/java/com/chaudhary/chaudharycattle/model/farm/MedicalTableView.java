package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.chaudhary.chaudharycattle.entities.farm.Medical;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class MedicalTableView {
    private String description;
    private double amount;
    private String supplier;
    private LocalDate date;
    private String paymentMode;

    public MedicalTableView (Medical medical) {
        this.description = medical.getDescription();
        this.amount = medical.getAmount();
        this.supplier = medical.getBId().getName();
        this.date = medical.getCreatedDate();
        this.paymentMode = (medical.getPaymentMode()== PaymentMode.CASH) ? "CASH" : "PAYLATER";
    }
}
