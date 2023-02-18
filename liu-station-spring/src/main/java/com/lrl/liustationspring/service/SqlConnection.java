package com.lrl.liustationspring.service;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = sqlSessionFactoryBuilder.build(input);

        SqlSession session = factory.openSession(true);
        return session;
    }
}
