package com.lrl.liustationspring.service;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a costumed class which encapsulates the basic steps to connect the dao and return a session to
 * commit transactions.
 *
 * Auto commit enabled.
 */
public class SqlConnection {


    /**
     * If cannot read a qualified configuration, the method will return a null for session.
     * @return
     */
    public static SqlSession getSession(){
        InputStream input = null;
        try {
            input = Resources.getResourceAsStream("mybatis-configuration.xml");


            //Redefined input
            //Retrieving Env Var from system.
            String newStr = new String(input.readAllBytes());
            String db_drive = System.getenv("DB_DRIVE");
            String db_url = System.getenv("DB_URL");
            String db_username = System.getenv("DB_USERNAME");
            String db_password = System.getenv("DB_PASSWORD");

//            newStr = newStr.replace("${drive}", db_drive)
//                    .replace("${url}", db_url)
//                    .replace("${username}", db_username)
//                    .replace("${password}", db_password);

            newStr = newStr.replace("${drive}", "com.mysql.cj.jdbc.Driver")
                    .replace("${url}", "jdbc:mysql://localhost:3306/TestDev?allowMultiQueries=true")
                    .replace("${username}", "root")
                    .replace("${password}", "Lrl@990213");

            input = new ByteArrayInputStream(newStr.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = sqlSessionFactoryBuilder.build(input);

/**
 *
 */

        SqlSession session = factory.openSession(true);
        return session;
    }
}
