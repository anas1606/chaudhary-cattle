package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FoodPurchaseRepository extends JpaRepository<FoodPurchase, Integer> {
    Page<FoodPurchase> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
}
