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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    private String sessionId;

    @OneToMany (cascade = CascadeType.ALL)
    private Set <PatientEventAssociation> patientEventAssociations;

    //All patients are anonymous, name is not needed for constructor
    @Autowired
    public Patient(){
        this.sessionId = UUID.randomUUID().toString();
        this.patientEventAssociations = new HashSet<>();
    }

    public Patient (String SessionId) {
        this.sessionId = SessionId;
        this.patientEventAssociations = new HashSet<>();
    }

    public String getSessionId(){
        return this.sessionId;
    }

    //Event list should not exist any redundant event, check ID before adding the event
    public void addAssociation (PatientEventAssociation event){
        this.patientEventAssociations.add(event);
    }

    public Set<PatientEventAssociation> getPatientEventAssication (){
        return this.patientEventAssociations;
    }

    /**
     *Return true if two patients have the same session Id
     */
    @Override
    public boolean equals (Object o){
        if (o instanceof Patient){
            Patient p = (Patient) o;
            if (p.sessionId == this.sessionId){
                return true;
            }
        }
        return false;
    }

}
