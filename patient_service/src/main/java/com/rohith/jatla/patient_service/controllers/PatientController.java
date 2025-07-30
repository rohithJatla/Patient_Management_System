package com.rohith.jatla.patient_service.controllers;

import com.rohith.jatla.patient_service.dto.PatientRequestDTO;
import com.rohith.jatla.patient_service.dto.PatientResponseDTO;
import com.rohith.jatla.patient_service.service.PatientService;
import com.rohith.jatla.patient_service.validators.CreatePatientValidationGroup;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {
    /* @Autowired */
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientResponseDTO> findAll() {
        return patientService.findAll();
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            //Add default check for non specified fields + additional validation is done for specified fields
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO
    )
    {
        PatientResponseDTO patientResponseDTO = patientService.create(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class})  @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        PatientResponseDTO patientResponseDTO = patientService.update(id,patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
