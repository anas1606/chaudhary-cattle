package com.chaudhary.chaudharycattle.entities.farm;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
public class BuyerLedger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bId")
    private Buyer bId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fId")
    private Food fId;
    private Double rate;
    private Double qty;
    private Double amount;
    private LocalDate createdDate;

    public BuyerLedger(Buyer bId, Food fId, Double rate, Double qty, Double amount, LocalDate createdDate) {
        this.bId = bId;
        this.fId = fId;
        this.rate = rate;
        this.qty = qty;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
