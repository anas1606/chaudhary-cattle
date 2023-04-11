package com.chaudhary.chaudharycattle.entities.farm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bId")
    private Supplier bId;
    private Double amount;
    private LocalDate createdDate;

    public SupplierRepayment(Supplier bId, Double amount, LocalDate createdDate) {
        this.bId = bId;
        this.amount = amount;
        this.createdDate = createdDate;
    }
}
