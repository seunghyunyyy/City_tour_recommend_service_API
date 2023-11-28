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
    /*public static String getEncrypt(String s, String messageDigest) {
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigest);
            byte[] passBytes = s.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) sb.append(Integer.toString((digested[i]&0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }
    public String getSalt() {
        //1. Random, salt 생성
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];

        //2. 난수 생성
        sr.nextBytes(salt);

        //3. byte To String (10진수 문자열로 변경)
        StringBuffer sb = new StringBuffer();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
*/
}