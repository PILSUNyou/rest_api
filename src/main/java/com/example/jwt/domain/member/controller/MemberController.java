package com.example.jwt.domain.member.controller;

import com.example.jwt.domain.member.entity.Member;
import com.example.jwt.domain.member.service.MemberService;
import com.example.jwt.global.rsData.RsData;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.PessimisticReadUpdateLockingStrategy;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.ALL_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class MemberController {
    private final MemberService memberService;

    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResponse {
        private final String accessToken;
    }

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public RsData<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse resp){

        // 테스트용
//        resp.addHeader("Authentication","JWT Token");

        String accessToken = memberService.genAccessToken(loginRequest.getUsername(), loginRequest.getPassword());

        resp.addHeader("Authentication", accessToken);
        return RsData.of(
                "S-1",
                "엑세스 토큰 생성되었습니다.",
                new LoginResponse(accessToken)
        );
    }
    @Getter
    @AllArgsConstructor
    public static class MeResponse {
        private final Member member; // member에는 username , password, nickname을 가지고 있따
    }

    @GetMapping(value = "/me", consumes = ALL_VALUE)
    public RsData<MeResponse> me() {
        Member member = memberService.findByUsername("user1").get();

        return RsData.of(
                "S-2",
                "성공",
                new MeResponse(member)
        );
    }

}
