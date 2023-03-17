package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FoodUsageTableView {
    private int id;
    private String food;
    private LocalDate date;
    private Double qty;

    public FoodUsageTableView (FoodUsage foodUsage){
        this.id = foodUsage.getId();
        this.food = foodUsage.getFood().getName();
        this.date = foodUsage.getCreatedDate();
        this.qty = foodUsage.getQty();
    }
}
