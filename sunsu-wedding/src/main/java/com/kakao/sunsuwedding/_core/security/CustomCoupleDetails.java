package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Getter
public class CustomCoupleDetails implements UserDetails {

    private final Couple couple;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(Role.COUPLE.getRoleName()));
    }

    @Override
    public String getPassword() {
        return couple.getPassword();
    }

    @Override
    public String getUsername() {
        return couple.getEmail();
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
