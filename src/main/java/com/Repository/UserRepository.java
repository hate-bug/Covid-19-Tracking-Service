package com.Repository;

import com.Model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoRepositoryBean
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByEmailAddressIgnoreCase(String emailAddress);
}
