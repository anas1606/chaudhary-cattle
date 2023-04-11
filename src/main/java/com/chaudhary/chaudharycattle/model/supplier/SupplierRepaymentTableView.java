package com.chaudhary.chaudharycattle.model.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRepaymentTableView {
    private String supplier;
    private LocalDate date;
    private String amount;
}
