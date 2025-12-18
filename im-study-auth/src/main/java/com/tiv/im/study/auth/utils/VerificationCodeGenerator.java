package com.tiv.im.study.auth.utils;

import java.util.Random;

public class VerificationCodeGenerator {

    private static final Random random = new Random();

    public static String getRandomNum() {
        int num = random.nextInt(900000) + 100000;
        return String.format("%06d", num);
    }

}