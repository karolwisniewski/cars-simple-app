package com.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class CarStatistics {
    private Statistic mileageStatistic;
    private Statistic priceStatistic;

}
