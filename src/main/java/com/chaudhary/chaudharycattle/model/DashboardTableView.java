package com.chaudhary.chaudharycattle.model;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardTableView {
    private String name;
    private Double amount;

    public DashboardTableView(Food food){
        this.name = food.getName();
        this.amount = Double.parseDouble(String.format("%.2f", food.getStock()));
    }
    public DashboardTableView(Supplier supplier){
        this.name = supplier.getName();
        this.amount = Double.parseDouble(String.format("%.2f", supplier.getAmount()));
    }
}
