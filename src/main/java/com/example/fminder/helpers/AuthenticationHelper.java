package com.example.fminder.helpers;

import com.example.fminder.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    public static final String LOGGED = "LOGGED";
    public static final String USER_ID = "USER_ID";
    public static final String REMOTE_IP = "REMOTE_IP";

    public void loginUser(HttpServletRequest request, int id) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED, true);
        session.setAttribute(USER_ID, id);
        session.setAttribute(REMOTE_IP, request.getRemoteAddr());
    }

    public int getLoggedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        checkIfUserIsLogged(request);
        return (int) session.getAttribute(USER_ID);
    }

    public void checkIfUserIsLogged(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String IP = request.getRemoteAddr();

        if(session.isNew() || session.getAttribute(LOGGED) == null
                || !(boolean) session.getAttribute(LOGGED) || !session.getAttribute(REMOTE_IP).equals(IP)){
            throw new UnauthorizedException("You have to login!");
        }
    }
}
