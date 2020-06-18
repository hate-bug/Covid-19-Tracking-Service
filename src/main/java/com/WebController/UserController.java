package com.WebController;

import com.Model.User;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping (value = "/userprofile")
    public User getUserProfile (Principal principal) throws UserPrincipalNotFoundException {
        if (principal == null) {
            throw new UserPrincipalNotFoundException("Not logged in");
        } else {
            return this.userRepository.findUserByEmailAddressIgnoreCase(principal.getName());
        }
    }
}
