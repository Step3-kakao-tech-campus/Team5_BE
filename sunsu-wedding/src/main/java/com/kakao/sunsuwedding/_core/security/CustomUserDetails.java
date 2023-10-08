package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final Planner planner;

    private final Couple couple;

    // security 에 사용하기 위한 권한 설정(플래너 -> planner, 커플 -> couple)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getInfo().getFirst().getRoleName()));
    }

    // role(planner or couple), userId
    public Pair<Role, Integer> getInfo(){
        return (couple == null) ? Pair.of(Role.PLANNER, planner.getId()) : Pair.of(Role.COUPLE, couple.getId());
    }

    @Override
    public String getPassword() {
        return (couple == null) ? planner.getPassword() : couple.getPassword();
    }

    @Override
    public String getUsername() {
        return (couple == null) ? planner.getEmail() : couple.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
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
