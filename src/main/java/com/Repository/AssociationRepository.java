package com.Repository;

import com.Model.Event;
import com.Model.PatientEventAssociation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AssociationRepository extends CrudRepository<PatientEventAssociation, Long> {

    @Query("SELECT DISTINCT p.event FROM PatientEventAssociation p " +
            "WHERE p.isValid=true")
    Page<Event> findAllValidEvents (PageRequest pageNum);

}
