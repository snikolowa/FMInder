package com.example.fminder.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class ViewController {

    @GetMapping("/register")
    public String showRegisterPage(){
        return "register";
    }

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/")
    public String showIndexPage(){
        return "index";
    }

    @GetMapping("/profile")
    public String showProfilePage(){
        return "profile";
    }

    @GetMapping("/matches")
    public String showMatchesPage(){
        return "matches";
    }
}
