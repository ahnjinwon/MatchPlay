package com.sports.match.config.security;

import com.sports.match.auth.model.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final int memNo;
    private final String username;
    private final String password;
    private final String memName;
    private final String memTel;
    private final String memEmail;
    private final Timestamp createdAt;
    private final String grade;
    private final int approved;
    private final String profileImg;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(MemberDto memberDto) {
        this.memNo = memberDto.getMemNo();    // memNo 추가!
        this.username = memberDto.getMemId();
        this.password = memberDto.getMemPw();
        this.memName = memberDto.getMemName();
        this.memTel = memberDto.getMemTel();
        this.memEmail = memberDto.getMemEmail();
        this.createdAt = memberDto.getCreatedAt();
        this.grade = memberDto.getGrade();
        this.approved = memberDto.getApproved();
        this.profileImg = memberDto.getProfileImg();
        this.authorities = AuthorityUtils.createAuthorityList(memberDto.getMemRole());
    }

    public String getMemEmail() {
        return memEmail;
    }

    public int getMemNo() {
        return memNo;
    }

    public String getMemName() {
        return memName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    public String getMemTel() {
        return memTel;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getGrade() {
        return grade;
    }

    public int getApproved() {
        return approved;
    }

    public String getProfileImg() {
        return profileImg;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}