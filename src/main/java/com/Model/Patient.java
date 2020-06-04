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

    private String attendedEventIds;

    //All patients are anonymous, name is not needed for constructor
    @Autowired
    public Patient(){
        this.attendedEventIds = "";
    }

    public long getId(){
        return this.Id;
    }

    //Event list should not exist any redundant event, check ID before adding the event
    public void addAttendEvents(Event event){
        this.attendedEventIds= this.attendedEventIds + event.getId()+",";
    }

    public Set<Integer> getAllEventIds(){
        HashSet<Integer> result = new HashSet<>();
        if (this.attendedEventIds.equals("")){
            return result;
        }
        String [] ids = this.attendedEventIds.split(",");
        for (int i=0; i<ids.length; i++){
            result.add(Integer.valueOf(ids[i]));
        }
        return result;
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
