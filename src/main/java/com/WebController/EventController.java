package com.WebController;

import com.Model.*;
import com.Repository.*;
import com.Service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

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
    @ResponseBody
    public String receivePatient (@RequestBody List<Event> events){
        Patient tempPatient = new Patient();//created patient
        List<Event> eventList = new ArrayList<>();
        Iterable<Event> allEvents = this.eventRepo.findAll();//pull all events from DB
        allEvents.forEach(eventList::add);//add them to the list
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

    /**
     * DoGet handler for all events.
     * User will get a list of all existing events with number of attended patients
     */
    @GetMapping(value = "/allEvents", produces = "application/json")
    @ResponseBody
    public List<Event> getAllEvents (){
        List<Event> list = new ArrayList<>();
        Iterable<Event> events = this.eventRepo.findAll();
        events.forEach(list::add);
        return list;
    }

    @PostMapping(value = "/userregister", consumes = "application/json")
    @ResponseBody
    public String userRegistration(@RequestBody User user){
        if (this.userRepository.findUserByEmailAddressIgnoreCase(user.getEmailAddress())==null){
            return "user already exists";
        }
        userRepository.save(user);
        ConfirmationToken token = new ConfirmationToken(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmailAddress());
        message.setSubject("COVID-19 TRacking System Registration.");
        message.setFrom("covidtracking08@gmail.com");
        message.setText("To confirm your email address, please click here:"
        + "http://localhost:8080/confirmaccount?token="+token.getConfirmationToken());
        emailSenderService.sendEmail(message);
        return "Confirmation sent to: "+user.getEmailAddress();
    }

    @RequestMapping (value = "confirmaccount", method = {RequestMethod.POST, RequestMethod.GET})
    public String ConfirmEmail (@RequestParam("token") String confirmationToken, Model model){
        ConfirmationToken token = this.confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token!=null){//valid user
            String emailAddress = token.getUser().getEmailAddress();
            User user = userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
            user.setEnable(true);
            userRepository.save(user);
            model.addAttribute("emailAddress", emailAddress);
            return "RegisterSuccess";
        }else {
            return "Error";
        }
    }

    @GetMapping (value = "login", consumes = "application/json")
    public String login (@RequestParam User longinUser){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(longinUser.getEmailAddress());
        if (user!=null){
            if (user.isEnabled()){
                return "success";
            } else {
                return "Please verify you email address";
            }
        }
        return "fail";
    }

}
