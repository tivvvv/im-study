package com.tiv.im.study.auth.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tiv.im.study.gateway.common.CodeEnum;
import com.tiv.im.study.auth.constants.AuthConstants;
import com.tiv.im.study.auth.context.UserContext;
import com.tiv.im.study.gateway.exception.GlobalException;
import com.tiv.im.study.auth.mapper.UserMapper;
import com.tiv.im.study.auth.model.User;
import com.tiv.im.study.gateway.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(AuthConstants.TOKEN);

        if (StringUtils.isEmpty(token)) {
            throw new GlobalException(CodeEnum.NOT_LOGIN_ERROR);
        }

        String userId = JwtUtil.parse(token).getSubject();
        if (!StrUtil.isNumeric(userId)) {
            throw new GlobalException(CodeEnum.INVALID_PARAM_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.valueOf(userId));
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new GlobalException(CodeEnum.NO_USER_ERROR);
        }

        // ThreadLocal存储userId
        UserContext.setUserId(Long.valueOf(userId));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal
        UserContext.clear();
    }

}
