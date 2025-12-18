package com.tiv.im.study.auth.utils;

import java.util.Random;

public class NicknameGenerator {

    static String[] adjectives = {"粘人的", "聪明的", "可爱的", "呆呆的", "睡着的", "勇敢的", "懒散的", "活泼的", "温柔的", "狡猾的", "快乐的", "笨拙的"};

    static String[] animals = {"小猫", "狗狗", "兔子", "熊猫", "老虎", "狮子", "长颈鹿", "大象", "企鹅", "吗喽", "乌鸦", "牛马"};

    public static String generateNickname() {
        Random random = new Random();
        String adj = adjectives[random.nextInt(adjectives.length)];
        String animal = animals[random.nextInt(animals.length)];
        return adj + animal;
    }

}
