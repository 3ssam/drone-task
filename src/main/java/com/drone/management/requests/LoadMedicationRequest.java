package com.drone.management.requests;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class LoadMedicationRequest {


    @NotNull(message = "droneId must be had a value")
    private Long droneId;

}
