package com.kakao.sunsuwedding._core.security;

import com.kakao.sunsuwedding._core.errors.exception.UnauthorizedException;
import com.kakao.sunsuwedding._core.errors.exception.ForbiddenException;
import com.kakao.sunsuwedding._core.utils.FilterResponseUtils;
import com.kakao.sunsuwedding.user.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtExceptionFilter jwtExceptionFilter;
    private final FilterResponseUtils filterResponseUtils;
    private final TokenService tokenService;
    private final JWTProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, tokenService));
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
                    filterResponseUtils.unAuthorized(response, new UnauthorizedException("인증되지 않았습니다"));
                })
        );

        // 9. 권한 실패 처리
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
                    filterResponseUtils.forbidden(response, new ForbiddenException("권한이 없습니다"));
                })
        );

        // 11. 인증, 권한 필터 설정
        /**
         *  로그인 필요 X - 회원가입, 로그인, 포트폴리오 조회
         *  웨딩 플래너만 - 게시글 등록 수정 삭제, 견적서 등록, 수정, 견적서 1개 확정
         *  예비 부부만 - 채팅방 생성, 견적서 전체 확정, 견적서 전체 확정, 결제
         */
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/user/signup"),
                                new AntPathRequestMatcher("/user/login"),
                                new AntPathRequestMatcher("/portfolios/**", "GET")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/user/**"),
                                new AntPathRequestMatcher("/portfolios/**"),
                                new AntPathRequestMatcher("/chat/**"),
                                new AntPathRequestMatcher("/quotations/**"),
                                new AntPathRequestMatcher("/payments/**")
                                ).authenticated()
                        // 검증 필요
                        .requestMatchers(
                                new AntPathRequestMatcher("/chat", "POST"),
                                new AntPathRequestMatcher("/quotations/confirmAll/**", "POST")
                        ).hasRole("couple")
                        .requestMatchers(
                                new AntPathRequestMatcher("/portfolios", "POST"),
                                new AntPathRequestMatcher("/portfolios", "PUT"),
                                new AntPathRequestMatcher("/portfolios", "DELETE"),
                                new AntPathRequestMatcher("/quotations/**", "PUT"),
                                new AntPathRequestMatcher("/quotations/**", "POST")
                        ).hasRole("planner")
                        .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
