package com.chaudhary.chaudharycattle.service.farm;

import java.time.LocalDate;

public interface MilkService {
    void saveData (String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount);
}
