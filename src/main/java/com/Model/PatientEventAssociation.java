package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
public class PatientEventAssociation {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn
    Event event;

    @ManyToOne (cascade = CascadeType.ALL)
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

}
