package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;
import com.chaudhary.chaudharycattle.repositories.FoodPurchaseRepository;
import com.chaudhary.chaudharycattle.repositories.SupplierRepository;
import com.chaudhary.chaudharycattle.repositories.FoodRepository;
import com.chaudhary.chaudharycattle.repositories.FoodUsageRepository;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodUsageServiceImpl implements FoodUsageService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodUsageRepository foodUsageRepository;
    @Autowired
    private FoodPurchaseRepository foodPurchaseRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    private static final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    private static final LocalDate endDate = startDate.plusMonths(1).minusDays(1);

    @Override
    public List<Food> getFoodList() {
        return foodRepository.findAll();
    }

    @Override
    public List<Supplier> getBuyerList() {
        return supplierRepository.findAll();
    }

    @Override
    public String getFoodUnit(String name) {
        String unit = foodRepository.findUnitByName(name);
        if (unit != null)
            return unit;
        else
            return "";
    }

    @Transactional
    @Override
    public boolean submit(String name, Double qty, String shift, LocalDate date) {
        Food food = foodRepository.findByName(name);
        if (food != null) {
            if (foodRepository.findQtyByName(name) >= qty) {
                food.setStock(food.getStock() - qty);
                foodRepository.save(food);

                List<FoodPurchase> foodPurchaseList = foodPurchaseRepository.findAllByFid(food);
                Double tQty = qty;
                for (FoodPurchase foodPurchase : foodPurchaseList) {
                    if (foodPurchase.getRQty() >= tQty) {
//                      Update Food Purchased Stock
                        foodPurchase.setRQty(foodPurchase.getRQty() - tQty);
                        foodPurchaseRepository.save(foodPurchase);
                        FoodUsage foodUsage = new FoodUsage(food, foodPurchase, date, tQty, Shift.valueOf(shift), foodPurchase.getRate(), foodPurchase.getRate()*tQty);
                        foodUsageRepository.save(foodUsage);
                        break;
                    } else {

                        FoodUsage foodUsage = new FoodUsage(food, foodPurchase ,date, foodPurchase.getRQty(), Shift.valueOf(shift), foodPurchase.getRate(), foodPurchase.getRate()* foodPurchase.getRQty());
                        foodUsageRepository.save(foodUsage);

                        tQty = tQty - foodPurchase.getRQty();
                        foodPurchase.setRQty(0.0);
                        foodPurchaseRepository.save(foodPurchase);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FoodUsageTableView> getDataTable(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo, maxSize, Sort.Direction.DESC, "createdDate");
        List<FoodUsage> data = foodUsageRepository.findAllByCreatedDateBetween(startDate, endDate, page).toList();
        return data.stream().map(FoodUsageTableView::new).collect(Collectors.toList());
    }

    @Override
    public int getTableDataCount() {
        Integer count = foodUsageRepository.countOfIdByCreatedDateBetween(startDate, endDate);
        return count != null ? count : 0;
    }

    @Override
    public List<DashboardTableView> foodUsageRecord() {
        return foodUsageRepository.sumOfQtyByCreatedDateBetweenAndFk_food_idAndGroupBy(startDate,endDate);
    }
    @Transactional
    @Override
    public void deleteRecord(FoodUsageTableView foodUsageTableView) {
        Food food = foodRepository.findByName(foodUsageTableView.getFood());
        food.setStock(food.getStock()+foodUsageTableView.getQty());
        foodRepository.save(food);

        FoodUsage foodUsage = foodUsageRepository.findById(foodUsageTableView.getId()).orElse(null);
        assert foodUsage != null;
        FoodPurchase foodPurchase = foodUsage.getFpId();
        foodPurchase.setRQty(foodPurchase.getRQty()+foodUsageTableView.getQty());
        foodPurchaseRepository.save(foodPurchase);

        foodUsageRepository.deleteById(foodUsageTableView.getId());
    }
}
