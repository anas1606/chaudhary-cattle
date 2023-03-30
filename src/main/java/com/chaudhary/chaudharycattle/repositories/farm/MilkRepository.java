package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.Milk;
import com.chaudhary.chaudharycattle.model.farm.MilkRecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MilkRepository extends JpaRepository<Milk, Long> {

    Page<Milk> findAllByCreatedDateBetween(LocalDate startDate, LocalDate endDate, Pageable page);
    Integer countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
    @Query(value = "SELECT IFNULL(ROUND(SUM(liters),2),0.0) FROM Milk WHERE createdDate BETWEEN ?1 AND ?2 AND shift = ?3 AND code IN (?4)",nativeQuery = true)
    Double sumOfLitersByCreatedDateBetweenAndShiftAndCode(LocalDate startDate, LocalDate endDate, String shift, List<String> codes);

    @Query("SELECT new com.chaudhary.chaudharycattle.model.farm.MilkRecordModel( ROUND(AVG(fat),2), ROUND(AVG(rate),2), ROUND(SUM(amount),2) ) FROM Milk WHERE createdDate BETWEEN ?1 AND ?2 AND code IN (?3)")
    MilkRecordModel milkRecord (LocalDate startDate, LocalDate endDate, List<String> codes);
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM Milk WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    Double sumOfAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
}
