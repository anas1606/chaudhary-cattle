package com.chaudhary.chaudharycattle.model.supplier;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTableView {
    private Supplier sid;
    private Food fid;
    private LocalDate date;
    private Double rate;
    private Double qty;
    private Double amount;
}
