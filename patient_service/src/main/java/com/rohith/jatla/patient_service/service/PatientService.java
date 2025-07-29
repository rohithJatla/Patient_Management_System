package com.rohith.jatla.patient_service.service;

import com.rohith.jatla.patient_service.dto.PatientRequestDTO;
import com.rohith.jatla.patient_service.dto.PatientResponseDTO;
import com.rohith.jatla.patient_service.mapper.PatientMapper;
import com.rohith.jatla.patient_service.model.Patient;
import com.rohith.jatla.patient_service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {
//  Can use auto wired insted of constructor injection
//  @Autowired
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
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
        Patient patient = patientRepository.save(PatientMapper.toPatientModel(patientRequestDTO));
        return PatientMapper.toPatientResponseDTO(patient);
    }

}
