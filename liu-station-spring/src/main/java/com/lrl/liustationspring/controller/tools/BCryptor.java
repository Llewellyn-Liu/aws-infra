package com.lrl.liustationspring.controller.tools;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptor {

    public static String generateSalt(){
        String salt = BCrypt.gensalt();
        return salt;
    }
}
