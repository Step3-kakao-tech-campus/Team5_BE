package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.errors.BaseException;
import com.kakao.sunsuwedding._core.errors.exception.Exception400;
import com.kakao.sunsuwedding._core.errors.exception.Exception404;
import com.kakao.sunsuwedding._core.errors.exception.Exception500;
import com.kakao.sunsuwedding._core.security.JWTProvider;
import com.kakao.sunsuwedding.match.Match;
import com.kakao.sunsuwedding.match.MatchJPARepository;
import com.kakao.sunsuwedding.match.Quotation.Quotation;
import com.kakao.sunsuwedding.match.Quotation.QuotationJPARepository;
import com.kakao.sunsuwedding.portfolio.PortfolioJPARepository;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final CoupleJPARepository coupleJPARepository;
    private final PlannerJPARepository plannerJPARepository;
    private final MatchJPARepository matchJPARepository;
    private final QuotationJPARepository quotationJPARepository;

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
            throw new Exception500(BaseException.USER_UNEXPECTED_ERROR.getMessage() + e.getMessage());
        }
    }

    public String login(UserRequest.LoginDTO requestDTO) {
        Role role = getRole(requestDTO.getRole());
        // 예비 부부
        if (role == Role.COUPLE) {
            Couple couplePS = coupleJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                    () -> new Exception400(BaseException.USER_EMAIL_NOT_FOUND.getMessage() + requestDTO.getEmail())
            );
            if(!passwordEncoder.matches(requestDTO.getPassword(), couplePS.getPassword())) {
                throw new Exception400(BaseException.USER_PASSWORD_WRONG.getMessage());
            }
            return JWTProvider.create(couplePS);
        }
        // 웨딩 플래너
        else {
            Planner plannerPS = plannerJPARepository.findByEmail(requestDTO.getEmail()).orElseThrow(
                    () -> new Exception400(BaseException.USER_EMAIL_NOT_FOUND.getMessage() + requestDTO.getEmail())
            );
            if(!passwordEncoder.matches(requestDTO.getPassword(), plannerPS.getPassword())){
                throw new Exception400(BaseException.USER_PASSWORD_WRONG.getMessage());
            }
            return JWTProvider.create(plannerPS);
        }
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
            /*
            // 1. 이미지 -> 가격아이템 -> 포트폴리오 삭제
            Optional<Portfolio> portfolio = portfolioRepository.findByPlanner(planner);
            if (portfolio.isPresent()) {
                // 포폴 존재 한다면 이미지 + 가격 아이템 삭제
                List<ImageItem> imageItems = imageItemJPARepository.findAllByPlanner(planner);
                if (!imageItems.isEmpty()) {
                    // 이미지 삭제 ------------ 추후 구현
                }
                List<PriceItem> priceItems = priceItemJPARepository.findAllByPlanner(planner);
                if (!priceItems.isEmpty()) {
                    // 가격 아이템 삭제 ------------ 추후 구현
                }
                // 포트폴리오 삭제
                portfolioRepository.deleteByPlanner(planner);
            }
            */
            // 2. 견적서 -> 매칭내역 => null로 변경? 그대로 두기?
            List<Match> matches = matchJPARepository.findAllByPlanner(planner);
            if (!matches.isEmpty()) { // 매칭 내역 존재한다면
                for (Match match : matches) {
                    List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
                    if (!quotations.isEmpty()) { // 작성된 견적서 있으면
                        for (Quotation quotation: quotations) {
                            // 견적서와 매칭 내역 연결 끊기  ---------- 추후 구현
                        }
                    }
                }
                // 매칭 내역과 플래너 연결 끊기  ---------- 추후 구현
            }

            // 3. 유저 삭제
            plannerJPARepository.deleteById(id);

        }
        // -- 예비부부 회원 탈퇴 --
        else {
            Couple couple = getCouple(id);
            // 1. 견적서 -> 매칭 내역 => null로 변경? 그대로 두기?
            List<Match> matches = matchJPARepository.findAllByCouple(couple);
            if (!matches.isEmpty()){
                for (Match match : matches) {
                    List<Quotation> quotations = quotationJPARepository.findAllByMatch(match);
                    if (!quotations.isEmpty()) {
                        for (Quotation quotation: quotations) {
                            // 견적서와 매칭 내역 연결 끊기  ---------- 추후 구현
                        }
                    }
                }
                // 매칭 내역과 예비 부부 연결 끊기  ---------- 추후 구현

            }

            // 2. 유저 삭제
            coupleJPARepository.deleteById(id);
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
