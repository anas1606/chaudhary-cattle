package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Milk;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;
import com.chaudhary.chaudharycattle.repositories.farm.MilkRepository;
import com.chaudhary.chaudharycattle.service.farm.MilkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MilkServiceImpl implements MilkService {

    @Autowired
    private MilkRepository milkRepository;

    @Transactional
    @Override
    public void saveData(String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount, String code) {
        milkRepository.save( new Milk(Shift.valueOf(shift), date, liters, fat, rate, amount, code) );
    }

    @Override
    public List<MilkTableView> getTableData(int pageNo, int maxSize) {
        LocalDate sdate = LocalDate.now();
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<Milk> data = milkRepository.findAllByCreatedDateBetween(sdate.minusDays(50), sdate.plusMonths(1), page).toList();
        return data.stream().map(MilkTableView::new).collect(Collectors.toList());
    }

    @Override
    public int getTableDataCount() {
        LocalDate sdate = LocalDate.now();
        Integer count =  milkRepository.countOfIdByCreatedDateBetween(sdate.minusDays(50), sdate.plusMonths(1));
        return count != null ? count : 0;
    }

    @Override
    public Double totalLitersOfMilkByShift(Shift shift, String code) {
        Double val;
        if(code.equalsIgnoreCase("0"))
            return milkRepository.sumOfLitersByCreatedDateBetweenAndShiftAndCode(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1), shift.name(), Arrays.asList("0599","0868"));
        else
            return milkRepository.sumOfLitersByCreatedDateBetweenAndShiftAndCode(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1), shift.name(),Collections.singletonList(code));
    }

    @Override
    public MilkRecordModel milkRecord(String code) {
        if(code.equalsIgnoreCase("0"))
            return milkRepository.milkRecord(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1), Arrays.asList("0599","0868"));
        else
            return milkRepository.milkRecord(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1), Collections.singletonList(code));
    }
}
