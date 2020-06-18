package com.Service;

import com.Model.UserDetail;
import com.Repository.AdminUserRepository;
import com.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (this.userRepository.findUserByEmailAddressIgnoreCase(userName)==null && this.adminUserRepository.findUserByEmailAddressIgnoreCase(userName)==null){
            throw new UsernameNotFoundException("Not found");
        }
        if (this.userRepository.findUserByEmailAddressIgnoreCase(userName)!=null){
            return new UserDetail(this.userRepository.findUserByEmailAddressIgnoreCase(userName));
        }
        return new UserDetail(this.adminUserRepository.findUserByEmailAddressIgnoreCase(userName));
    }

}
