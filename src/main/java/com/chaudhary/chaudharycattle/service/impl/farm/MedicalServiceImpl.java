package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.chaudhary.chaudharycattle.entities.farm.Medical;
import com.chaudhary.chaudharycattle.entities.farm.Supplier;
import com.chaudhary.chaudharycattle.repositories.farm.MedicalRepository;
import com.chaudhary.chaudharycattle.repositories.farm.SupplierRepository;
import com.chaudhary.chaudharycattle.service.farm.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class MedicalServiceImpl implements MedicalService {
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private MedicalRepository medicalRepository;
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
}
