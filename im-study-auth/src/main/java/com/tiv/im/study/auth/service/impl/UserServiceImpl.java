package com.tiv.im.study.auth.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.im.study.gateway.common.CodeEnum;
import com.tiv.im.study.auth.constants.AuthConstants;
import com.tiv.im.study.auth.data.user.login.LoginByCodeRequest;
import com.tiv.im.study.auth.data.user.login.LoginRequest;
import com.tiv.im.study.auth.data.user.login.LoginResponse;
import com.tiv.im.study.auth.data.user.register.RegisterRequest;
import com.tiv.im.study.auth.data.user.register.RegisterResponse;
import com.tiv.im.study.auth.data.user.update.UpdateUserInfoRequest;
import com.tiv.im.study.gateway.exception.GlobalException;
import com.tiv.im.study.auth.mapper.UserMapper;
import com.tiv.im.study.auth.model.User;
import com.tiv.im.study.auth.service.UserService;
import com.tiv.im.study.gateway.utils.JwtUtil;
import com.tiv.im.study.auth.utils.NicknameGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        String phone = request.getPhone();
        String password = request.getPassword();

        // 判断用户是否已注册
        if (isRegistered(phone)) {
            throw new GlobalException(CodeEnum.REGISTER_ERROR);
        }

        // 查询redis中验证码
        String verifyCode = stringRedisTemplate.opsForValue().get(AuthConstants.USER_PREFIX + phone);
        if (verifyCode == null || !verifyCode.equals(request.getCode())) {
            throw new GlobalException(CodeEnum.CODE_ERROR);
        }

        // 雪花算法
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);

        long userId = snowflake.nextId();
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());

        User user = User.builder()
                .userId(userId)
                .password(encryptedPassword)
                .phone(phone)
                .userName(NicknameGenerator.generateNickname())
                .build();
        boolean saved = this.save(user);
        if (!saved) {
            throw new GlobalException(CodeEnum.SAVE_USER_ERROR);
        }
        stringRedisTemplate.delete(AuthConstants.USER_PREFIX + phone);

        return new RegisterResponse(userId);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", request.getPhone());
        User user = this.getOnly(queryWrapper, true);
        String password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (user == null || !password.equals(user.getPassword())) {
            throw new GlobalException(CodeEnum.LOGIN_ERROR);
        }
        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(user, response);
        String token = JwtUtil.generate(String.valueOf(user.getUserId()));
        response.setToken(token);
        return response;
    }

    @Override
    public LoginResponse loginByCode(LoginByCodeRequest request) {
        String verifyCode = stringRedisTemplate.opsForValue().get(AuthConstants.USER_PREFIX + request.getPhone());
        if (verifyCode == null || !verifyCode.equals(request.getCode())) {
            throw new GlobalException(CodeEnum.CODE_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", request.getPhone());
        User user = this.getOnly(queryWrapper, true);
        if (user == null) {
            throw new GlobalException(CodeEnum.NO_USER_ERROR);
        }
        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(user, response);
        String token = JwtUtil.generate(String.valueOf(user.getUserId()));
        response.setToken(token);
        stringRedisTemplate.delete(AuthConstants.USER_PREFIX + request.getPhone());

        return response;
    }

    @Override
    public LoginResponse updateUserInfo(Long userId, UpdateUserInfoRequest request) {

        User userToUpdate = new User();
        BeanUtils.copyProperties(request, userToUpdate);
        userToUpdate.setUserId(userId);

        boolean updated = this.updateSelective(userToUpdate);
        if (!updated) {
            throw new GlobalException(CodeEnum.UPDATE_USER_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        User user = this.getOnly(queryWrapper, true);
        if (user == null) {
            throw new GlobalException(CodeEnum.NO_USER_ERROR);
        }

        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(user, response);

        return response;
    }

    private boolean isRegistered(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        long count = this.count(queryWrapper);
        return count > 0;
    }

}