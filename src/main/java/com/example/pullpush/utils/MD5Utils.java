package com.example.pullpush.utils;

import com.google.common.base.Objects;

import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/22 10:49
 * description:
 */
public class MD5Utils {

    /**
     * 生成Token
     *
     * @return String
     */
    public static String generateToken(String tokenKey, long currentTime) {
        // 获取 10 为毫秒值
        String keys = tokenKey + currentTime + tokenKey;
        return MD5(keys);
    }

    /**
     * MD5加密
     *
     * @param s 字符串
     * @return String
     */
    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String timestamp = "1548397220280";
        String user = "ylzadmin";
        String pwd = "123456";
        String callbackaddress = "http://www.baidu.com";
        String privateKey = "8AF3A9FC577E160EB02099DEC1D1DCE0";
        String identity = "4FD7E1A84582A84214669DE0E2FBAB52";
        System.out.println(MD5Utils.MD5(privateKey + timestamp + user + pwd));
        System.out.println(MD5Utils.MD5(privateKey + timestamp + user + callbackaddress));
        String s = privateKey + timestamp + user + identity;

        System.out.println(MD5Utils.MD5(s));
        String x = "8AF3A9FC577E160EB02099DEC1D1DCE02019-01-23129118553044FD7E1A84582A84214669DE0E2FBAB52";
        System.out.println(MD5Utils.MD5(x));


        System.out.println("s = " + s);
        System.out.println("x = " + x);
        System.out.println(Objects.equal(s,x));


    }

}
