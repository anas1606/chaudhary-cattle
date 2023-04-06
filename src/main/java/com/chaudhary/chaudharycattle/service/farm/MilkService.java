package com.chaudhary.chaudharycattle.service.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;

import java.time.LocalDate;
import java.util.List;

public interface MilkService {
    void saveData (String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount, String code);

    List<MilkTableView> getTableData (int pageNo, int maxSize);
    int getTableDataCount ();
    Double totalLitersOfMilkByShift (Shift shift, String code);
    MilkRecordModel milkRecord (String code);
    void deleteRecord (long id);
}
