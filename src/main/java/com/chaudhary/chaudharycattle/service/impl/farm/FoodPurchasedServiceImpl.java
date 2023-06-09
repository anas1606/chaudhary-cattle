package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierLedger;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;
import com.chaudhary.chaudharycattle.repositories.SupplierLedgerRepository;
import com.chaudhary.chaudharycattle.repositories.SupplierRepository;
import com.chaudhary.chaudharycattle.repositories.FoodPurchaseRepository;
import com.chaudhary.chaudharycattle.repositories.FoodRepository;
import com.chaudhary.chaudharycattle.service.farm.FoodPurchasedService;
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
public class FoodPurchasedServiceImpl implements FoodPurchasedService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private FoodPurchaseRepository foodPurchaseRepository;
    @Autowired
    private SupplierLedgerRepository supplierLedgerRepository;
    private static final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    private static final LocalDate endDate = startDate.plusMonths(1).minusDays(1);
    @Transactional
    @Override
    public boolean submit(String foodName, String buyerName, Double rate, Double qty, Double amount, LocalDate date) {
        Food food = foodRepository.findByName(foodName);
        Supplier buyer = supplierRepository.findByName(buyerName);
        if(food!= null && buyer != null){
            buyer.setAmount(buyer.getAmount()+amount);
            supplierRepository.save(buyer);
            SupplierLedger model = new SupplierLedger(buyer,food,rate,qty,amount,date);
            supplierLedgerRepository.save(model);
            food.setStock(food.getStock()+qty);
            foodRepository.save(food);
            FoodPurchase foodPurchase = new FoodPurchase(food,rate,qty,qty,amount,date);
            foodPurchaseRepository.save(foodPurchase);
            return true;
        }
        return false;
    }

    @Override
    public boolean addNewFood(String name, String unit) {
        if(foodRepository.findByName(name) == null) {
            foodRepository.save(new Food(name.toUpperCase(), unit.toUpperCase(), 0.0));
            return true;
        }
        return false;
    }
    @Override
    public boolean addNewBuyer(String name, String contact) {
        if(supplierRepository.findByName(name) == null) {
            supplierRepository.save(new Supplier(name.toUpperCase(), contact, 0.0));
            return true;
        }
        return false;
    }
    @Override
    public List<FoodPurchaseTableView> getTableData(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<SupplierLedger> foodPurchases = supplierLedgerRepository.findAllByCreatedDateBetween(startDate,endDate, page).toList();
        return foodPurchases.stream().map(FoodPurchaseTableView::new).collect(Collectors.toList());
    }
    @Override
    public long getTableDataCount() {
        Long count =  supplierLedgerRepository.countOfIdByCreatedDateBetween(startDate, endDate);
        return count != null ? count : 0;
    }
}
