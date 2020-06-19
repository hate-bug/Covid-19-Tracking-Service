package com.Repository;

import com.Model.PatientEventAssociation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

public interface AssociationRepository extends CrudRepository<PatientEventAssociation, Long> {

}
