package com.CRUD;

import com.Model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends CrudRepository<Patient, Integer> {

}
