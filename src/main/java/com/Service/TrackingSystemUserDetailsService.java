package com.Service;

import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TrackingSystemUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if (this.userRepository.findUserByEmailAddressIgnoreCase(userName)==null){
            throw new UsernameNotFoundException("Not found");
        }
        return this.userRepository.findUserByEmailAddressIgnoreCase(userName);
    }

}
