package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.base_user.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userJPARepository.findByEmailNative(email).orElseThrow(
                () -> new UsernameNotFoundException(BaseException.USER_EMAIL_NOT_FOUND.getMessage())
        );

        return new CustomUserDetails(user);
    }
}
