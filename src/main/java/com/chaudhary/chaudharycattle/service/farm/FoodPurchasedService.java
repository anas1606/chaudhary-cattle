package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;

import java.util.List;

public interface FoodPurchasedService {
    boolean addNewFood (String name, String unit);
    boolean addNewBuyer (String name, String contact);
    List<FoodPurchase> getTableData ();
}
