package com.drone.management.controllers;

import com.drone.management.requests.DroneRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DroneControllerTest {

    private static Validator validator = null;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    void validateDroneRequestTest() {

        DroneRequest request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(101), "IDLE");
        assertEquals("batteryCapacity value must be less than or equal 100", getErrorMessage(request));

        request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(-1), "IDLE");
        assertEquals("batteryCapacity value must be greater than 0.0", getErrorMessage(request));

        request = new DroneRequest("1234", "", new BigDecimal(10), new BigDecimal(100), "IDLE");
        assertEquals("model value should be value from LIGHT, MIDDLE, HEAVY, CRUISER", getErrorMessage(request));

        request = new DroneRequest("1234", "text", new BigDecimal(10), new BigDecimal(100), "IDLE");
        assertEquals("model value should be value from LIGHT, MIDDLE, HEAVY, CRUISER", getErrorMessage(request));

        request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(100), "");
        assertEquals("state value should be value from IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING", getErrorMessage(request));

        request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(100), "text");
        assertEquals("state value should be value from IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING", getErrorMessage(request));

        request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(100), "text");
        assertEquals("state field must be had a value", getErrorMessage(request));

        request = new DroneRequest("1234", "LIGHT", new BigDecimal(10), new BigDecimal(100), "");
        assertEquals("state value should be value from IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING", getErrorMessage(request));

    }

    private String getErrorMessage(DroneRequest request) {
        List<String> failures = new ArrayList<>();
        validator.validate(request).forEach(droneRequestConstraintViolation -> failures.add(droneRequestConstraintViolation.getMessageTemplate()));
        return failures.isEmpty() == true ? null : failures.get(0);
    }

    @Test
    void updateDrone() {
    }

    @Test
    void deleteDrone() {
    }

    @Test
    void getDrone() {
    }

    @Test
    void getAllDrones() {
    }

    @Test
    void getAvailableDrones() {
    }

    @Test
    void getBatteryLevel() {
    }
}