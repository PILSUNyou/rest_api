package com.example.jwt.domain.member.service;

import com.example.jwt.domain.member.entity.Member;
import com.example.jwt.domain.member.repository.MemberRepository;
import com.example.jwt.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Optional<Member> findByUsername(String userName) {
        return memberRepository.findByUsername(userName);
    }

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();

       memberRepository.save(member);

       return member;
    }

    public String genAccessToken(String userName, String password) {
        Member member = findByUsername(userName).orElse(null);

        if( member == null) return  null;

        if ( !passwordEncoder.matches(password, member.getPassword()) ){
            return null;
        }

        return jwtProvider.genToken(member.toClaims(), 60 * 60 * 24 * 365);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
}
