package com.chaudhary.chaudharycattle.repositories.farm;

import com.chaudhary.chaudharycattle.entities.farm.BuyerLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BuyerLedgerRepository extends JpaRepository<BuyerLedger,Integer> {
    Page<BuyerLedger> findAllByCreatedDateBetween (LocalDate startDate, LocalDate endDate, Pageable page);
    Integer countOfIdByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
}
