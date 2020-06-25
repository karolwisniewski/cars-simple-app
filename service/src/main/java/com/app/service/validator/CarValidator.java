package com.app.service.validator;

import com.app.converter.model.Car;
import com.app.service.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CarValidator extends AbstractValidator<Car> {

    @Override
    public Map<String, String> validate(Car car) {
        if (car == null) {
            errors.put("object", "is null");
            return errors;
        }

        String modelValidationMessage = validateModel(car.getModel());
        if (!modelValidationMessage.isEmpty()) {
            errors.put("model", modelValidationMessage);
        }

        String priceValidatorMessage = validatePrice(car.getPrice());
        if (!priceValidatorMessage.isEmpty()) {
            errors.put("price", priceValidatorMessage);
        }

        String mileageValidatorMessage = validateMileage(car.getMileage());
        if (!mileageValidatorMessage.isEmpty()) {
            errors.put("mileage", mileageValidatorMessage);
        }

        String componentsValidatorMessage = validateComponents(car.getComponents());
        if (!componentsValidatorMessage.isEmpty()) {
            errors.put("components", componentsValidatorMessage);
        }

        return errors;
    }

    private String validateModel(String model) {
        if (model == null) {
            return "string is null";
        }

        final String REGEX = "([A-Z]+\\s?)+";
        if (!model.matches(REGEX)) {
            return "model value doesn't match regular expression";
        }
        return "";
    }

    private String validatePrice(BigDecimal price) {
        if (price == null) {
            return "object is null";
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            return "price value is lower than zero";
        }
        return "";
    }

    private String validateMileage(Double mileage) {
        if (mileage <= 0) {
            return "Less or equal than zero";
        }
        return "";
    }

    private String validateComponents(List<String> components) {
        if (components == null) {
            return "Components list is null!";
        }
        for (String component : components) {
            if (!component.matches("([A-Z]+\\s?)+")) {
                return "component " + component + " doesn't match regular expression";
            }
        }
        return "";
    }
}
