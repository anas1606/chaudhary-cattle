package com.chaudhary.chaudharycattle.service.impl.reports;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import com.chaudhary.chaudharycattle.model.farm.MilkTableView;
import com.chaudhary.chaudharycattle.repositories.MilkRepository;
import com.chaudhary.chaudharycattle.service.reports.MilkReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MilkReportServiceImpl implements MilkReportService {
    @Autowired
    private MilkRepository milkRepository;

    @Override
    public List<MilkTableView> getFilterMilkReport(LocalDate startDate, LocalDate endDate, String code, String shift, int pageNo, int maxSize) {
        List<String> codeList = (code.equalsIgnoreCase("")) ? Arrays.asList("0599","0868") : Collections.singletonList(code);
        shift = (shift == null) ? "%" : shift;
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        return milkRepository.milkReport(startDate,endDate,shift,codeList,page).stream().map(MilkTableView::new).collect(Collectors.toList());
    }

    @Override
    public long getTableDataCount(LocalDate startDate, LocalDate endDate, String code, String shift) {
        List<String> codeList = (code.equalsIgnoreCase("")) ? Arrays.asList("0599","0868") : Collections.singletonList(code);
        shift = (shift == null) ? "%" : shift;
        return milkRepository.milkReportTotalCount(startDate,endDate,shift,codeList);
    }

    @Override
    public Double totalLitersOfMilkByShift(LocalDate startDate, LocalDate endDate, String code, Shift shift) {
        List<String> codeList = (code.equalsIgnoreCase("")) ? Arrays.asList("0599","0868") : Collections.singletonList(code);
        return milkRepository.sumOfLitersByCreatedDateBetweenAndShiftAndCode(startDate,endDate, shift.name(), codeList);
    }

    @Override
    public MilkRecordModel fatRateAndAmountOfReport(LocalDate startDate, LocalDate endDate, String code, String shiftName) {
        List<String> codeList = (code.equalsIgnoreCase("")) ? Arrays.asList("0599","0868") : Collections.singletonList(code);
        List<Shift> shiftList = (shiftName == null) ? Arrays.asList(Shift.EVENING, Shift.MORNING) : Collections.singletonList(Shift.valueOf(shiftName));
        return milkRepository.fatRateAmountOfmilkRecord(startDate,endDate,shiftList,codeList);
    }
}
