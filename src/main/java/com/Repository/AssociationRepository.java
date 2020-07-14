package com.Repository;

import com.Model.PatientEventAssociation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AssociationRepository extends PagingAndSortingRepository<PatientEventAssociation, Long> {

    Iterable<PatientEventAssociation> findAllByisValid(boolean isValid);


}
