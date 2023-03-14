package com.chaudhary.chaudharycattle.service.impl.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.chaudhary.chaudharycattle.entities.farm.Milk;
import com.chaudhary.chaudharycattle.repositories.farm.MilkRepository;
import com.chaudhary.chaudharycattle.service.farm.MilkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class MilkServiceImpl implements MilkService {

    @Autowired
    private MilkRepository milkRepository;

    @Transactional
    @Override
    public void saveData(String shift, LocalDate date, Double liters, Double fat, Double rate, Double amount) {
        milkRepository.save( new Milk(Shift.valueOf(shift), date, liters, fat, rate, amount) );
    }
}
