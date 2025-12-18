package com.tiv.im.study.auth.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {

    SUCCESS(200, "OK"),
    REGISTER_ERROR(40001, "注册失败,用户已存在"),
    CODE_ERROR(40002, "验证码错误"),
    LOGIN_ERROR(40003, "登录失败,用户名或密码错误"),
    NO_USER_ERROR(40004, "用户不存在"),
    NOT_LOGIN_ERROR(40005, "未登录"),
    INVALID_REQUEST_SOURCE_ERROR(40006, "非法请求来源"),
    INVALID_PARAM_ERROR(40007, "非法参数"),

    SYSTEM_ERROR(50000, "系统内部异常"),
    SEND_CODE_ERROR(50001, "发送验证码失败"),
    SAVE_USER_ERROR(50002, "数据库异常,保存用户失败"),
    UPDATE_USER_ERROR(50003, "数据库异常,更新用户失败"),
    UPLOAD_AVATAR_ERROR(50003, "更新头像失败");

    private final int code;

    private final String msg;

}
