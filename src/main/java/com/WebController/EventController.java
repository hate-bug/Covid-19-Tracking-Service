package com.WebController;

import com.Model.*;
import com.Repository.*;
import com.Service.EmailSenderService;
import com.Service.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private MessageSender messageSender;

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
    public ResponseEntity<String> receivePatient (@RequestBody List<Event> events){
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
        return new ResponseEntity<>("saved", HttpStatus.OK);
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
    public ResponseEntity<String> userRegistration(@RequestBody User user, HttpServletRequest request){
        if (this.userRepository.findUserByEmailAddressIgnoreCase(user.getEmailAddress())!=null){
            return new ResponseEntity<>("user already exists", HttpStatus.FORBIDDEN);
        }
        userRepository.save(user);
        ConfirmationToken token = new ConfirmationToken(user);
        String rootAddress = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        this.messageSender.sendRegisterEmail(user.getEmailAddress(), token.getConfirmationToken(), rootAddress);
        this.confirmationTokenRepository.save(token);
        return new ResponseEntity<>("Confirmation email sent to: "+user.getEmailAddress(), HttpStatus.OK);
    }

    @RequestMapping (value = "/confirmaccount", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView ConfirmEmail (@RequestParam("token") String confirmationToken, Model model){
        ConfirmationToken token = this.confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ModelAndView modelview;
        if (token!=null){//valid user
            String emailAddress = token.getUser().getEmailAddress();
            User user = userRepository.findUserByEmailAddressIgnoreCase(emailAddress);
            user.setEnable(true);
            userRepository.save(user);
            model.addAttribute("emailAddress", emailAddress);
            modelview = new ModelAndView("RegisterSuccess");
            modelview.setStatus(HttpStatus.OK);
        }else {
            modelview = new ModelAndView("Error");
            modelview.setStatus(HttpStatus.NOT_FOUND);
        }
        return modelview;
    }

    @PostMapping (value = "/userlogin", consumes = "application/json")
    public ResponseEntity<String> login (@RequestBody User longinUser){
        User user = this.userRepository.findUserByEmailAddressIgnoreCase(longinUser.getEmailAddress());
        if (user!=null){
            if (user.isEnabled()){
                return new ResponseEntity<>("Successfully log in", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Please verify you email address", HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<>("Email or password not correct.", HttpStatus.UNAUTHORIZED);
    }

}
