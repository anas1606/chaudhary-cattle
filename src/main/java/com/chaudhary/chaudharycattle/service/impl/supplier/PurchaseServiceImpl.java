package com.chaudhary.chaudharycattle.service.impl.supplier;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierLedger;
import com.chaudhary.chaudharycattle.model.farm.FoodPurchaseTableView;
import com.chaudhary.chaudharycattle.repositories.SupplierLedgerRepository;
import com.chaudhary.chaudharycattle.repositories.SupplierRepository;
import com.chaudhary.chaudharycattle.service.supplier.PurchaseService;
import com.chaudhary.chaudharycattle.utils.CommanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private SupplierLedgerRepository supplierLedgerRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<FoodPurchaseTableView> getFilteredPurchaseRecords(LocalDate startDate, LocalDate endDate, String supplier, int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<SupplierLedger> supplierLedgers = new ArrayList<>();
        if(supplier.equalsIgnoreCase("")) {
            supplierLedgers = supplierLedgerRepository.findAllByCreatedDateBetween(startDate, endDate, page).toList();
        }else {
            Supplier supplierobj = supplierRepository.findByName(supplier);
            if (supplierobj!=null)
                supplierLedgers = supplierLedgerRepository.findAllByCreatedDateBetweenAndBId(startDate, endDate, supplierobj,page).toList();
            else {
                CommanUtils.warningAlert("Warning","Please Check The Supplier Name");
                return new ArrayList<>();
            }
        }
        return supplierLedgers.stream().map(FoodPurchaseTableView::new).collect(Collectors.toList());
    }

    @Override
    public double getFilteredPurchaseRecordsTotal(LocalDate startDate, LocalDate endDate, String supplier) {
        if(supplier.equalsIgnoreCase("")) {
             return supplierLedgerRepository.sumAmountByCreatedDateBetween(startDate, endDate);
        }else {
            Supplier supplierobj = supplierRepository.findByName(supplier);
            if(supplierobj!=null)
                return supplierLedgerRepository.sumAmountByCreatedDateBetweenAndBId(startDate, endDate, supplierRepository.findByName(supplier));
            else {
                CommanUtils.warningAlert("Warning","Please Check The Supplier Name");
                return 0;
            }
        }
    }

    @Override
    public long getTableDataCount(LocalDate startDate, LocalDate endDate) {
        return supplierLedgerRepository.countOfIdByCreatedDateBetween(startDate,endDate);
    }
}
