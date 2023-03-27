package com.chaudhary.chaudharycattle.model.farm;

import com.chaudhary.chaudharycattle.entities.farm.Buyer;
import com.chaudhary.chaudharycattle.entities.farm.BuyerLedger;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
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

    public FoodPurchaseTableView(BuyerLedger buyerLedger) {
        this.food = buyerLedger.getFId().getName();
        this.buyer = buyerLedger.getBId().getName();
        this.rate = buyerLedger.getRate();
        this.qty = buyerLedger.getQty();
        this.amount = buyerLedger.getAmount();
        this.date = buyerLedger.getCreatedDate();
    }
}
