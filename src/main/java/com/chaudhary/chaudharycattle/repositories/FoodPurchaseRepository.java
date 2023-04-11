package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodPurchaseRepository extends JpaRepository<FoodPurchase, Long> {
    Page<FoodPurchase> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    @Query(value = "SELECT * FROM FoodPurchase WHERE fid = ?1 AND rQty > 0 ORDER BY createdDate ASC",nativeQuery = true)
    List<FoodPurchase> findAllByFid (Food food);
}
