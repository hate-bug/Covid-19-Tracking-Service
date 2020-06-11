package com.WebController;

import com.Model.*;
import com.Repository.*;
import com.Service.UserSessionService;
import org.assertj.core.util.IterableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserSessionService userSessionService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    /**
     * Handler for patients creation.
     * User create a patient in front end and associate patient with multiple events.
     * Data posted with JSON string and parsed to Event objects directly.
     * Event duplication should be avoided.
     */
    @PostMapping(value = "/patientinfo", consumes = "application/json")
    public ResponseEntity<String> receivePatient (@RequestBody List<Event> events, HttpServletRequest request){
        Patient tempPatient = new Patient();//created patient
        List<Event> eventList;
        PatientEventAssociation association;
        //Assume registered users are valid here!
        boolean isValid = false;
        if (this.userSessionService.getUserFromRequest(request)!=null){
            User user = this.userSessionService.getUserFromRequest(request);
            isValid = true;
        }
        for (Event e: events){
            eventList = this.eventRepo.findAllByNameAndDate(e.getName(), e.getDate());
            int index = eventList.indexOf(e);
            if (index<0) {//new Event, save it to Repo
                association = new PatientEventAssociation(e, tempPatient, isValid);
                e.addAssociation(association);
                eventRepo.save(e);
            } else {//event already exist, just save association
                Event event = eventList.get(index);
                association = new PatientEventAssociation(event, tempPatient, isValid);
                event.addAssociation(association);
                eventRepo.save(event);
            }
        }
        return new ResponseEntity<>("saved", HttpStatus.OK);
    }

    /**
     * DoGet handler for all events.
     * User will get a list of all existing events with number of attended patients
     */
    @GetMapping(value = "/allEvents", produces = "application/json")
    @ResponseBody
    public Iterable<Event> getAllEvents (@RequestParam("page") int pageNum, ServletResponse response){
        Iterable<Event> events = this.eventRepo.findAll( PageRequest.of(pageNum-1, 20));
        Iterable<Event> nextPage = this.eventRepo.findAll( PageRequest.of(pageNum, 20));
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (IterableUtil.sizeOf(nextPage)>0){ //have next page
            httpServletResponse.addHeader("has-next-page", "true");
        } else {
            httpServletResponse.addHeader("has-next-page", "false");
        }
        List<Event> list=new ArrayList<>();
        events.forEach(list::add);
        return list;
    }


}
