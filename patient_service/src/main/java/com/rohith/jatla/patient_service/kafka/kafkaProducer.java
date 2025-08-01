package com.rohith.jatla.patient_service.kafka;

import com.rohith.jatla.patient_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class kafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(kafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent patientEve = PatientEvent.newBuilder()
                .setPatientId(patient.getID().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();
        try{
            log.info("Sending Patient Event to topic: {}", patientEve);
            kafkaTemplate.send("patient",patientEve.toByteArray());

        }
        catch(Exception e){
            log.error("Error sending Patient Event to topic-> patient {} \n error : {} ", patientEve,e.getMessage());
        }
    }
}
