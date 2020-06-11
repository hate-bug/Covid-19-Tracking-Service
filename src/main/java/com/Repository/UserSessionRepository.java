package com.Repository;

import com.Model.User;
import com.Model.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    public UserSession findBySessionId (String sessionId);
}
