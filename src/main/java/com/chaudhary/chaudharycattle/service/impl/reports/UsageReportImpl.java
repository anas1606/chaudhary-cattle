package com.chaudhary.chaudharycattle.service.impl.reports;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;
import com.chaudhary.chaudharycattle.repositories.FoodRepository;
import com.chaudhary.chaudharycattle.repositories.FoodUsageRepository;
import com.chaudhary.chaudharycattle.service.reports.UsageReportService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsageReportImpl implements UsageReportService {

    @Autowired
    private FoodUsageRepository foodUsageRepository;
    @Autowired
    private FoodRepository foodRepository;

    @Override
    public List<FoodUsageTableView> getFilterUsageReport(LocalDate startDate, LocalDate endDate, String food, String shift, int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        shift = (shift == null) ? "%" : shift;
        if (food == null || food.equalsIgnoreCase("")) {
            return foodUsageRepository.getFilterReport(startDate, endDate, shift, "%", page).stream().map(FoodUsageTableView::new).collect(Collectors.toList());
        }else {
            Food foodObj = foodRepository.findByName(food);
            if (foodObj != null) {
                return foodUsageRepository.getFilterReport(startDate, endDate, shift, String.valueOf(foodObj.getId()), page).stream().map(FoodUsageTableView::new).collect(Collectors.toList());
            } else {
                CommanUtils.warningAlert("Warning", "Invalid Food Name");
                return new ArrayList<>();
            }
        }
    }

    @Override
    public long getFilterUsageReportTotalCount(LocalDate startDate, LocalDate endDate, String food, String shift) {
        shift = (shift == null) ? "%" : shift;
        if (food == null || food.equalsIgnoreCase("")) {
            return foodUsageRepository.getFilterReportTotalCount(startDate, endDate, shift, "%");
        }else {
            Food foodObj = foodRepository.findByName(food);
            if (foodObj != null) {
                return foodUsageRepository.getFilterReportTotalCount(startDate, endDate, shift, String.valueOf(foodObj.getId()));
            } else {
                return 0;
            }
        }
    }

    @Override
    public List<DashboardTableView> getFilterFoodAmounts(LocalDate startDate, LocalDate endDate, String food, String shift) {
        List<Shift> shiftList = (shift == null) ? Arrays.asList(Shift.EVENING, Shift.MORNING) : Collections.singletonList(Shift.valueOf(shift));
        if (food == null || food.equalsIgnoreCase("")) {
            return foodUsageRepository.getFilterFoodAmounts(startDate, endDate, shiftList);
        }else {
            Food foodObj = foodRepository.findByName(food);
            if (foodObj != null) {
                return foodUsageRepository.getFilterFoodAmounts(startDate, endDate, shiftList, foodObj);
            } else {
                return new ArrayList<>();
            }
        }
    }
    @Override
    public double getFilterFoodsTotalAmount(LocalDate startDate, LocalDate endDate, String food, String shift) {
        shift = (shift == null) ? "%" : shift;
        if (food == null || food.equalsIgnoreCase("")) {
            return foodUsageRepository.getFilterFoodsTotalAmount(startDate, endDate, shift, "%");
        }else {
            Food foodObj = foodRepository.findByName(food);
            if (foodObj != null) {
                return foodUsageRepository.getFilterFoodsTotalAmount(startDate, endDate, shift, String.valueOf(foodObj.getId()));
            } else {
                return 0.0;
            }
        }
    }
}
