/**
 * Author: Zhe
 * Date: May30-2020
 * Event class is going to store the details of patient attended events
 */
package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.*;

@Entity
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long Id;
    private Date date;
    private String name;

    @OneToOne (cascade = CascadeType.ALL)
    private Place place;

    @OneToMany (cascade = CascadeType.ALL)
    private Set<PatientEventAssociation> patientEventAssociations;

    //Default constructor set the time to current date
    @Autowired
    public Event(){
        this("unknown", new Place(), Calendar.getInstance().getTime());
    }

    @Autowired
    public Event(String name, Place place, Date date) {
        this.name = name;
        this.place = place;
        this.date = date;
        this.patientEventAssociations = new HashSet<>();
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

    public void addAssociation(PatientEventAssociation patient) {
        this.patientEventAssociations.add(patient);
    }

    public Set<PatientEventAssociation> getPatientEventAssociations() {
        return patientEventAssociations;
    }

    /**
     * Return true if two events have the same date, Place and name.
     */
    @Override
    public boolean equals (Object o){
        if (o instanceof Event){
            if ((((Event)o).getName().equals(this.name)) && ((((Event)o).getDate()).compareTo(this.date)==0) && ((Event)o).getPlace().equals(this.place)){
                return true;
            }
        }
        return false;
    }

}
