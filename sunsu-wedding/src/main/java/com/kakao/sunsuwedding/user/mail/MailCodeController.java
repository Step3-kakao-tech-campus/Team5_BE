package com.kakao.sunsuwedding.user.mail;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailCodeController {
    private final MailServiceImpl mailServiceImpl;

    @PostMapping("")
    public ResponseEntity<?> sendAuthenticationCode(@RequestBody MailRequest.SendCode request) {
        mailServiceImpl.send(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody MailRequest.CheckCode request) {
        mailServiceImpl.verify(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
