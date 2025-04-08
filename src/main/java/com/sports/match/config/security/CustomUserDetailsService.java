package com.sports.match.config.security;

import com.sports.match.auth.model.dto.MemberDto;
import com.sports.match.auth.model.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDto memberDto = authMapper.selectMemberById(username);
        return new org.springframework.security.core.userdetails.User(
                memberDto.getMemId(), // DB에서 조회한 사용자 아이디
                memberDto.getMemPw(), // DB에서 조회한 암호화된 비밀번호
                AuthorityUtils.createAuthorityList(memberDto.getMemRole()) // 권한 설정 (예: ROLE_USER)
                //이거 커스텀으로 만들어줘야할수도
        );
    }
}
