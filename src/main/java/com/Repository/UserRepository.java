package com.Repository;

import com.Model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findUserByEmailAddressIgnoreCase(String emailAddress);
}
