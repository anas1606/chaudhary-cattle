package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.farm.Buyer;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import com.chaudhary.chaudharycattle.repositories.farm.BuyerRepository;
import com.chaudhary.chaudharycattle.repositories.farm.FoodPurchaseRepository;
import com.chaudhary.chaudharycattle.repositories.farm.FoodRepository;
import com.chaudhary.chaudharycattle.service.farm.FoodPurchasedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FoodPurchasedServiceImpl implements FoodPurchasedService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private FoodPurchaseRepository foodPurchaseRepository;
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
    public List<FoodPurchase> getTableData() {
        return foodPurchaseRepository.findAllByCreatedDateBetween(LocalDate.now().minusDays(50),LocalDate.now().plusMonths(1));
    }
}
