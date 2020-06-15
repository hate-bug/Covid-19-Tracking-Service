package com.Repository;

import com.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    public User findUserByEmailAddressIgnoreCase(String emailAddress);
}
