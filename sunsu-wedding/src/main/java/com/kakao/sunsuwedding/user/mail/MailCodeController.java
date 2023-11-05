package com.kakao.sunsuwedding.user.mail;

import com.kakao.sunsuwedding._core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailCodeController {
    private final MailService mailService;

    @PostMapping("")
    public ResponseEntity<?> sendAuthenticationCode(@RequestBody MailRequest.SendCode request) {
        mailService.send(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> checkAuthenticationCode(@RequestBody MailRequest.CheckCode request) {
        mailService.verify(request);
        return ResponseEntity.ok().body(ApiUtils.success(null));
    }
}
