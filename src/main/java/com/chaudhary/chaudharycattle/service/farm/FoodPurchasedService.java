package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;
import java.time.LocalDate;
import java.util.List;

public interface FoodPurchasedService {
    boolean addNewFood (String name, String unit);
    boolean addNewBuyer (String name, String contact);
    boolean submit (String food, String buyer, Double rate, Double qty, Double amount, LocalDate date);
    List<FoodPurchaseTableView> getTableData (int pageNo, int maxSize);
    long getTableDataCount ();
}
