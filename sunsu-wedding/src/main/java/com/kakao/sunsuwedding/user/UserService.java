package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding.user.base_user.User;
import com.kakao.sunsuwedding.user.token.TokenDTO;
import org.springframework.data.util.Pair;

public interface UserService {

    UserResponse.FindUserId signup(UserRequest.SignUpDTO requestDTO);

    Pair<TokenDTO, UserResponse.FindUserId> login(UserRequest.LoginDTO requestDTO);

    UserResponse.FindById findById(User user);

    void withdraw(User user);
}
