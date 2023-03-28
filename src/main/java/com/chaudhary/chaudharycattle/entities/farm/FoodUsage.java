package com.chaudhary.chaudharycattle.entities.farm;

import com.chaudhary.chaudharycattle.entities.enums.Shift;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FoodUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_food_id")
    private Food food;
    @Enumerated(EnumType.STRING)
    private Shift shift;
    private LocalDate createdDate;
    private Double qty;
    private Double rate;
    private Double amount;

    public FoodUsage (Food food, LocalDate date, Double qty, Shift shift,Double rate, Double amount){
        this.food = food;
        this.createdDate = date;
        this.qty = qty;
        this.shift = shift;
        this.rate = rate;
        this.amount = amount;
    }
}
