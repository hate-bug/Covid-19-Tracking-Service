package com.Repository;

import com.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmailAddressIgnoreCase(String emailAddress);
}
