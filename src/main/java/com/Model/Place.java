/**
 * Author: Zhe
 * Date: May30-2020
 * Place class is going to store the Geographical information of an event.
 */
package com.Model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Place {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private long id;
    private double longitude;
    private double latitude;
    private String name;

    @Autowired
    public Place(){
        this.name = "unknown";
        this.latitude = 0;
        this.latitude = 0;
    }

    public Place (String name, double longitude, double latitude){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName (){
        return this.name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setLongitude (double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude (double latitude) {
        this.latitude = latitude;
    }

    public Long getId (){
        return this.id;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }
}
