package com.lrl.liustationspring;

import org.apache.ibatis.io.Resources;
import org.hibernate.engine.jdbc.ReaderInputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String[] args) throws IOException {
        InputStream input = null;
        try {
            input = Resources.getResourceAsStream("mybatis-configuration.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String newStr = new String(input.readAllBytes());
        String db_drive = System.getenv("DB_DRIVE");
        String db_url = System.getenv("DB_URL");
        String db_username = System.getenv("DB_USERNAME");
        String db_password = System.getenv("DB_PASSWORD");

        newStr = newStr.replace("${drive}", db_drive)
                .replace("${url}", db_url)
                .replace("${username}", db_username)
                .replace("${password}", db_password);

        input = new ByteArrayInputStream(newStr.getBytes());
        System.out.println(new String(input.readAllBytes()));


    }
}
