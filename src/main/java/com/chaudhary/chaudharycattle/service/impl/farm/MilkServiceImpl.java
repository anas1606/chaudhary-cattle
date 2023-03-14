package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Milk;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;
import com.chaudhary.chaudharycattle.repositories.farm.MilkRepository;
import com.chaudhary.chaudharycattle.service.farm.MilkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MilkServiceImpl implements MilkService {

    @Autowired
    private MilkRepository milkRepository;

    @Transactional
    @Override
    public void saveData(String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount) {
        milkRepository.save( new Milk(Shift.valueOf(shift), date, liters, fat, rate, amount) );
    }

    @Override
    public List<MilkTableView> getTableData() {
        LocalDate sdate = LocalDate.now();
        List<Milk> data = milkRepository.findAllByCreatedDateBetween(sdate.minusDays(10), sdate);
        return data.stream().map(MilkTableView::new).collect(Collectors.toList());
    }

    @Override
    public Double totalLitersOfMilkByShift(Shift shift) {
        Double val = milkRepository.sumOfLitersByCreatedDateBetweenAndShift(LocalDate.now().minusDays(10),LocalDate.now(), shift);
        return (val != null) ? val : 0.0;
    }

    @Override
    public MilkRecordModel milkRecord() {
        return milkRepository.MilkRecord(LocalDate.now().minusDays(10),LocalDate.now());
    }
}
