package com.drone.management.service;

import com.drone.management.models.Drone;
import com.drone.management.models.enums.DroneState;
import com.drone.management.projection.DroneProjection;
import com.drone.management.repositories.DroneRepository;
import com.drone.management.requests.DroneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DroneServiceImpl implements DroneService {

    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public DroneProjection createDrone(DroneRequest request) throws Exception {
        Drone drone = new Drone();
        drone.setBatteryCapacity(request.getBatteryCapacity());
        drone.setModel(request.getModel());
        drone.setState(request.getState());
        if (drone.getState().equals(DroneState.LOADING)) {
            validationService.isBatteryLow(drone);
        }
        drone.setSerialNumber(request.getSerialNumber());
        drone.setWeightLimit(request.getWeightLimit());
        drone.setRemainWeight(request.getWeightLimit());
        drone = droneRepository.save(drone);
        return droneRepository.getDroneById(drone.getId());
    }

    @Override
    @Transactional
    public DroneProjection updateDrone(DroneRequest request, Long id) throws Exception {
        Drone drone = getDroneIfExist(id);
        drone.setBatteryCapacity(request.getBatteryCapacity());
        drone.setModel(request.getModel());
        drone.setState(request.getState());
        if (drone.getState().equals(DroneState.LOADING)) {
            validationService.isBatteryLow(drone);
        }
        drone.setSerialNumber(request.getSerialNumber());
        if (request.getWeightLimit().doubleValue() < (drone.getWeightLimit().doubleValue() - drone.getRemainWeight().doubleValue())) {
            throw new Exception("can't decrease Weight of drone because it carry Medications had Weight bigger than this new Weight");

        }
        drone.setRemainWeight(BigDecimal.valueOf(request.getWeightLimit().doubleValue() - (drone.getWeightLimit().doubleValue() - drone.getRemainWeight().doubleValue())));
        drone.setWeightLimit(request.getWeightLimit());
        drone = droneRepository.save(drone);
        return droneRepository.getDroneById(drone.getId());
    }

    @Override
    public List<DroneProjection> getAllDrones() {
        return droneRepository.findAllBy();
    }

    @Override
    public DroneProjection getDrone(Long id) throws Exception {
        return getDroneProjectionIfExist(id);
    }

    @Override
    public Drone getDroneOfMedication(Long id) throws Exception {
        return getDroneIfExist(id);
    }

    @Override
    @Transactional
    public DroneProjection deleteDrone(Long id) throws Exception {
        DroneProjection droneProjection = getDroneProjectionIfExist(id);
        droneRepository.deleteById(droneProjection.getId());
        return droneProjection;
    }

    @Override
    public List<DroneProjection> getAvailableDrones() {
        return droneRepository.findAllByRemainWeightGreaterThan(new BigDecimal(0.0));
    }

    @Override
    public BigDecimal getBatteryLevel(Long id) throws Exception {
        DroneProjection droneProjection = getDroneProjectionIfExist(id);
        return droneProjection.getBatteryCapacity();
    }

    @Override
    public Drone getDroneIfExist(Long id) throws Exception {
        Drone drone = droneRepository.findById(id).get();
        if (drone == null) {
            throw new Exception("Drone not found and this Id not exist");
        }
        return drone;
    }

    private DroneProjection getDroneProjectionIfExist(Long id) throws Exception {
        DroneProjection droneProjection = droneRepository.getDroneById(id);
        if (droneProjection == null) {
            throw new Exception("Drone not found and this Id not exist");
        }
        return droneProjection;
    }

    @Override
    public void updateDroneWeightAfterLoad(Drone drone, BigDecimal itemWeight) {
        drone.setRemainWeight(new BigDecimal(drone.getRemainWeight().doubleValue() - itemWeight.doubleValue()));
        droneRepository.save(drone);
    }
}
