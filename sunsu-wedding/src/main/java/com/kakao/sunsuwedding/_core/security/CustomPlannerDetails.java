package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.planner.Planner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class CustomPlannerDetails implements UserDetails {

    private final Planner planner;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(Role.PLANNER.getRoleName()));
    }

    @Override
    public String getPassword() {
        return planner.getPassword();
    }

    @Override
    public String getUsername() {
        return planner.getEmail();
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
