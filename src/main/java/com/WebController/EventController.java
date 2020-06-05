package com.WebController;

import com.Model.Patient;
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
        for (Event e: events){
            int index = eventList.indexOf(e);
            if (index>=0){//such event already exists in the repository, just add patient to the event
                eventList.get(index).addPatient(tempPatient);
                this.eventRepo.save(eventList.get(index));
            } else {//new event, add such event to the repository
                e.addPatient(tempPatient);
                this.eventRepo.save(e);
            }
            tempPatient.addAttendEvents(e);
        }
        this.patientRepo.save(tempPatient);
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
