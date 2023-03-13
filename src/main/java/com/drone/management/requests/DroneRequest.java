package com.drone.management.requests;

import com.drone.management.models.enums.DroneModel;
import com.drone.management.models.enums.DroneState;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class DroneRequest {
    @Length(max = 100, message = "maximum length for serialNumber field is 100 character")
    @NotEmpty(message = "serialNumber field must not to be empty")
    private String serialNumber;


    @NotEmpty(message = "model field must be had a value")
    @Pattern(regexp = "LIGHT_WEIGHT|MIDDLE_WEIGHT|HEAVY_WEIGHT|CRUISER_WEIGHT", message = "model value should be value from LIGHT_WEIGHT, MIDDLE_WEIGHT, HEAVY_WEIGHT, CRUISER_WEIGHT")
    private String model;


    @NotNull(message = "weightLimit field must be had a value")
    @DecimalMin(value = "0", inclusive = false, message = "weightLimit value must be greater than 0.0")
    @DecimalMax(value = "501", inclusive = false, message = "weightLimit value must be less than or equal 500")
    @Digits(integer = 3, fraction = 3)
    private BigDecimal weightLimit;


    @NotNull(message = "batteryCapacity field must be had a value")
    @DecimalMin(value = "0", inclusive = false, message = "batteryCapacity value must be greater than 0.0")
    @DecimalMax(value = "101", inclusive = false, message = "batteryCapacity value must be less than or equal 100")
    @Digits(integer = 3, fraction = 3)
    private BigDecimal batteryCapacity;

    @NotEmpty(message = "state field must be had a value")
    @Pattern(regexp = "IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING", message = "state value should be value from IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING")
    private String state;

    public DroneModel getModel() {
        return DroneModel.valueOf(model);
    }

    public DroneState getState() {
        return DroneState.valueOf(state);
    }
}
