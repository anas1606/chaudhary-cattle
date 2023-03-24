package com.chaudhary.chaudharycattle.entities.farm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

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
}
