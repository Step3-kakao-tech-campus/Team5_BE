package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.security.CustomUserDetails;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding._core.utils.ApiUtils;
import com.kakao.sunsuwedding.user.constant.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserRequest.SignUpDTO requestDTO) {
        userService.signup(requestDTO);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 로그인 (테스트용 - token 받으려고 대충 만듦, 지워야함)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid String email) {
        String jwt = userService.login(email);
        return ResponseEntity.ok().header(JWTProvider.HEADER, jwt).body(ApiUtils.success(null));
    }

    // 유저 정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Role, Integer> info = userDetails.getInfo();
        UserResponse.FindById responseDTO = userService.findById(info.getFirst(), info.getSecond());
        return ResponseEntity.ok().body(ApiUtils.success(responseDTO));
    }

    // 유저 등급 업그레이드
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgrade(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Pair<Role, Integer> info = userDetails.getInfo();
        userService.upgrade(info.getFirst(), info.getSecond());
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    // 테스트용, 지워야함
    @GetMapping("/test")
    public ResponseEntity<?> findById(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 역할: Role.PLANNER | Role.COUPLE, 유저의 id
        Pair<Role, Integer> info = userDetails.getInfo();
        System.out.println("role: " + info.getFirst().getRoleName() + " id: " + info.getSecond());
        return ResponseEntity.ok(ApiUtils.success(null));
    }

}
