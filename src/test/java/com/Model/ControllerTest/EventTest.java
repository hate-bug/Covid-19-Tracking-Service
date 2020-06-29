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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
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

    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void testPostEventsWithUser () throws Exception {
        this.userRepository.save(new User("user", "user"));
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
        assertEquals(true, assoications.get(0).isVerified());
    }

    @Test
    public void testPostEventsWithoutUser () throws Exception {
        this.userRepository.save(new User("user", "user"));
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
        assertEquals(false, assoications.get(0).isVerified());
    }

    @Test
    public void testPatient () throws Exception {
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
    }

    @Test
    public void testEvent () throws Exception {
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

    @Test
    public void testVerifiedEvent() throws Exception {
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
        MvcResult result = mockMvc.perform(get("/allverifiedevents?page=1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements",is(2)))
                .andExpect(jsonPath("$.content[0].name", is("E1")))
                .andExpect(jsonPath("$.content[0].patientEventAssociations",hasSize(1)))
                .andExpect(jsonPath("$.content[1].name", is("E3")))
                .andReturn();
        MvcResult result2 = mockMvc.perform(get("/allevents?page=1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements",is(3)))
                .andExpect(jsonPath("$.content[0].name", is("E1")))
                .andExpect(jsonPath("$.content[0].patientEventAssociations",hasSize(1)))
                .andExpect(jsonPath("$.content[1].name", is("E2")))
                .andExpect(jsonPath("$.content[2].name", is("E3")))
                .andReturn();
    }
}
