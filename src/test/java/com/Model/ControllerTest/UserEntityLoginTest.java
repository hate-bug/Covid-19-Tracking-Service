package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Model.UserEntity;
import com.Model.UserPassword;
import com.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import javax.transaction.Transactional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Transactional
public class UserEntityLoginTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.mail.username}")
    private String userName;

    /**
     * User should be authenticated with correct credentials
     * @throws Exception
     */
    @Test
    public void testLoginWithCorrectCredentials () throws Exception{
        this.userRepository.save(new UserEntity(this.userName, new UserPassword("testpassword")));
        RequestBuilder requestBody = formLogin().user ("emailaddress",this.userName).password("password","testpassword").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody)
                .andExpect(status().isFound())
                .andExpect(authenticated().withUsername(this.userName));
    }

    /**
     * User try to login with wrong credentials should not be authenticated.
     * @throws Exception
     */
    @Test
    public void testLoginWithWrongCredentials () throws Exception{
        this.userRepository.save(new UserEntity(this.userName, new UserPassword("testpassword")));
        RequestBuilder requestBody = formLogin().user ("emailaddress",this.userName).password("password","testpasswordeee").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody)
                .andExpect(unauthenticated());
        RequestBuilder requestBody1 = formLogin().user ("emailaddress","atmin@admin.ca").password("password","testpassword").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody)
                .andExpect(unauthenticated());
    }

    /**
     * User change its password and should be able to log in with new password.
     * old credentials should become invalid
     * @throws Exception
     */
    @Test
    @WithMockUser(username="test@test.com",roles={"USER"})
    public void testChangingPasswordAndLogInWithChangedPassword() throws Exception {
        this.userRepository.save(new UserEntity("test@test.com", new UserPassword("testpassword")));
        String jsonString = "{\"oldPassword\":\"testpassword\",\"newPassword\":\"newpassword\",\"confirmnewPassword\":\"newpassword\"}";
        mockMvc.perform(post("/changepassword").content(jsonString).contentType("application/json"))
                .andExpect(status().isOk());
        UserEntity userEntity = this.userRepository.findUserByEmailAddressIgnoreCase("test@test.com");
        RequestBuilder requestBody = formLogin().user ("emailaddress","test@test.com").password("password","newpassword").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody)
                .andExpect(authenticated().withUsername("test@test.com"));

        //Old credentials should now become invalid
        RequestBuilder requestBody1 = formLogin().user ("emailaddress","test@test.com").password("password","testpassword").loginProcessingUrl("/userlogin");
        mockMvc.perform(requestBody1)
                .andExpect(unauthenticated());
    }

}