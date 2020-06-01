/**
 * Author: Zhe
 * Date: May30-2020
 * Event class is going to store the details of patient attended events
 */
package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE)
    private long Id;
    private Date date;
    private String name;

    @OneToOne
    private Place place;

    @ManyToMany
    private List<Patient> attendedPatients;

    @Autowired
    public Event(){}

    public Event(String name, Place place, Date date) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.attendedPatients = new ArrayList<>();
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return this.place;
    }

    public long getId() {
        return this.Id;
    }

    public int getAttendedPatientNum() {
        return this.attendedPatients.size();
    }

    public void setId (long Id){
        this.Id = Id;
    }

    public String getName (){
        return this.name;
    }

    //Patient list should not have any redundant patient, check ID before adding.
    public void addPatient(Patient patient) {
        for (Patient p : this.attendedPatients) {
            if (p.getId() == patient.getId()) {
                return;
            }
        }
        this.attendedPatients.add(patient);
    }
}
