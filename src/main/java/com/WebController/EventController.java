package com.WebController;

import com.Model.*;
import com.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserRepository userRepo;

    /**
     * Handler for patients creation.
     * User create a patient in front end and associate patient with multiple events.
     * Data posted with JSON string and parsed to Event objects directly.
     * Event duplication should be avoided.
     */
    @PostMapping(value = "/patientinfo", consumes = "application/json")
    public List<Event> receivePatient (@RequestBody List<Event> events, Principal principal){
        Patient tempPatient = new Patient();//created patient
        List<Event> eventList, returnList;
        returnList = new ArrayList<>();
        PatientEventAssociation association;
        boolean verified =false;
        if (principal!=null){ // check if its a verified user
            verified = true;
        }
        for (Event e: events){
            if (e.getDate()==null || e.getName().equals("") || e.getPlace().getAddress().equals("") || e.getPlace().getLongitude()==0.0 || e.getPlace().getLatitude()==0.0){
                continue;
            }
            eventList = this.eventRepo.findAllByNameAndDate(e.getName(), e.getDate());
            int index = eventList.indexOf(e);
            if (index<0) {//new Event, save it to Repo
                association = new PatientEventAssociation(e, tempPatient, verified);
                e.addAssociation(association);
                returnList.add(eventRepo.save(e));
            } else {//event already exist, just save association
                Event event = eventList.get(index);
                association = new PatientEventAssociation(event, tempPatient, verified);
                event.addAssociation(association);
                returnList.add(eventRepo.save(event));
            }
        }
        return returnList;
    }

    /**
     * DoGet handler for all events.
     * User will get a list of all existing events with number of attended patients
     */
    @GetMapping(value = "/allEvents")
    public Iterable<Event> getAllEvents (@RequestParam("page") int pageNum){
        Page<Event> events = this.eventRepo.findAll( PageRequest.of(pageNum-1, 10));
        return events;
    }

}
