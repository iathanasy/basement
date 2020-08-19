package com.eyxyt.basement.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64工具
 * @author cd.wang
 * @create 2020-07-15 15:55
 * @since 1.0.0
 */
public class Base64Utils {

    /**
     * 加密 JDK1.8
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 解密 JDK1.8
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(encode("Api@Login(Auth}"));
        System.out.println(encode("Admin@Login(Auth}"));
        System.out.println(decode("QXBpQExvZ2luKEF1dGh9"));
    }
}
