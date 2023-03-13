package com.drone.management.service;

import com.drone.management.models.Drone;
import com.drone.management.models.Medication;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public Boolean isBatteryLow(Drone drone) throws Exception {
        if (drone.getBatteryCapacity().doubleValue() < 25.0) {
            throw new Exception("can't loading this drone because its battery blow 25%");
        }
        return false;
    }

    public Boolean isAvailableToLoad(Drone drone, Medication medication) {
        return medication.getWeight().doubleValue() <= drone.getRemainWeight().doubleValue();
    }

}
