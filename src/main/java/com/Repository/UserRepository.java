package com.Repository;

import com.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    public User findUserByEmailAddressIgnoreCase(String emailAddress);
}
