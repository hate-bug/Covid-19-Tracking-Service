package com.Repository;

import com.Model.UserFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserFileRepository extends CrudRepository<UserFile, Long> {

    UserFile findAllById (long id);

}
