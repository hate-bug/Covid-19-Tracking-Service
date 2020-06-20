package com.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String applicantEmail;

    private String description;

    public Applicant(){
        this.applicantEmail = "unknown";
        this.description = "unknown";
    }

    public Applicant(String applicantEmail, String description){
        this.applicantEmail = applicantEmail;
        this.description = description;
    }
    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId () {
        return this.id;
    }

}
