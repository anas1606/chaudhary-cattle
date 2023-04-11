package com.chaudhary.chaudharycattle.service.impl.supplier;

import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.entities.farm.SupplierRepayment;
import com.chaudhary.chaudharycattle.model.supplier.RepaymentStatementTableView;
import com.chaudhary.chaudharycattle.repositories.SupplierRepaymentRepository;
import com.chaudhary.chaudharycattle.repositories.SupplierRepository;
import com.chaudhary.chaudharycattle.service.supplier.SupplierRepaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierRepaymentServiceImpl implements SupplierRepaymentService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierRepaymentRepository supplierRepaymentRepository;
    private static final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    private static final LocalDate endDate = startDate.plusMonths(1).minusDays(1);

    @Transactional
    @Override
    public void submit(String supplier, LocalDate date, double amount) {
        Supplier supplier1 = supplierRepository.findByName(supplier);
        if(supplier1!=null){
            SupplierRepayment model = new SupplierRepayment(supplier1, amount, date);
            supplierRepaymentRepository.save(model);
            supplier1.setAmount( supplier1.getAmount() - amount );
            supplierRepository.save(supplier1);
        }
    }
    @Override
    public List<RepaymentStatementTableView> getSupplierRepayments(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo, maxSize, Sort.Direction.DESC, "createdDate");
        List<SupplierRepayment> foodPurchases = supplierRepaymentRepository.findAllByCreatedDateBetween (startDate, endDate, page).toList();
        return foodPurchases.stream().map(RepaymentStatementTableView::new).collect(Collectors.toList());
    }
    @Override
    public long getTableDataCount() {
        return supplierRepaymentRepository.countOfIdByCreatedDateBetween(startDate, endDate);
    }
    @Override
    public double getRepaymentTotal() {
        return supplierRepaymentRepository.sumAmountByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public double getSupplieroldBalance(String supplier) {
        Supplier supplier1 = supplierRepository.findByName(supplier);
        if (supplier1 != null){
            return supplier1.getAmount();
        }else {
            return -1;
        }
    }
}
