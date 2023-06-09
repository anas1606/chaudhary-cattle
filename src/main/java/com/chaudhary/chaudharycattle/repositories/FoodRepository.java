package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Food findByName (String name);
    @Query(value = "SELECT stock FROM Food WHERE name = ?1",nativeQuery = true)
    Double findQtyByName (String name);
    @Query(value = "SELECT unit FROM Food WHERE name = ?1",nativeQuery = true)
    String findUnitByName (String name);
    @Query(value = "SELECT * FROM Food ORDER BY Stock ASC",nativeQuery = true)
    List<Food> findAllOrderByStockAsc ();
}
