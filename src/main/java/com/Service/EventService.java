package com.Service;

import com.Model.Event;
import com.Model.Patient;
import com.Model.PatientEventAssociation;
import com.Repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;

    public void saveEvents (List<Event> events) throws Exception{
        Patient tempPatient = new Patient();//created patient
        List<Event> eventList = new ArrayList<>();
        PatientEventAssociation association;
        for (Event e: events){
            eventList = this.eventRepo.findAllByNameAndDate(e.getName(), e.getDate());
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
    }
}
