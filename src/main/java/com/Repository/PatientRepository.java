package com.Repository;

import com.Model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "patient", path = "patient")
public interface PatientRepository extends CrudRepository<Patient, String> {

}
