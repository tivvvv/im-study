package com.tiv.im.study.auth.controller;

import com.tiv.im.study.gateway.common.CodeEnum;
import com.tiv.im.study.gateway.common.Result;
import com.tiv.im.study.auth.context.UserContext;
import com.tiv.im.study.auth.data.user.login.LoginByCodeRequest;
import com.tiv.im.study.auth.data.user.login.LoginRequest;
import com.tiv.im.study.auth.data.user.login.LoginResponse;
import com.tiv.im.study.auth.data.user.register.RegisterRequest;
import com.tiv.im.study.auth.data.user.register.RegisterResponse;
import com.tiv.im.study.auth.data.user.update.UpdateUserInfoRequest;
import com.tiv.im.study.gateway.exception.GlobalException;
import com.tiv.im.study.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);

        return Result.success(response);
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return Result.success(response);
    }

    @PostMapping("/login/code")
    public Result<LoginResponse> loginByCode(@Valid @RequestBody LoginByCodeRequest request) {
        LoginResponse response = userService.loginByCode(request);

        return Result.success(response);
    }

    @PostMapping("/update/info")
    public Result<LoginResponse> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest request) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new GlobalException(CodeEnum.NOT_LOGIN_ERROR);
        }
        LoginResponse response = userService.updateUserInfo(userId, request);
        return Result.success(response);
    }

}
