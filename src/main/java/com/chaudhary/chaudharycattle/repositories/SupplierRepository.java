package com.chaudhary.chaudharycattle.repositories;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findByName (String name);
    @Query(value = "SELECT IFNULL(ROUND(SUM(amount),2),0.0) FROM Supplier WHERE 1=1",nativeQuery = true)
    Double sumOfAmount ();
}
