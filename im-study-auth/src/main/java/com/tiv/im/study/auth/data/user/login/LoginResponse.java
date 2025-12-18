package com.tiv.im.study.auth.data.user.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 性别:0-男,1-女,2-保密
     */
    private Integer gender;

    /**
     * 状态:1-正常,2-封号,3-注销
     */
    private Integer status;

    /**
     * 登录token
     */
    private String token;

}
