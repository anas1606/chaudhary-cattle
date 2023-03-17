package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;

import java.util.List;

public interface FoodUsageService {
    List<Food> getFoodList ();
    void submit (String name, Double qty);
    List<FoodUsageTableView> getDataTable (int pageNo, int maxSize);

    int getTableDataCount();
}
