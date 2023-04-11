package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierRepayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SupplierRepaymentRepository extends JpaRepository <SupplierRepayment, Long> {

    Page<SupplierRepayment> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    @Query("SELECT sr FROM SupplierRepayment sr WHERE Suppl BETWEEN ?1 AND ?2 AND bId = ?3")
    Page<SupplierRepayment> findAllByCreatedDateBetweenAndBId (LocalDate startDate, LocalDate endDate, Supplier supplier, Pageable page);
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM SupplierRepayment sr WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    double sumAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM SupplierRepayment sr WHERE createdDate BETWEEN ?1 AND ?2 AND bId = ?3",nativeQuery = true)
    double sumAmountByCreatedDateBetweenAndBId (LocalDate startDate, LocalDate endDate, Supplier supplier);
    Long countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
}
