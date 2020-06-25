package com.app.converter.model;

import com.app.converter.model.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {
    private String model;
    private BigDecimal price;
    private Color color;
    private Double mileage;
    private List<String> components;

    @Override
    public String toString() {
        return model + " " + price + " " + color + " " + mileage + " " + components;
    }
}
