package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Food;
import com.chaudhary.chaudharycattle.entities.farm.FoodUsage;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodUsageRepository extends JpaRepository<FoodUsage, Long> {

    Page<FoodUsage> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    Integer countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT new com.chaudhary.chaudharycattle.model.DashboardTableView( f.name, ROUND(SUM(fu.qty),2) ) FROM FoodUsage fu INNER JOIN Food f ON f.id = fu.fId WHERE fu.createdDate BETWEEN ?1 AND ?2 GROUP BY fu.fId")
    List<DashboardTableView> sumOfQtyByCreatedDateBetweenAndFk_food_idAndGroupBy (LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM FoodUsage WHERE createdDate BETWEEN ?1 AND ?2",nativeQuery = true)
    Double sumOfAmountByCreatedDateBetween (LocalDate startDate, LocalDate endDate);
    @Query(value = "SELECT * FROM FoodUsage WHERE createdDate BETWEEN ?1 AND ?2 AND shift LIKE ?3 AND fId LIKE ?4",nativeQuery = true)
    Page<FoodUsage> getFilterReport (LocalDate startDate, LocalDate endDate, String shift, String food, Pageable page);
    @Query(value = "SELECT IFNULL(ROUND(COUNT(*),2),0.0) FROM FoodUsage WHERE createdDate BETWEEN ?1 AND ?2 AND shift LIKE ?3 AND fId LIKE ?4",nativeQuery = true)
    long getFilterReportTotalCount (LocalDate startDate, LocalDate endDate, String shift, String food);
    @Query("SELECT new com.chaudhary.chaudharycattle.model.DashboardTableView( f.name, ROUND(SUM(fu.amount),2) ) FROM FoodUsage fu INNER JOIN Food f ON f.id = fu.fId WHERE fu.createdDate BETWEEN ?1 AND ?2 AND fu.shift IN (?3) GROUP BY fu.fId")
    List<DashboardTableView> getFilterFoodAmounts (LocalDate startDate, LocalDate endDate, List<Shift> shift);
    @Query("SELECT new com.chaudhary.chaudharycattle.model.DashboardTableView( f.name, ROUND(SUM(fu.amount),2) ) FROM FoodUsage fu INNER JOIN Food f ON f.id = fu.fId WHERE fu.createdDate BETWEEN ?1 AND ?2 AND fu.shift IN (?3) AND fu.fId = ?4 GROUP BY fu.fId")
    List<DashboardTableView> getFilterFoodAmounts (LocalDate startDate, LocalDate endDate, List<Shift> shift, Food food);
    @Query(value = "SELECT IFNULL( ROUND(SUM(fu.amount),2), 0.0 ) FROM FoodUsage fu INNER JOIN Food f ON f.id = fu.fId WHERE fu.createdDate BETWEEN ?1 AND ?2 AND fu.shift LIKE ?3 AND fu.fId LIKE ?4" , nativeQuery = true)
    double getFilterFoodsTotalAmount (LocalDate startDate, LocalDate endDate, String shift, String food);
}
