package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Model.*;
import com.Repository.AssociationRepository;
import com.Repository.EventRepository;
import com.Repository.PatientRepository;
import com.Repository.UserRepository;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class EventTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * When verified user posts events, he is able to create verified associations, duplicated events are not accepted.
     * After events saved, user is able to retrieve events and event places from CrudRepository.
     * If two different patients attended same place's events, then two identical places should be returned.
     * @throws Exception
     */
    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void verifiedUserPostEvent () throws Exception {
        this.userRepository.save(new UserEntity("user", new UserPassword("user")));
        ArrayList<Event> list = new ArrayList<>();
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(0, list.size());
        ArrayList<PatientEventAssociation> associations = new ArrayList<>();
        String event = "{\"name\":\"e1\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(1, list.size());
        assertEquals("e1", list.get(0).getName());
        assertEquals("2201 Riverside Drive, Ottawa, ON, Canada", list.get(0).getPlace().getAddress());
        this.associationRepository.findAll().forEach(associations::add);
        assertEquals(1, associations.size());
        assertEquals(true, associations.get(0).isVerified());

        //If user post duplicated event, database does persist it.
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        list.clear();
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(1, list.size());
        assertEquals("e1", list.get(0).getName());
        mockMvc.perform(get("/patientEventAssociations").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.patientEventAssociations",hasSize(2)))
                .andExpect(jsonPath("$._embedded.patientEventAssociations[0].verified",is(true)));
        mockMvc.perform(get("/patientEventAssociations/search/findAllByisValid?isValid=true").contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.patientEventAssociations",hasSize(2)));
    }

    /**
     * Anonymous users are able to create unverified associations. They cannot post duplicated events.
     * @throws Exception
     */
    @Test
    public void anonymousUserPostEvents () throws Exception {
        this.userRepository.save(new UserEntity("user", new UserPassword("user")));
        ArrayList<Event> list = new ArrayList<>();
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(0, list.size());
        ArrayList<PatientEventAssociation> assoications = new ArrayList<>();
        String event = "{\"name\":\"e1\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(1, list.size());
        assertEquals("e1", list.get(0).getName());
        assertEquals("2201 Riverside Drive, Ottawa, ON, Canada", list.get(0).getPlace().getAddress());
        this.associationRepository.findAll().forEach(assoications::add);
        assertEquals(1, assoications.size());
        assertFalse( assoications.get(0).isVerified());
        //Post another duplicated event, it should be ignored
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        list.clear();
        this.eventRepository.findAll().forEach(list::add);
        assertEquals(1, list.size());
        mockMvc.perform(get("/patientEventAssociations").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.patientEventAssociations",hasSize(2)));
    }

    /**
     * When user create events with different sessions, new patients should be created each time.
     * Each patient has its own attended place, three places should be returned here.
     * @throws Exception
     */
    @Test
    public void userCreateAssociationsWithDifferentSessions () throws Exception {
        MockHttpSession session = new MockHttpSession();
        ArrayList <Patient> patients = new ArrayList<>();
        this.patientRepository.findAll().forEach(patients::add);
        assertEquals(0,patients.size());
        String event = "{\"name\":\"e1\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        MvcResult result = mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk()).andReturn();
        this.patientRepository.findAll().forEach(patients::add);
        assertEquals(1, patients.size());

        String event1 = "{\"name\":\"e2\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        session = (MockHttpSession) result.getRequest().getSession();
        MvcResult result1 = mockMvc.perform(post("/eventinfo").content(event1).contentType("application/json"))
                .andExpect(status().isOk()).andReturn();
        session = (MockHttpSession) result1.getRequest().getSession();
        patients.clear();

        this.patientRepository.findAll().forEach(patients::add);
        assertEquals(2, patients.size());
        String event2 = "{\"name\":\"e2\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        mockMvc.perform(post("/eventinfo").content(event2).contentType("application/json"))
                .andExpect(status().isOk());
        patients.clear();
        this.patientRepository.findAll().forEach(patients::add);
        assertEquals(3, patients.size());
        mockMvc.perform(get("/patientEventAssociations").contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.patientEventAssociations",hasSize(3)));
    }

    /**
     * When user post different events, they should be persisted by DB.
     * @throws Exception
     */
    @Test
    public void nonymousUserCreateDifferentEvents () throws Exception {
        String event = "{\"name\":\"e1\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2201 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        Iterable<Event> list = this.eventRepository.findAll();
        assertEquals(1, IterableUtil.sizeOf(list));
        mockMvc.perform(post("/eventinfo").content(event).contentType("application/json"))
                .andExpect(status().isOk());
        list = this.eventRepository.findAll();
        assertEquals(1, IterableUtil.sizeOf(list));
        String event1 = "{\"name\":\"e1\",\"date\":\"2020-12-12\",\"place\":{\"address\":\"2203 Riverside Drive, Ottawa, ON, Canada\",\"longitude\":\"-75.6745825\",\"latitude\":\"45.38841\"}}";
        mockMvc.perform(post("/eventinfo").content(event1).contentType("application/json"))
                .andExpect(status().isOk());
        list = this.eventRepository.findAll();
        assertEquals(2, IterableUtil.sizeOf(list));
    }

    /**
     * @Before: Three different associations exist in database: verified association between P1 and E1,
     * unverified assiciation between P2 and E2, Verified association between P3 and E3.
     * @After: When user is trying to get all verified associations, they will get information about E1 and E3.
     * User can get all three events if choose all events option.
     * @throws Exception
     */
    @Test
    public void retrieveVerifiedOrAllEvents() throws Exception {
        Patient p1 = new Patient();
        Patient p2 = new Patient();
        Patient p3 = new Patient();
        Event e1 = new Event("E1", new Place("2201 Riverside", 110.21, -11.23), new Date(2020,11,12));
        Event e2 = new Event("E2", new Place("2201 Riverside", 110.21, -11.23), new Date(2020,11,12));
        Event e3 = new Event("E3", new Place("2201 Riverside", 110.21, -11.23), new Date(2020,11,12));
        PatientEventAssociation a1 = new PatientEventAssociation(e1, p1, true);
        PatientEventAssociation a2 = new PatientEventAssociation(e2, p2, false);
        PatientEventAssociation a3 = new PatientEventAssociation(e3, p3, true);
        p1.addAssociation(a1);
        e1.addAssociation(a1);
        p2.addAssociation(a2);
        e2.addAssociation(a2);
        p3.addAssociation(a3);
        e3.addAssociation(a3);
        Patient[] patients = {p1, p2, p3};
        Iterable<Patient> patientList = Arrays.asList(patients);
        this.patientRepository.saveAll(patientList);
        assertEquals(3, IterableUtil.sizeOf(this.associationRepository.findAll()));
        MvcResult result = mockMvc.perform(get("/patientEventAssociations/search/findAllByisValid?isValid=true")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.patientEventAssociations",hasSize(2)))
                .andReturn();
        MvcResult result2 = mockMvc.perform(get("/events")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.events",hasSize(3)))
                .andReturn();
    }
}
