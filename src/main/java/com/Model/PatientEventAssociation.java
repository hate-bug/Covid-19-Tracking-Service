package com.Model;

import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.*;

@Entity
public class PatientEventAssociation {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn
    Event event;

    @ManyToOne
    @JoinColumn
    Patient patient;

    boolean isValid;

    @Autowired
    public PatientEventAssociation(){
        this(new Event(), new Patient(), false);
    }

    public PatientEventAssociation(Event event, Patient patient, boolean isValid){
        this.event = event;
        this.patient = patient;
        this.isValid = isValid;
    }

    @Override
    public boolean equals (Object o){
        if (o instanceof PatientEventAssociation){
            PatientEventAssociation association = (PatientEventAssociation) o;
            if (association.patient.equals(this.patient) && association.event.equals(this.event) && association.isValid==this.isValid){
                return true;
            }
        }
        return false;
    }

}
