package com.chaudhary.chaudharycattle.service.impl.supplier;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierRepayment;
import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;
import com.chaudhary.chaudharycattle.repositories.SupplierRepaymentRepository;
import com.chaudhary.chaudharycattle.repositories.SupplierRepository;
import com.chaudhary.chaudharycattle.service.supplier.RepaymentStatementService;
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
public class RepaymentStatementServiceImpl implements RepaymentStatementService {
    @Autowired
    private SupplierRepaymentRepository supplierRepaymentRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Override
    public List<RepaymentStatementTableView> getRepaymentStatement(LocalDate startDate, LocalDate endDate, String supplier, int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<SupplierRepayment> foodPurchases = new ArrayList<>();
        if(supplier.equalsIgnoreCase("")) {
            foodPurchases = supplierRepaymentRepository.findAllByCreatedDateBetween(startDate, endDate, page).toList();
        }else {
            Supplier supplierobj = supplierRepository.findByName(supplier);
            if (supplierobj!=null)
                foodPurchases = supplierRepaymentRepository.findAllByCreatedDateBetweenAndBId(startDate, endDate, supplierobj,page).toList();
            else {
                CommanUtils.warningAlert("Warning","Please Check The Supplier Name");
                return new ArrayList<>();
            }
        }
        return foodPurchases.stream().map(RepaymentStatementTableView::new).collect(Collectors.toList());
    }
    @Override
    public long getTableDataCount(LocalDate startDate, LocalDate endDate) {
        return supplierRepaymentRepository.countOfIdByCreatedDateBetween(startDate,endDate);
    }

    @Override
    public double getRepaymentStatementTotal(LocalDate startDate, LocalDate endDate, String supplier) {
        if(supplier.equalsIgnoreCase("")) {
            return supplierRepaymentRepository.sumAmountByCreatedDateBetween(startDate, endDate);
        }else {
            Supplier supplierobj = supplierRepository.findByName(supplier);
            if(supplierobj!=null)
                return supplierRepaymentRepository.sumAmountByCreatedDateBetweenAndBId(startDate, endDate, supplierRepository.findByName(supplier));
            else {
                CommanUtils.warningAlert("Warning","Please Check The Supplier Name");
                return 0;
            }
        }
    }
}
