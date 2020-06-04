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
    private String address;

    @Autowired
    public Place(){
        this("unknown", 0, 0);
    }

    public Place (String address, double longitude, double latitude){
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    /**
     * Return true if two Places have the same latitude, longitude and address.
     */
    @Override
    public boolean equals (Object o){
        if (o instanceof Place){
            Place p = (Place) o;
            if ((p.getLatitude() == this.latitude)&&(p.getLongitude()==this.longitude)&&p.getAddress().equals(this.address)){
                return true;
            }
        }
        return false;
    }
}
