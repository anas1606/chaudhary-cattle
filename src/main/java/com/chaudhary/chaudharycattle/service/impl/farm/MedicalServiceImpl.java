package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.chaudhary.chaudharycattle.entities.farm.Medical;
import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.model.DashboardTableView;
import com.chaudhary.chaudharycattle.model.farm.MedicalTableView;
import com.chaudhary.chaudharycattle.repositories.farm.MedicalRepository;
import com.chaudhary.chaudharycattle.repositories.farm.SupplierRepository;
import com.chaudhary.chaudharycattle.service.farm.MedicalService;
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
public class MedicalServiceImpl implements MedicalService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MedicalRepository medicalRepository;
    private static final LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    private static final LocalDate endDate = startDate.plusMonths(1).minusDays(1);
    @Transactional
    @Override
    public boolean submit(String description, String supplierName, double amount, LocalDate createdDate, String paymentMode) {
        Supplier supplier = supplierRepository.findByName(supplierName);
        if(supplier != null) {
//            paymentMode = (paymentMode.equalsIgnoreCase("Pay Later")) ? "PAYLATER" : "CASH";
            Medical medical = new Medical(description, supplier, createdDate, amount, PaymentMode.valueOf(paymentMode));
            medicalRepository.save(medical);
            if(PaymentMode.PAYLATER == PaymentMode.valueOf(paymentMode)) {
                supplier.setAmount(supplier.getAmount()+amount);
                supplierRepository.save(supplier);
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean validSupplier(String name) {
        return supplierRepository.findByName(name)!=null;
    }

    @Override
    public List<MedicalTableView> getTableData(int pageNo, int maxSize) {
        Pageable page = PageRequest.of(pageNo,maxSize, Sort.Direction.DESC, "createdDate");
        List<Medical> data = medicalRepository.findAllByCreatedDateBetween(startDate, endDate, page).toList();
        return data.stream().map(MedicalTableView::new).collect(Collectors.toList());
    }
    @Override
    public long getTableDataCount() {
        Long count =  medicalRepository.countOfIdByCreatedDateBetween(startDate, endDate);
        return count != null ? count : 0;
    }
    @Override
    public List<DashboardTableView> getRecTableData() {
        return medicalRepository.sumOfAmountBYSupplierAndCreatedDateAndGroupBy(startDate, endDate);
    }
}
