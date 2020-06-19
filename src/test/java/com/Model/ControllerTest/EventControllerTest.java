package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Model.Event;
import com.Model.Place;
import com.Repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import javax.transaction.Transactional;
import java.util.Calendar;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    /**
     * Test if the page can be loaded correctly
     */
    @Test
    public void contextLoad () throws Exception{
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    /**
     * Test if patient information can be post to the backend.
     */
    @Test
    public void testPostPatients () throws Exception{
        String jsonString = "[{\"name\":\"E1\",\"date\":\"2020-01-01\",\"place\":{\"address\":\"1123Ahiji\",\"longitude\":\"123131\",\"latitude\":\"123123\"}},{\"name\":\"E2\",\"date\":\"2222-02-02\",\"place\":{\"address\":\"313132LJ\",\"longitude\":\"13312\",\"latitude\":\"23213\"}}]";
        mockMvc.perform(post("/patientinfo").content(jsonString)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
        String jsonString1 = "[{\"name\":\"E1\",\"date\":\"2020-01-01\",\"place\":{\"address\":\"1123Ahiji\",\"longitude\":\"123131a\",\"latitude\":\"123123\"}},{\"name\":\"E2\",\"date\":\"2222-02-02\",\"place\":{\"address\":\"313132LJ\",\"longitude\":\"13312.123\",\"latitude\":\"23213\"}}]";
        mockMvc.perform(post("/patientinfo").content(jsonString1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test get events with Json
     */
    @Test
    public void testGetEvents () throws Exception{
        Place p1 = new Place("home", 1234,1234);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 1, 1);
        Event e1 = new Event("E1", p1, calendar.getTime());
        this.eventRepository.save(e1);
        Place p2 = new Place("Work", 2222, 2222);
        Event e2 = new Event("E2", p2, calendar.getTime());
        this.eventRepository.save(e2);
        mockMvc.perform(get("/allEvents?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect((jsonPath("$.content[0].name", is(e1.getName()))))
                .andExpect((jsonPath("$.content[0].place.address", is(p1.getAddress()))))
                .andExpect(jsonPath("$.content[0].place.longitude", is(p1.getLongitude())))
                .andExpect(jsonPath("$.content[1].name", is(e2.getName())))
                .andExpect(jsonPath("$.content[1].place.address", is(p2.getAddress())))
                .andExpect(jsonPath("$.content[1].place.latitude", is(p2.getLatitude())));
    }

    @Test
    public void testPostAndGetEvent () throws Exception {
        String jsonString ="[]";
        mockMvc.perform(post("/patientinfo").content(jsonString)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/allEvents?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));

        String jsonString1 = "[{\"name\":\"E1\",\"date\":\"2020-01-01\",\"place\":{\"address\":\"1123Ahiji\",\"longitude\":\"123131a\",\"latitude\":\"123123\"}},{\"name\":\"E2\",\"date\":\"2222-02-02\",\"place\":{\"address\":\"313132LJ\",\"longitude\":\"\",\"latitude\":\"\"}}]";
        mockMvc.perform(post("/patientinfo").content(jsonString1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
