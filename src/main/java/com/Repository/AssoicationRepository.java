package com.Repository;

import com.Model.PatientEventAssociation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AssoicationRepository extends CrudRepository<PatientEventAssociation, Long> {

    @Query("select PatientEventAssociation from PatientEventAssociation where PatientEventAssociation .isValid=?1")
    List<PatientEventAssociation> getAllByisValid (boolean isValid);
}
