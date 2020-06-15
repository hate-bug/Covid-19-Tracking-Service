package com.WebController;

import com.Model.*;
import com.Repository.*;
import com.Service.*;
import org.assertj.core.util.IterableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private EventService eventService;

    /**
     * Handler for patients creation.
     * User create a patient in front end and associate patient with multiple events.
     * Data posted with JSON string and parsed to Event objects directly.
     * Event duplication should be avoided.
     */
    @PostMapping(value = "/patientinfo", consumes = "application/json")
    public ResponseEntity<String> receivePatient (@RequestBody List<Event> events){
        try {
            this.eventService.saveEvents(events);
            return new ResponseEntity<>("saved", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Unable to save", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * DoGet handler for all events.
     * User will get a list of all existing events with number of attended patients
     */
    @GetMapping(value = "/allEvents")
    public Iterable<Event> getAllEvents (@RequestParam("page") int pageNum, ServletResponse response){
        Page<Event> events = this.eventRepo.findAll( PageRequest.of(pageNum-1, 10));
        return events;
    }

}
