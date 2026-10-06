package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping({ "/", "/login", "/proxy/8080", "/proxy/8080/", "/proxy/8080/login" })
    public String loginView() {
        return "login";
    }
}