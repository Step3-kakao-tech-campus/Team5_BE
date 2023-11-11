package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.user.token.TokenDTO;
import com.kakao.sunsuwedding.user.token.TokenServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {
    private final UserServiceImpl userServiceImpl;
    private final TokenServiceImpl tokenServiceImpl;
    private final JWTProvider jwtProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequest.SignUpDTO requestDTO) {
        UserResponse.FindUserId response = userServiceImpl.signup(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(response));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest.LoginDTO requestDTO) {
        Pair<TokenDTO, UserResponse.FindUserId> response = userServiceImpl.login(requestDTO);
        return ResponseEntity
                .ok()
                .header(jwtProvider.AUTHORIZATION_HEADER, response.getFirst().accessToken())
                .header(jwtProvider.REFRESH_HEADER, response.getFirst().refreshToken())
                .body(ApiUtils.success(response.getSecond()));
    }

    @PostMapping("/token")
    public ResponseEntity<?> refresh(@AuthenticationPrincipal CustomUserDetails userDetails) {
        TokenDTO tokens = tokenServiceImpl.refreshAllTokens(userDetails.getUser());
        return ResponseEntity
                .ok()
                .header(jwtProvider.AUTHORIZATION_HEADER, tokens.accessToken())
                .header(jwtProvider.REFRESH_HEADER, tokens.refreshToken())
                .body(ApiUtils.success(null));
    }

    // 유저 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse.FindById responseDTO = userServiceImpl.findById(userDetails.getUser().getId());
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    // 회원 탈퇴
    @DeleteMapping("")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userServiceImpl.withdraw(userDetails.getUser());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
