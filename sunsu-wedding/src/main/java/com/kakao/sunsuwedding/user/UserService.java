package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.user.constant.Role;
import com.kakao.sunsuwedding.user.couple.Couple;
import com.kakao.sunsuwedding.user.couple.CoupleJPARepository;
import com.kakao.sunsuwedding.user.planner.Planner;
import com.kakao.sunsuwedding.user.planner.PlannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;


    @Transactional
    public void join(UserRequest.JoinDTO requestDTO) {
        sameCheckEmail(requestDTO.getEmail());
        sameCheckPassword(requestDTO.getPassword(), requestDTO.getPassword2());
        requestDTO.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        Role role = getRole(requestDTO.getRole());
        try {
            if (role == Role.COUPLE) {
                coupleJPARepository.save(requestDTO.toCoupleEntity());
            }
            else {
                plannerJPARepository.save(requestDTO.toPlannerEntity());
            }
        } catch (Exception e) {
            throw new Exception500(BaseException.USER_UNEXPECTED_ERROR.getMessage());
        }
    }

    private Role getRole(String roleName) {
        Role role = Role.valueOfRole(roleName);
        if (role == null){
            throw new Exception400(BaseException.USER_ROLE_WRONG.getMessage() + roleName);
        }
        else return role;
    }

    private void sameCheckPassword(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new Exception400(BaseException.USER_PASSWORD_NOT_SAME.getMessage());
        }
    }

    public void sameCheckEmail(String email) {
        Optional<Couple> couple = coupleJPARepository.findByEmail(email);
        Optional<Planner> planner = plannerJPARepository.findByEmail(email);

        if (couple.isPresent() || planner.isPresent()) {
            throw new Exception400(BaseException.USER_EMAIL_EXIST.getMessage() + email);
        }
    }
}
