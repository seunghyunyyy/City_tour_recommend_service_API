package com.ssuopenpj.spring;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptionUtils {
    public static String SHA256(String id, String pw) {
        return encrypt(id, pw, "SHA-256");
    }
    public static String MD5(String id, String pw) {
        return encrypt(id, pw, "MD5");
    }

    public static String encrypt(String id, String pw, String messageDigest) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigest);

            //System.out.println("PWD + SALT 적용 전 : " + pw + id);
            md.update((pw + id).getBytes());
            byte[] pwdSalt = md.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b : pwdSalt) {
                sb.append(String.format("%02x", b));
            }
            result = sb.toString();
            //System.out.println("PWD + SALT 적용 후 : " + result);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}