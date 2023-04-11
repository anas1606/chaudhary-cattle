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
public class FoodPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fId")
    private Food fId;
    private Double rate;
    private Double qty;
    private Double rQty;
    private Double amount;
    private LocalDate createdDate;

    public FoodPurchase(Food fId, Double rate, Double qty, Double rQty, Double amount, LocalDate createdDate) {
        this.fId = fId;
        this.rate = rate;
        this.qty = qty;
        this.rQty = rQty;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
