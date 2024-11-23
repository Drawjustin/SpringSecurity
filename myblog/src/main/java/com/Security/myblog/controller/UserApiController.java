package com.Security.myblog.controller;

import com.Security.myblog.dto.AddUserRequest;
import com.Security.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {
    private final UserService userService;

    @PostMapping("/user")
    public String signup(AddUserRequest request){
        userService.save(request);
        return "redirect:/login";
    }

}
