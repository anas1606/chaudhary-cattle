package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.farm.Buyer;
import com.chaudhary.chaudharycattle.entities.farm.BuyerLedger;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;
import com.chaudhary.chaudharycattle.repositories.farm.BuyerLedgerRepository;
import com.chaudhary.chaudharycattle.repositories.farm.BuyerRepository;
import com.chaudhary.chaudharycattle.repositories.farm.FoodPurchaseRepository;
import com.chaudhary.chaudharycattle.repositories.farm.FoodRepository;
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
    private BuyerRepository buyerRepository;
    @Autowired
    private FoodPurchaseRepository foodPurchaseRepository;
    @Autowired
    private BuyerLedgerRepository buyerLedgerRepository;

    @Transactional
    @Override
    public boolean submit(String foodName, String buyerName, Double rate, Double qty, Double amount, LocalDate date) {
        Food food = foodRepository.findByName(foodName);
        Buyer buyer = buyerRepository.findByName(buyerName);
        if(food!= null && buyer != null){
            BuyerLedger model = new BuyerLedger(buyer,food,rate,qty,amount,date);
            buyerLedgerRepository.save(model);
            food.setStock(food.getStock()+qty);
            foodRepository.save(food);
            FoodPurchase foodPurchase = new FoodPurchase(food,rate,qty,qty,date);
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
        if(buyerRepository.findByName(name) == null) {
            buyerRepository.save(new Buyer(name.toUpperCase(), contact, 0.0));
            return true;
        }
        return false;
    }
    @Override
    public List<FoodPurchaseTableView> getTableData(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<BuyerLedger> foodPurchases = buyerLedgerRepository.findAllByCreatedDateBetween(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1), page).toList();
        return foodPurchases.stream().map(FoodPurchaseTableView::new).collect(Collectors.toList());
    }
    @Override
    public int getTableDataCount() {
        Integer count =  buyerLedgerRepository.countOfIdByCreatedDateBetween(LocalDate.now().minusDays(50), LocalDate.now().plusMonths(1));
        return count != null ? count : 0;
    }
}
