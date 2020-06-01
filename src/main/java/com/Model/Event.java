/**
 * Author: Zhe
 * Date: May30-2020
 * Event class is going to store the details of patient attended events
 */
package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long Id;
    private Date date;
    private String name;

    @OneToOne
    private Place place;

    @ManyToMany
    private Set<Patient> attendedPatients;

    @Autowired
    public Event(){
        this.attendedPatients = new HashSet<>();
        this.name = "unknown";
        this.place = new Place();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 01, 01);
        this.date = new Date (calendar.getTimeInMillis()); //By default set date to 1st/Jan/2000
    }

    public Event(String name, Place place, Date date) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.attendedPatients = new HashSet<>();
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

    public String getName (){
        return this.name;
    }

    public void setName (String name){
        this.name = name;
    }

    public void setDate (Date date){
        this.date = date;
    }

    public Date getDate () {
        return this.date;
    }

    public void addPatient(Patient patient) {
        this.attendedPatients.add(patient);
    }

    public Set<Patient> getAttendedPatients() {
        return attendedPatients;
    }
}
