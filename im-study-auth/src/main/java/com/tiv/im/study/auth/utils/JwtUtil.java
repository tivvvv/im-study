package com.tiv.im.study.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Date;

public class JwtUtil {

    private final static String JWT_SECRET_KEY = "jwt";

    private final static Duration expiration = Duration.ofDays(7);

    /**
     * 生成token
     *
     * @param userId
     * @return
     */
    public static String generate(String userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws JwtException
     */
    public static Claims parse(String token) throws JwtException {
        if (StringUtils.isEmpty(token)) {
            throw new JwtException("token不能为空");
        }

        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}