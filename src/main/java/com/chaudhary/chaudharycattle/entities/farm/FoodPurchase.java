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
    private int fpId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fId")
    private Food fId;
    private Double rate;
    private Double qty;
    private Double rQty;
    private LocalDate createdDate;

    public FoodPurchase(Food fId, Double rate, Double qty, Double rQty, LocalDate createdDate) {
        this.fId = fId;
        this.rate = rate;
        this.qty = qty;
        this.rQty = rQty;
        this.createdDate = createdDate;
    }
}
