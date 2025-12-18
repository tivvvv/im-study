package com.tiv.im.study.auth;

import com.tiv.im.study.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void testMyBatisPlus() {
        System.out.println(userService.count());
    }

}
