package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.farm.Milk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilkTableView {
    private Long id;
    private String shift;
    private LocalDate date;
    private Double liters;
    private Double fat;
    private Double rate;
    private Double amount;
    private Double action;

    public MilkTableView (Milk milk){
        this.id = milk.getId();
        this.shift = milk.getShift().name();
        this.date = milk.getCreatedDate();
        this.liters = milk.getLiters();
        this.fat = milk.getFat();
        this.rate = milk.getRate();
        this.amount = milk.getAmount();
    }
}
