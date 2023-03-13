package com.drone.management.service;

import com.drone.management.exceptions.FileStorageException;
import com.drone.management.models.Drone;
import com.drone.management.models.Medication;
import com.drone.management.projection.MedicationProjection;
import com.drone.management.repositories.MedicationRepository;
import com.drone.management.requests.LoadMedicationRequest;
import com.drone.management.requests.MedicationRequest;
import com.drone.management.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {
    private static Logger logger = LogManager.getLogger(MedicationServiceImpl.class);

    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private DroneService droneService;

    @Override
    @Transactional
    public MedicationProjection createMedication(MedicationRequest request, MultipartFile image) {
        Medication medication = new Medication();
        medication.setCode(request.getCode());
        medication.setName(request.getName());
        medication.setWeight(request.getWeight());
        medication = saveImage(medication, image);
        medicationRepository.save(medication);
        return medicationRepository.getMedicationById(medication.getId());
    }

    @Override
    @Transactional
    public MedicationProjection loadMedicationToDrone(Long id, LoadMedicationRequest request) throws Exception {
        Drone drone = droneService.getDroneOfMedication(request.getDroneId());
        Medication medication = medicationRepository.findById(id).get();
        if (validationService.isAvailableToLoad(drone, medication)) {
            medication.setDrone(drone);
            droneService.updateDroneWeightAfterLoad(drone, medication.getWeight());
            medicationRepository.save(medication);
        } else {
            throw new Exception("this weight is bigger than remain weight of drone");
        }
        return medicationRepository.getMedicationById(medication.getId());
    }

    @Override
    @Transactional
    public MedicationProjection updateMedication(MedicationRequest request, Long id) throws Exception {
        Medication medication = getMedicationIfExist(id);
        medication.setCode(request.getCode());
        medication.setName(request.getName());
        medication.setWeight(request.getWeight());
        medicationRepository.save(medication);
        return medicationRepository.getMedicationById(medication.getId());
    }

    @Override
    @Transactional
    public MedicationProjection updateImageOfMedication(Long id, MultipartFile image) throws Exception {
        Medication medication = getMedicationIfExist(id);
        medication = saveImage(medication, image);
        medicationRepository.save(medication);
        return medicationRepository.getMedicationById(medication.getId());
    }


    @Override
    public List<MedicationProjection> getAllMedications() {
        return medicationRepository.findAllBy();
    }

    @Override
    public MedicationProjection getMedication(Long id) throws Exception {
        return getMedicationProjectionIfExist(id);
    }

    @Override
    @Transactional
    public MedicationProjection deleteMedication(Long id) throws Exception {
        MedicationProjection medicationProjection = getMedicationProjectionIfExist(id);
        medicationRepository.deleteById(medicationProjection.getId());
        return medicationProjection;
    }

    @Override
    public String getImageOfMedication(Long id) throws Exception {
        return getMedicationIfExist(id).getImageSource();
    }

    private Medication saveImage(Medication medication, MultipartFile image) {
        if (!image.getOriginalFilename().isEmpty()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                medication.setImageName(fileName);
                medication.setImagetype(image.getContentType());
                medication.setImageByte(ImageUtil.compressBytes(image.getBytes()));
                medication.setImageSource(ImageUtil.convertToImage(medication.getImageByte()));
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return medication;
    }

    private Medication getMedicationIfExist(Long id) throws Exception {
        Medication medication = medicationRepository.findById(id).get();
        if (medication == null) {
            throw new Exception("Medication not found and this Id not exist");
        }
        return medication;
    }

    private MedicationProjection getMedicationProjectionIfExist(Long id) throws Exception {
        MedicationProjection medicationProjection = medicationRepository.getMedicationById(id);
        if (medicationProjection == null) {
            throw new Exception("Medication not found and this Id not exist");
        }
        return medicationProjection;
    }

    @Override
    public List<MedicationProjection> getMedicationsOfDrone(Long droneId) throws Exception {
        Drone drone = droneService.getDroneOfMedication(droneId);
        return medicationRepository.findAllByDrone(drone);
    }

}
