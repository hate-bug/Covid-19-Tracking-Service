package com.Model.ControllerTest;

import com.Application.Tracking_System_Application;
import com.Configuration.ApplicationConfigurationBeans;
import com.Model.AdminUser;
import com.Model.Applicant;
import com.Model.UserPassword;
import com.Repository.ApplicantRepository;
import com.Repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;
import static junit.framework.TestCase.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Tracking_System_Application.class)
@AutoConfigureMockMvc
@Transactional
public class AdminTest {

    @Autowired
    private ApplicationConfigurationBeans passwordConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Value("${spring.mail.username}")
    private String emailAddress;

    //Apply spring security for tests
    @Before
    public void pre (){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * Test admin log in with correct credentials
     * @throws Exception
     */
    @Test
    public void AdminLoginTest () throws Exception {
        AdminUser adminUser = new AdminUser("test@test.test", new UserPassword("testpassword"));
        this.userRepository.save(adminUser);
        RequestBuilder requestBuilder = formLogin().user("emailaddress","test@test.test").password("password","testpassword").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder)
                .andExpect(authenticated().withUsername("test@test.test").withRoles("ADMIN", "USER"));

    }

    /**
     * Test admin log in with wrong credentials should not be authenticated
     * @throws Exception
     */
    @Test
    public void AdminLoginTestWithWrongCredentials () throws Exception {
        AdminUser adminUser = new AdminUser("test@test.test", new UserPassword("testpassword"));
        this.userRepository.save(adminUser);
        RequestBuilder requestBuilder = formLogin().user("emailaddress","test@test.test").password("password","testpassword222").loginProcessingUrl("/userlogin");
        this.mockMvc.perform(requestBuilder)
                .andExpect(unauthenticated());

    }

    /**
     * User with "ADMIN_Role" should get access to admin page
     * @throws Exception
     */
    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    public void adminPageTest () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    /**
     * Normal user should not get access to admin page
     * @throws Exception
     */
    @Test
    @WithMockUser(username="user",roles={"USER"})
    public void adminPageTestWithoutAdmin () throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    /**
     * User with Admin_Role should be able to approve applicants
     * Once applicants approved, it should exist in Userrepository.
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void adminAppprove () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        assertFalse (this.userRepository.findUserByEmailAddressIgnoreCase(this.emailAddress)!=null);
        String data = "\"" + String.valueOf(id) + "\"";
        this.mockMvc.perform(post("/admin/approveapplicant").content(data).contentType("application/json"))
                .andExpect(status().isOk());
        assertFalse (this.applicantRepository.existsById(id));
        assertNotNull (this.userRepository.findUserByEmailAddressIgnoreCase(this.emailAddress));
    }

    /**
     * User with Admin_Role should be able to decline applicants
     * @After: Once applicant is declined, it should be deleted.
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admindDecline () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        String data = String.valueOf(id);
        this.mockMvc.perform(delete("/admin/deleteapplicant?id="+data))
                .andExpect(status().isOk());
        assertFalse( this.applicantRepository.existsById(id));
        //user should not exist inside Userrepository since it's declined
        assertTrue (this.userRepository.findUserByEmailAddressIgnoreCase(this.emailAddress)==null);
    }

    /**
     * @Before: Application created inside repository, user log in as a USER_ROLE.
     * Normal user should not be able to access to this application. He cannot make any decision either.
     * Applicantion shall exist all the time.
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void NormalUserSendApproveOrDeclineCommandForApplication () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        String data = String.valueOf(id);
        this.mockMvc.perform(delete("/admin/deleteapplicant?id="+data))
                .andExpect(status().isForbidden());
        assertTrue( this.applicantRepository.existsById(id));
        //user should not exist inside Userrepository since it's declined
        assertTrue ( this.userRepository.findUserByEmailAddressIgnoreCase(this.emailAddress)==null);
        this.mockMvc.perform(post("/admin/approveapplicant").content(data).contentType("application/json"))
                .andExpect(status().isForbidden());
        assertTrue(this.applicantRepository.existsById(id));
    }

    /**
     * @Before: Application created inside repository.
     * Anonymous user should not be able to access to this application. He cannot make any decision either.
     * He will be redirected to a log in page with status code 302. But nothing should be changed inside backend.
     * Applicantion shall exist all the time.
     * @throws Exception
     */
    @Test
    public void AnonymousUserSendApproveOrDeclineCommandForApplication () throws Exception {
        Applicant applicant = this.applicantRepository.save(new Applicant(this.emailAddress, "description text"));
        long id = applicant.getId();
        String data = String.valueOf(id);
        this.mockMvc.perform(delete("/admin/deleteapplicant?id="+data))
                .andExpect(status().isFound());
        assertTrue(this.applicantRepository.existsById(id));
        //user should not exist inside Userrepository since it's declined
        assertTrue (this.userRepository.findUserByEmailAddressIgnoreCase(this.emailAddress)==null);
        this.mockMvc.perform(post("/admin/approveapplicant").content(data).contentType("application/json"))
                .andExpect(status().isFound());
        assertTrue(this.applicantRepository.existsById(id));
    }
}
