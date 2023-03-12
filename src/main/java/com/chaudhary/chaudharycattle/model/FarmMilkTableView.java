package com.chaudhary.chaudharycattle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmMilkTableView {
    private String id;
    private String shift;
    private String date;
    private String liters;
    private String fat;
    private String rate;
    private String amount;
    private String action;
}
