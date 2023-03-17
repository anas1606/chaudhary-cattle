package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Food findByName (String name);
}
