package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Model.User;
import com.Repository.ApplicantRepository;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import javax.transaction.Transactional;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class UserLoginTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.mail.username}")
    private String userName;

    @Test
    public void testLogin () throws Exception{
        this.userRepository.save(new User(this.userName, "testpassword"));
        RequestBuilder requestBody = formLogin().user ("emailaddress",this.userName).password("password","testpassword").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody)
                .andExpect(status().isFound())
                .andExpect(authenticated().withUsername(this.userName));
    }

    @Test
    @WithMockUser(username="test@test.com",roles={"USER"})
    public void testChangePassword() throws Exception {
        this.userRepository.save(new User("test@test.com", "testpassword"));
        String jsonString = "{\"oldPassword\":\"testpassword\",\"newPassword\":\"newpassword\",\"confirmnewPassword\":\"newpassword\"}";
        mockMvc.perform(post("/changepassword").content(jsonString).contentType("application/json"))
                .andExpect(status().isOk());
        User user = this.userRepository.findUserByEmailAddressIgnoreCase("test@test.com");
        assertTrue(new BCryptPasswordEncoder().matches("newpassword", user.getPassword()));
    }

}
