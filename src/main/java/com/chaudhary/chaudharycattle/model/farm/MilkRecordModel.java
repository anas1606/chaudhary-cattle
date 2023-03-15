package com.chaudhary.chaudharycattle.model.farm;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MilkRecordModel {
    private Double fat;
    private Double rate;
    private Double amount;

    public MilkRecordModel(Double fat, Double rate, Double amount) {
        this.fat = fat!=null ? fat : 0.0;
        this.rate = rate!=null ? rate : 0.0;
        this.amount = amount!=null ? amount : 0.0;
    }
}
