package com.kakao.sunsuwedding.user.base_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Long> {
    // where is_active = true가 적용되지 않음
    // 이메일 중복 비교를 위해 사용
    @Query(value = "select u.* from user_tb u where u.email = :email", nativeQuery = true)
    Optional<User> findByEmailNative(@Param("email") String email);

    // where is_active = true가 적용됨
    Optional<User> findByEmail(String email);
}
