package com.kakao.sunsuwedding.user.email;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class EmailCodeRestController {
    private final EmailServiceImpl emailServiceImpl;

    @PostMapping("")
    public ResponseEntity<?> sendAuthenticationCode(@RequestBody EmailRequest.SendCode request) {
        emailServiceImpl.send(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody EmailRequest.CheckCode request) {
        emailServiceImpl.verify(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
