package com.Repository;

import com.Model.Event;
import com.Model.PatientEventAssociation;
import com.Model.Place;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AssociationRepository extends PagingAndSortingRepository<PatientEventAssociation, Long> {

    @Query("SELECT DISTINCT p.event FROM PatientEventAssociation p " +
            "WHERE p.isValid=true")
    Iterable<Event> findAllValidEvents ();

    @Query("SELECT p.event.place FROM PatientEventAssociation p " +
            "WHERE p.isValid=true")
    Iterable<Place> findAllValidPlaces ();

    @Query("SELECT p.event.place FROM PatientEventAssociation p ")
    Iterable<Place> findAllPlaces ();

}
