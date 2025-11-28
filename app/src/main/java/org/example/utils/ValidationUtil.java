package org.example.utils;

import org.example.model.UserInfoDto;

public class ValidationUtil {

    public static Boolean validateUserAttributes(UserInfoDto userInfoDto) {
        String email = userInfoDto.getEmail();
        String password = userInfoDto.getPassword();
        if(password.length() < 8 || email.isEmpty() || password.isEmpty() || !email.contains("@") || password.contains(".")) {
            return false;
        }
        return true;
    }
}
