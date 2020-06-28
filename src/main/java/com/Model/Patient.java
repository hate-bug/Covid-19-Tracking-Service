/**
 * Author: Zhe
 * Date: May 31th-2020
 * Patient class is used to store basic patient information including ID and attended events
 */
package com.Model;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.*;
import java.util.*;

@Entity
public class Patient {

    @Id
    private String Id;

    @OneToMany (cascade = CascadeType.ALL)
    private Set <PatientEventAssociation> patientEventAssociations;

    //All patients are anonymous, name is not needed for constructor
    @Autowired
    public Patient(){
        this.Id = UUID.randomUUID().toString();
        this.patientEventAssociations = new HashSet<>();
    }

    public Patient (String Id) {
        this.Id = Id;
        this.patientEventAssociations = new HashSet<>();
    }

    public String getId(){
        return this.Id;
    }

    //Event list should not exist any redundant event, check ID before adding the event
    public void addAssociation (PatientEventAssociation event){
        this.patientEventAssociations.add(event);
    }

    public Set<PatientEventAssociation> getPatientEventAssication (){
        return this.patientEventAssociations;
    }

    /**
     *Return true if two patients have the same Id
     */
    @Override
    public boolean equals (Object o){
        if (o instanceof Patient){
            Patient p = (Patient) o;
            if (p.Id == this.Id){
                return true;
            }
        }
        return false;
    }

}
