package com.Repository;

import com.Model.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    Optional<Patient> findBySessionId (String SessionId);
}
