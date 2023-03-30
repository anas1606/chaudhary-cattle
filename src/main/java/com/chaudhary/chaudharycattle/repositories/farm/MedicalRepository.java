package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.Medical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MedicalRepository extends JpaRepository<Medical, Integer> {
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM Medical WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    Double sumOfAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
}
