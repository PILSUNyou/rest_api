package com.example.jwt.domain.member;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @PostMapping("/login")
    public String login(){
        return "성공";
    }
}
