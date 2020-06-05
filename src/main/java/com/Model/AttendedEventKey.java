package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AttendedEventKey implements Serializable {

    @Column
    long eventId;

    @Column
    long patientId;

    @Autowired
    public AttendedEventKey () {}

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getPatientId(){
        return this.patientId;
    }
}
