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
    private String unit;

    public FoodUsageTableView (FoodUsage foodUsage){
        this.id = foodUsage.getId();
        this.food = foodUsage.getFId().getName();
        this.date = foodUsage.getCreatedDate();
        this.qty = foodUsage.getQty();
        this.unit = foodUsage.getFId().getUnit();
    }
}
