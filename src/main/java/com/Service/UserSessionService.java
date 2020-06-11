package com.Service;

import com.Model.User;
import com.Repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    public User getUserFromRequest (HttpServletRequest request){
        if (request.getSession().getAttribute("usersessionid")!= null) {
            String userSession = request.getSession().getAttribute("usersessionid").toString();
            if (this.userSessionRepository.findBySessionId(userSession)!=null){
                return this.userSessionRepository.findBySessionId(userSession).getUser();
            }
        }
        return null;
    }
}
