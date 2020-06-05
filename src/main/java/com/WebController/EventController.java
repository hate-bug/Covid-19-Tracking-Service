package com.WebController;

import com.Model.Patient;
import com.Model.PatientEventAssociation;
import com.Repository.AssociationRepository;
import com.Repository.EventRepository;
import com.Repository.PatientRepository;
import com.Model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private AssociationRepository assoicationRepo;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @PostMapping(value = "/patientinfo", consumes = "application/json")
    @ResponseBody
    public String receivePatient (@RequestBody List<Event> events){
        Patient tempPatient = new Patient();
        List<Event> eventList = new ArrayList<>();
        Iterable<Event> allEvents = this.eventRepo.findAll();
        allEvents.forEach(eventList::add);
        PatientEventAssociation association;
        for (Event e: events){
            int index = eventList.indexOf(e);
            if (index<0) {//new Event, save it to Repo
                association = new PatientEventAssociation(e, tempPatient, true);
                e.addAssociation(association);
                eventRepo.save(e);
            } else {//event already exist, just save association
                Event event = eventList.get(index);
                association = new PatientEventAssociation(event, tempPatient, true);
                event.addAssociation(association);
                eventRepo.save(event);
            }
        }
        return "success";
    }

    @GetMapping(value = "/allEvents", produces = "application/json")
    @ResponseBody
    public List<Event> getAllEvents (){
        List<Event> list = new ArrayList<>();
        Iterable<Event> events = this.eventRepo.findAll();
        events.forEach(list::add);
        return list;
    }

}
