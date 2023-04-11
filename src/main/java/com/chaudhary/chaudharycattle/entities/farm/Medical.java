package com.chaudhary.chaudharycattle.entities.farm;

import com.chaudhary.chaudharycattle.entities.enums.PaymentMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Medical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bId")
    private Supplier bId;
    private String description;
    private LocalDate createdDate;
    private Double amount;
    @Enumerated(EnumType.ORDINAL)
    private PaymentMode paymentMode;

    public Medical (String description, Supplier supplier, LocalDate createdDate, Double amount, PaymentMode paymentMode){
        this.description = description;
        this.bId = supplier;
        this.createdDate = createdDate;
        this.amount = amount;
        this.paymentMode = paymentMode;
    }
}
