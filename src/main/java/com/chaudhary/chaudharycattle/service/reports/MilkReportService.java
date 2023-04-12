package com.chaudhary.chaudharycattle.service.reports;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;

import java.time.LocalDate;
import java.util.List;

public interface MilkReportService {
    List<MilkTableView> getFilterMilkReport (LocalDate startDate, LocalDate endDate, String code, String shift, int pageNo, int maxSize);
    long getTableDataCount (LocalDate startDate, LocalDate endDate, String code, String shift);
    Double totalLitersOfMilkByShift(LocalDate startDate, LocalDate endDate, String code, Shift shift);
    MilkRecordModel fatRateAndAmountOfReport (LocalDate startDate, LocalDate endDate, String code, String shift);
}
