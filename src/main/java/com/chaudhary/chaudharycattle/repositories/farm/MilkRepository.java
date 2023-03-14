package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.Milk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MilkRepository extends JpaRepository<Milk, Long> {
}
