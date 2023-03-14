package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;

import java.time.LocalDate;
import java.util.List;

public interface MilkService {
    void saveData (String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount);

    List<MilkTableView> getTableData ();
    Double totalLitersOfMilkByShift (Shift shift);
    MilkRecordModel milkRecord ();
}
