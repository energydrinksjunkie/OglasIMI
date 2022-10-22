package com.example.demo;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class Security {

    public static String encryptCookie(String plain) {
        String b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());

        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < reverse.length(); i++) {
            tmp.append((char)(reverse.charAt(i) + OFFSET));
        }
        return tmp.toString();
    }

    public static String decryptCookie(String secret) {
        StringBuilder tmp = new StringBuilder();
        final int OFFSET = 4;
        for (int i = 0; i < secret.length(); i++) {
            tmp.append((char)(secret.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(tmp.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversed));
    }

    public static String generatePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean matchPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static boolean proveriCookies(int role, int id, HttpServletResponse response) {
        if (role == -1 || id == -1) {
            // create a cookie
            Cookie cookieid = new Cookie("id", encryptCookie("0"));
            Cookie cookierole = new Cookie("role", encryptCookie("0"));
            cookieid.setPath("/");
            cookierole.setPath("/");

            // add cookie to response
            response.addCookie(cookieid);
            response.addCookie(cookierole);
            return true;
        }
        return false;
    }

}
