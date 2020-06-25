package com.app.service.validator.generic;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data

public abstract class AbstractValidator<T> implements Validator<T> {
    protected Map<String, String> errors = new HashMap<>();
}
