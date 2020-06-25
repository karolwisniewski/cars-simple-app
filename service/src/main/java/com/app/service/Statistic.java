package com.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor

public class Statistic {
    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal avg;

    @Override
    public String toString() {
        return "MIN = " + min + " MAX = " + max + " AVG = " + avg;
    }
}
