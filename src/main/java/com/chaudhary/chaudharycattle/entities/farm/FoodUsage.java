package com.chaudhary.chaudharycattle.entities.farm;

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
    private LocalDate createdDate;
    private Double qty;

    public FoodUsage (Food food, LocalDate date, Double qty){
        this.food = food;
        this.createdDate = date;
        this.qty = qty;
    }
}
