package com.chaudhary.chaudharycattle.entities.farm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bId;
    private String name;
    private String contact;
    private Double amount;

    public Supplier(String name, String contact, Double amount) {
        this.name = name;
        this.contact = contact;
        this.amount = amount;
    }
}
