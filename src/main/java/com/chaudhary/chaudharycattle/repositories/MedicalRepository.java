package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.chaudhary.chaudharycattle.entities.farm.Medical;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRepository extends JpaRepository<Medical, Long> {
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM Medical WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    Double sumOfAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
    Page<Medical> findAllByCreatedDateBetween(LocalDate startDate, LocalDate endDate, Pageable page);
    Long countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT new com.chaudhary.chaudharycattle.model.DashboardTableView( s.name, ROUND(SUM(m.amount),2) ) FROM Medical AS m INNER JOIN Supplier AS s ON m.bId = s.bId WHERE m.createdDate BETWEEN ?1 AND ?2 GROUP BY m.bId ")
    List<DashboardTableView> sumOfAmountBYSupplierAndCreatedDateAndGroupBy (LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM Medical WHERE paymentMode = ?1",nativeQuery = true)
    Double sumOfAmountByPymentMode (PaymentMode paymentMode);
}
