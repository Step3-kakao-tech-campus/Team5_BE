package com.kakao.sunsuwedding.user;

import com.kakao.sunsuwedding.user.constant.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoleTest {
    @Test
    public void planner_role_test() throws Exception {
        String roleName = "planner";
        Role result = Role.valueOfRole(roleName);

        Assertions.assertThat(result).isEqualTo(Role.PLANNER);
    }
    @Test
    public void couple_role_test() throws Exception {
        String roleName = "couple";
        Role result = Role.valueOfRole(roleName);

        Assertions.assertThat(result).isEqualTo(Role.COUPLE);
    }
    @Test
    public void null_role_test() throws Exception {
        String roleName = "asdf";
        Role result = Role.valueOfRole(roleName);

        Assertions.assertThat(result).isEqualTo(null);
    }
}
