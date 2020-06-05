package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
public class AttendedEvent {

    @EmbeddedId
    private AttendedEventKey key;

    @ManyToOne
    Event event;

    @ManyToOne
    Patient patient;

    boolean isValid;

    @Autowired
    public AttendedEvent(){
        this(new Event(), new Patient(), false);
    }

    public AttendedEvent(Event event, Patient patient, boolean isValid){
        this.event = event;
        this.patient = patient;
        this.isValid = isValid;
    }

}
