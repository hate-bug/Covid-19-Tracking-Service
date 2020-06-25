package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Model.Event;
import com.Model.Patient;
import com.Model.PatientEventAssociation;
import com.Model.User;
import com.Repository.AssociationRepository;
import com.Repository.EventRepository;
import com.Repository.PatientRepository;
import com.Repository.UserRepository;
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
import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}
