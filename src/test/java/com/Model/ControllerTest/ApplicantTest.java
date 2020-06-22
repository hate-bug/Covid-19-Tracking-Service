package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Repository.ApplicantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import javax.transaction.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class ApplicantTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApplicant () throws Exception {
        String jsonString = "{\"applicantEmail\":\"test@testa.com\",\"description\":\"adfajsldfjaf;a\"}";
        mockMvc.perform(post("/postapplication").content(jsonString).contentType("application/json"))
                .andExpect(status().isOk());
    }
}
