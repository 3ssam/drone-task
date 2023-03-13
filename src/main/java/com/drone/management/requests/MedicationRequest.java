package com.drone.management.requests;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel
public class MedicationRequest {

    @NotEmpty(message = "name field must be had a value")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "name field accept only letters, numbers, ‘-‘, ‘_’ ")
    private String name;

    @NotNull(message = "weight field must be had a value")
    @DecimalMin(value = "0.0", inclusive = false, message = "weight field must be greater than 0.0")
    @Digits(integer = 3, fraction = 3)
    private BigDecimal weight;

    @NotEmpty(message = "code field must not to be empty")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "code field accept only upper case letters, numbers, ‘_’ ")
    private String code;

}
