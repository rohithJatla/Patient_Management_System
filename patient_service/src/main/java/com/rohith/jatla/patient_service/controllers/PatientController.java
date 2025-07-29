package com.rohith.jatla.patient_service.controllers;

import com.rohith.jatla.patient_service.dto.PatientRequestDTO;
import com.rohith.jatla.patient_service.dto.PatientResponseDTO;
import com.rohith.jatla.patient_service.repository.PatientRepository;
import com.rohith.jatla.patient_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    // @Autowired
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientResponseDTO> findAll() {
        return patientService.findAll();
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid  @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.create(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }
}
