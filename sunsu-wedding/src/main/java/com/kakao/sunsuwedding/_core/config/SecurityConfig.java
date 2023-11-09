package com.kakao.sunsuwedding._core.config;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.security.JwtAuthenticationFilter;
import com.kakao.sunsuwedding._core.security.JwtExceptionFilter;
import com.kakao.sunsuwedding._core.utils.FilterResponseUtils;
import com.kakao.sunsuwedding.user.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtExceptionFilter jwtExceptionFilter;
    private final FilterResponseUtils filterResponseUtils;
    private final TokenServiceImpl tokenServiceImpl;
    private final JWTProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, tokenServiceImpl));
            builder.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
            super.configure(builder);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1. CSRF 해제
        http.csrf(CsrfConfigurer::disable);

        // 2. iframe 거부
        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 3. cors 재설정
        http.cors((cors) -> cors.configurationSource(configurationSource()));

        // 4. jSessionId 사용 거부 (5번을 설정하면 jsessionId가 거부되기 때문에 4번은 사실 필요 없다)
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화)
        http.formLogin(FormLoginConfigurer::disable);

        // 6. 로그인 인증창이 뜨지 않게 비활성화
        http.httpBasic(HttpBasicConfigurer::disable);

        // 7. 커스텀 필터 적용 (시큐리티 필터 교환)
        http.apply(new CustomSecurityFilterManager());

        // 8. 인증 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                    filterResponseUtils.writeResponse(response, new UnauthorizedException(BaseException.USER_UNAUTHORIZED));
                })
        );

        // 9. 권한 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                    filterResponseUtils.writeResponse(response, new ForbiddenException(BaseException.PERMISSION_DENIED_METHOD_ACCESS));
                })
        );

        // 11. 인증, 권한 필터 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/portfolio/self", "GET")
                        ).hasAuthority("planner")
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/user/signup"),
                                new AntPathRequestMatcher("/api/user/login"),
                                new AntPathRequestMatcher("/api/portfolio/**", "GET"),
                                new AntPathRequestMatcher("/api/mail/**")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/chat"),
                                new AntPathRequestMatcher("/api/match/**"),
                                new AntPathRequestMatcher("/api/review/all", "GET"),
                                new AntPathRequestMatcher("/api/review/{reviewId}", "GET"),
                                new AntPathRequestMatcher("/api/review/**", "POST"),
                                new AntPathRequestMatcher("/api/review/**", "PUT"),
                                new AntPathRequestMatcher("/api/review/**", "DELETE")
                        ).hasAuthority("couple")
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/portfolio", "POST"),
                                new AntPathRequestMatcher("/api/portfolio", "PUT"),
                                new AntPathRequestMatcher("/api/portfolio", "DELETE"),
                                new AntPathRequestMatcher("/api/quotation/**", "PUT"),
                                new AntPathRequestMatcher("/api/quotation/**", "POST"),
                                new AntPathRequestMatcher("/api/quotation/**", "DELETE")
                        ).hasAuthority("planner")
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/user/**"),
                                new AntPathRequestMatcher("/api/portfolio/**"),
                                new AntPathRequestMatcher("/api/chat/**"),
                                new AntPathRequestMatcher("/api/match/**"),
                                new AntPathRequestMatcher("/api/quotation/**"),
                                new AntPathRequestMatcher("/api/payment/**"),
                                new AntPathRequestMatcher("/api/review/**"),
                                new AntPathRequestMatcher("/api/favorite/**")
                        ).authenticated()
                        .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");

        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.DELETE);

        configuration.addAllowedOriginPattern("http://localhost:8080/**");
        configuration.addAllowedOriginPattern("http://localhost:3000/**");
        configuration.addAllowedOriginPattern("https://k6f3d3b1a0696a.user-app.krampoline.com/**");

        configuration.setAllowCredentials(true);

        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Refresh");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
