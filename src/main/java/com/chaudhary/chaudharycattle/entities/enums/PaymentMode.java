package com.chaudhary.chaudharycattle.entities.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum PaymentMode {
    PAYLATER(1),
    CASH(0);
    private int paymentMode;
    private PaymentMode (int paymentMode){
        this.paymentMode = paymentMode;
    }
}
