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
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long Id;
    @ManyToMany
    private Set<Event> attendedEvents;

    //All patients are anonymous, name is not needed for constructor
    @Autowired
    public Patient(){
        this.attendedEvents = new HashSet<Event>();
    }

    public long getId(){
        return this.Id;
    }

    //Event list should not exist any redundant event, check ID before adding the event
    public void addAttendEvents(Event event){
        this.attendedEvents.add(event);
    }

    public Set<Event> getAllEvents(){
        return this.attendedEvents;
    }

}
