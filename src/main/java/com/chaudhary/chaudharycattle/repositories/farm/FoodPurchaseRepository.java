package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodPurchaseRepository extends JpaRepository<FoodPurchase, Integer> {
    List<FoodPurchase> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
}
