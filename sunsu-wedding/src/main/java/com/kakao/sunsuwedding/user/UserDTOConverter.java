package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding._core.utils.DateFormat;
import com.kakao.sunsuwedding.user.base_user.User;

public class UserDTOConverter {

    public static UserResponse.FindById toFindByIdDTO(User user){
        return new UserResponse.FindById(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDtype(),
                user.getGrade().getGradeName(),
                DateFormat.dateFormatKorean(user.getUpgradeAt())
        );
    }
}
