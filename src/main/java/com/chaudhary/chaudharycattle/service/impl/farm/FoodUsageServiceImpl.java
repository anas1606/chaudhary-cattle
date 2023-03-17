package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import com.chaudhary.chaudharycattle.model.farm.FoodUsageTableView;
import com.chaudhary.chaudharycattle.repositories.farm.FoodRepository;
import com.chaudhary.chaudharycattle.repositories.farm.FoodUsageRepository;
import com.chaudhary.chaudharycattle.service.farm.FoodUsageService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
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

    @Override
    public List<Food> getFoodList() {
        return foodRepository.findAll();
    }

    @Transactional
    @Override
    public void submit(String name, Double qty) {
        Food food = foodRepository.findByName(name);
        if(food!=null){
            try {
                food.setStock(food.getStock() - qty);
                foodRepository.save(food);
                FoodUsage foodUsage = new FoodUsage(food, LocalDate.now(), qty);
                foodUsageRepository.save(foodUsage);
            }catch (Exception e){
                CommanUtils.warningAlert("Warning", "Something Wrong.\nPlease Try Again");
            }
        }
    }

    @Override
    public List<FoodUsageTableView> getDataTable(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<FoodUsage> data = foodUsageRepository.findAllByCreatedDateBetween(LocalDate.now().minusDays(50), LocalDate.now().plusMonths(1), page).toList();
        return data.stream().map(FoodUsageTableView::new).collect(Collectors.toList());
    }

    @Override
    public int getTableDataCount() {
        LocalDate sdate = LocalDate.now();
        Integer count =  foodUsageRepository.countOfIdByCreatedDateBetween(sdate.minusDays(50), sdate.plusMonths(1));
        return count != null ? count : 0;
    }
}
