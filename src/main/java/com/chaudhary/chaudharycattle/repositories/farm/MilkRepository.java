package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Milk;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilkRepository extends JpaRepository<Milk, Long> {

    List<Milk> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(liters) FROM Milk WHERE createdDate BETWEEN ?1 AND ?2 AND shift = ?3")
    Double sumOfLitersByCreatedDateBetweenAndShift (LocalDate startDate, LocalDate endDate, Shift shift);

    @Query("SELECT new com.chaudhary.chaudharycattle.model.farm.MilkRecordModel( ROUND(AVG(fat),2), ROUND(AVG(rate),2), ROUND(SUM(amount),2) ) FROM Milk WHERE createdDate BETWEEN ?1 AND ?2")
    MilkRecordModel MilkRecord (LocalDate startDate, LocalDate endDate);
}
