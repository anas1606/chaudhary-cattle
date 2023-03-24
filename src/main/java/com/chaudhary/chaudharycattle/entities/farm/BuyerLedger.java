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
    private Double rate;
    private Double qty;
    private Double amount;
    private LocalDate createdDate;
}
