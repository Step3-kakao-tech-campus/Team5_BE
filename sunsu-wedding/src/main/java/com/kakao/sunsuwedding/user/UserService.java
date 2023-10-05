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
        Role role = Role.valueOfRole(requestDTO.getRole());
        try {
            if (role == Role.COUPLE) {
                coupleJPARepository.save(requestDTO.toCoupleEntity());
            }
            else {
                plannerJPARepository.save(requestDTO.toPlannerEntity());
            }
        } catch (Exception e) {
            throw new Exception500(BaseException.USER_UNEXPECTED_ERROR.getMessage() + e.getMessage());
        }
    }

    public String login(UserRequest.LoginDTO requestDTO) {
        // 예비 부부
        Optional<Couple> couplePS = coupleJPARepository.findByEmail(requestDTO.getEmail());
        if (couplePS.isPresent()) {
            Couple couple = couplePS.get();
            if (!passwordEncoder.matches(requestDTO.getPassword(), couple.getPassword())) {
                throw new Exception400(BaseException.USER_PASSWORD_WRONG.getMessage());
            }
            return JWTProvider.create(couple);
        }
        // 플래너
        Optional<Planner> plannerPS = plannerJPARepository.findByEmail(requestDTO.getEmail());
        if (plannerPS.isPresent()) {
            Planner planner = plannerPS.get();
            if (!passwordEncoder.matches(requestDTO.getPassword(), planner.getPassword())) {
                throw new Exception400(BaseException.USER_PASSWORD_WRONG.getMessage());
            }
            return JWTProvider.create(planner);
        }
        // 존재하지 않는 이메일
        throw new Exception400(BaseException.USER_EMAIL_NOT_FOUND.getMessage() + requestDTO.getEmail());
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

    // 회원 탈퇴
    @Transactional
    public void withdraw(Role role, int id) {
        // -- 플래너 회원 탈퇴 --
        if (role == Role.PLANNER){
            Planner planner = getPlanner(id);
            plannerJPARepository.deleteById(id);

        }
        // -- 예비 부부 회원 탈퇴 --
        else {
            Couple couple = getCouple(id);
            coupleJPARepository.deleteById(id);
        }
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
