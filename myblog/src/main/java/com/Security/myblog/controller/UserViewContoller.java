package com.Security.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewContoller {
    @GetMapping("/login")
    public String login(){
        return "oauthLogin";
    }
    @GetMapping("signup")
    public String singup(){
        return "signup";
    }
}
