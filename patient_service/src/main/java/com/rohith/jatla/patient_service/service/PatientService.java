package com.rohith.jatla.patient_service.service;

import com.rohith.jatla.patient_service.dto.PatientRequestDTO;
import com.rohith.jatla.patient_service.dto.PatientResponseDTO;
import com.rohith.jatla.patient_service.exceptions.EmailAlreadyExistsException;
import com.rohith.jatla.patient_service.exceptions.PatientNotFoundException;
import com.rohith.jatla.patient_service.grpc.BillingServiceGrpcClient;
import com.rohith.jatla.patient_service.mapper.PatientMapper;
import com.rohith.jatla.patient_service.model.Patient;
import com.rohith.jatla.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {
//  Can use auto wired insted of constructor injection
//  @Autowired
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public Patient findById(UUID id) {
        return patientRepository.findById(id).get();
    }

    public List<PatientResponseDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs;
//        for (Patient patient : patients) {
//            patientResponseDTOs.add(PatientMapper.toPatientResponseDTO(patient));
//        }
        patientResponseDTOs = patients.stream().map(PatientMapper::toPatientResponseDTO).collect(Collectors.toList());
        return patientResponseDTOs;
    }

    public PatientResponseDTO create(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("A patient with this email already exists"+ patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toPatientModel(patientRequestDTO));

        billingServiceGrpcClient.createBillingAccount(
                patient.getID().toString(),
                patient.getName(),
                patient.getEmail()
        );
        return PatientMapper.toPatientResponseDTO(patient);
    }

    public PatientResponseDTO update(UUID id, PatientRequestDTO patientRequestDTO) {
        // Get patient or throw exception
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id " + id));

        // Only check email if it's changing
        if (!patient.getEmail().equals(patientRequestDTO.getEmail()) &&
                patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists: " + patientRequestDTO.getEmail());
        }

        // Update fields
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        // Save and return
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDTO(savedPatient);

    }

    public void delete(UUID id) {
        // Maintain Idempotent Delete of REST APIS the goal is
        // archived if the patient is not present in the database
       patientRepository.deleteById(id);
    }


}
