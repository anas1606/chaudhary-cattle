package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FoodUsageRepository extends JpaRepository<FoodUsage, Integer> {

    Page<FoodUsage> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    Integer countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT SUM(qty) FROM FoodUsage WHERE createdDate BETWEEN ?1 AND ?2 AND fk_food_id = ?3",nativeQuery = true)
    Double sumOfQtyByCreatedDateBetweenAndFk_food_id (LocalDate startDate, LocalDate endDate, int food);
}
