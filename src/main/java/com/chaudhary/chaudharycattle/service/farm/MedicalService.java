package com.chaudhary.chaudharycattle.service.farm;

import java.time.LocalDate;

public interface MedicalService {
    boolean submit(String description, String supplier, double amount, LocalDate now, String paymentMode);
    boolean validSupplier (String name);
}
