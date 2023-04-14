package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FoodUsageTableView {
    private Long id;
    private String food;
    private LocalDate date;
    private Double qty;
    private Double rate;
    private Double amount;
    private String unit;
    private String shift;

    public FoodUsageTableView (FoodUsage foodUsage){
        this.id = foodUsage.getId();
        this.food = foodUsage.getFId().getName();
        this.date = foodUsage.getCreatedDate();
        this.rate = foodUsage.getRate();
        this.qty = foodUsage.getQty();
        this.amount = foodUsage.getAmount();
        this.unit = foodUsage.getFId().getUnit();
        this.shift = foodUsage.getShift().getShift();
    }
}
