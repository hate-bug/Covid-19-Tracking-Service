package com.Repository;

import com.Model.User_Entity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User_Entity, Long> {

    User_Entity findUserByEmailAddressIgnoreCase(String emailAddress);
}
