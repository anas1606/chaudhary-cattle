package com.chaudhary.chaudharycattle.entities.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Shift {
    MORNING("Morning"),
    EVENING("Evening");

    private String shift;

    private Shift (String shift){
        this.shift = shift;
    }
}
