package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.user.constant.Grade;
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
    public void signup(UserRequest.SignUpDTO requestDTO) {
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

    // 테스트용 로그인 - 삭제될 예정
    public String login(String email) {
        Optional<Couple> couplePS = coupleJPARepository.findByEmail(email);
        Optional<Planner> plannerPS = plannerJPARepository.findByEmail(email);

        return couplePS.map(JWTProvider::create).orElseGet(() -> JWTProvider.create(plannerPS.get()));
    }

    public UserResponse.FindById findById(Role role, int id) {
        if (role == Role.PLANNER){
            return new UserResponse.FindById(getPlanner(id));
        }
        else {
            return new UserResponse.FindById(getCouple(id));
        }
    }

    // 유저 등급 업그레이드
    @Transactional
    public void upgrade(Role role, int id) {
        if (role == Role.PLANNER){
            Planner planner = getPlanner(id);
            if (planner.getGrade() == Grade.PREMIUM){
                throw new Exception400(BaseException.USER_ALREADY_PREMIUM.getMessage());
            }
            planner.upgrade();
        }
        else {
            Couple couple = getCouple(id);
            if (couple.getGrade() == Grade.PREMIUM){
                throw new Exception400(BaseException.USER_ALREADY_PREMIUM.getMessage());
            }
            couple.upgrade();
        }
    }

    private Role getRole(String roleName) {
        Role role = Role.valueOfRole(roleName);
        if (role == null){
            throw new Exception400(BaseException.USER_ROLE_WRONG.getMessage());
        }
        else return role;
    }

    private void sameCheckPassword(String password, String password2) {
        boolean isEqual = Objects.equals(password, password2);
        if (!isEqual){
            throw new Exception400(BaseException.USER_PASSWORD_NOT_SAME.getMessage());
        }
    }

    private void sameCheckEmail(String email) {
        Optional<Couple> couple = coupleJPARepository.findByEmail(email);
        Optional<Planner> planner = plannerJPARepository.findByEmail(email);

        if (couple.isPresent() || planner.isPresent()) {
            throw new Exception400(BaseException.USER_EMAIL_EXIST.getMessage());
        }
    }

    private Planner getPlanner(int id){
        return plannerJPARepository.findById(id).orElseThrow(
                () -> new Exception404(BaseException.USER_NOT_FOUND.getMessage())
        );
    }
    private Couple getCouple(int id){
        return coupleJPARepository.findById(id).orElseThrow(
                () -> new Exception404(BaseException.USER_NOT_FOUND.getMessage())
        );
    }
}
