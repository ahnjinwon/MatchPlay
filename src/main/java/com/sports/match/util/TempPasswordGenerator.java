package com.sports.match.util;
import java.security.SecureRandom;

public class TempPasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*";

    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;
    private static SecureRandom random = new SecureRandom();

    public static String generate() {

        StringBuilder password = new StringBuilder(16);

        // 각 종류별 문자 최소 1개씩 포함시키기
        password.append(randomChar(UPPER));
        password.append(randomChar(LOWER));
        password.append(randomChar(DIGITS));
        password.append(randomChar(SPECIAL));

        // 나머지는 랜덤 문자로 채우기
        for (int i = 0; i < 12; i++) {
            password.append(randomChar(ALL));
        }

        // 생성된 문자열을 섞기
        return shuffleString(password.toString());
    }

    private static char randomChar(String chars) {
        int idx = random.nextInt(chars.length());
        return chars.charAt(idx);
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length -1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[j];
            characters[j] = temp;
        }
        return new String(characters);
    }
}
