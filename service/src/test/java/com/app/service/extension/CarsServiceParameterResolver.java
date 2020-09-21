package com.app.service.extension;

import com.app.service.CarsService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class CarsServiceParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(CarsService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String BASE_TEST_PATH = CarsServiceParameterResolver.class.getResource("").toString().split("target")[0];
        final String TEST_FILENAME_PATH = "src/test/resources/";
        final String FILENAME = "test-cars-1.json";
        final String FULL_PATH = BASE_TEST_PATH.substring(6) + TEST_FILENAME_PATH + FILENAME;
        return new CarsService(FULL_PATH);
    }
}
