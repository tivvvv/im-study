package com.tiv.im.study.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tiv.im.study.auth.data.user.login.LoginByCodeRequest;
import com.tiv.im.study.auth.data.user.login.LoginRequest;
import com.tiv.im.study.auth.data.user.login.LoginResponse;
import com.tiv.im.study.auth.data.user.register.RegisterRequest;
import com.tiv.im.study.auth.data.user.register.RegisterResponse;
import com.tiv.im.study.auth.data.user.update.UpdateUserInfoRequest;
import com.tiv.im.study.auth.model.User;

public interface UserService extends IService<User> {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse loginByCode(LoginByCodeRequest request);

    LoginResponse updateUserInfo(Long userId, UpdateUserInfoRequest request);

    default User getOnly(QueryWrapper<User> wrapper, boolean throwEx) {
        wrapper.last("limit 1");

        return this.getOne(wrapper, throwEx);
    }

    default boolean updateSelective(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();

        // 只更新非空字段
        if (user.getUserName() != null) {
            updateWrapper.set(User::getUserName, user.getUserName());
        }
        if (user.getPassword() != null) {
            updateWrapper.set(User::getPassword, user.getPassword());
        }
        if (user.getEmail() != null) {
            updateWrapper.set(User::getEmail, user.getEmail());
        }
        if (user.getPhone() != null) {
            updateWrapper.set(User::getPhone, user.getPhone());
        }
        if (user.getAvatar() != null) {
            updateWrapper.set(User::getAvatar, user.getAvatar());
        }
        if (user.getSignature() != null) {
            updateWrapper.set(User::getSignature, user.getSignature());
        }
        if (user.getGender() != null) {
            updateWrapper.set(User::getGender, user.getGender());
        }
        if (user.getStatus() != null) {
            updateWrapper.set(User::getStatus, user.getStatus());
        }

        updateWrapper.eq(User::getUserId, user.getUserId());
        return this.update(updateWrapper);
    }

}