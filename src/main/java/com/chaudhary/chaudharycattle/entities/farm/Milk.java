package com.chaudhary.chaudharycattle.entities.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
public class Milk implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Shift shift;
    private LocalDate createdDate;
    private Double liters;
    private Double fat;
    private Double rate;
    private Double amount;

    public Milk(Shift shift, LocalDate date, Double liters, Double fat, Double rate, Double amount) {
        this.shift = shift;
        this.createdDate = date;
        this.liters = liters;
        this.fat = fat;
        this.rate = rate;
        this.amount = amount;
    }
}
