package com.Repository;

import com.Model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Integer> {

}
