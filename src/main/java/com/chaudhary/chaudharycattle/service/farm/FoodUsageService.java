package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;

import java.time.LocalDate;
import java.util.List;

public interface FoodUsageService {
    List<Food> getFoodList ();
    List<Supplier> getBuyerList ();
    String getFoodUnit (String name);
    boolean submit (String name, Double qty, String shift, LocalDate date);
    List<FoodUsageTableView> getDataTable (int pageNo, int maxSize);
    int getTableDataCount();
    List<DashboardTableView> foodUsageRecord();
    void deleteRecord (FoodUsageTableView foodUsageTableView);
}
