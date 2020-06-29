package com.WebController;

import com.Model.*;
import com.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * DoGet handler for all events.
     * User will get a list of all existing events with number of attended patients
     */
    @GetMapping(value = "/allEvents")
    public Iterable<Event> getAllEvents (@RequestParam("page") int pageNum){
        Page<Event> events = this.eventRepo.findAll( PageRequest.of(pageNum-1, 10));
        return events;
    }

    @PostMapping (value = "/eventinfo")
    public Patient addPatientEventAssociation (@RequestBody Event event, HttpSession session, Principal principal) {
        //prevent user from input garbage event with empty name, empty date or empty place.
        if (event.getDate()==null ||event.getName().equals("unknwon") || event.getName().equals("") || event.getPlace().getAddress().equals("") || event.getPlace().getAddress().equals("unknown")){
            return null;
        }
        boolean isVerified = false;
        if (principal!=null && this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName())!=null){
            isVerified = true;
        }
        Optional<Patient> p = this.patientRepository.findById(session.getId());
        Patient patient;
        if (p.isPresent()){
            patient = p.get();
        } else {
            patient = new Patient(session.getId());
        }
        List<Event> existEvents =  this.eventRepo.findAllByNameAndDate(event.getName(), event.getDate());
        if (existEvents.indexOf(event)>-1){
            event = existEvents.get(existEvents.indexOf(event));
        }
        PatientEventAssociation association = new PatientEventAssociation(event, patient, isVerified);
        event.addAssociation(association);
        patient.addAssociation(association);
        return this.patientRepository.save(patient);
    }

    @PutMapping (value = "/submitpatient")
    public void submitPatient (HttpSession session, HttpServletRequest request){
        session.invalidate();
        HttpSession session1 = request.getSession();
    }

}
