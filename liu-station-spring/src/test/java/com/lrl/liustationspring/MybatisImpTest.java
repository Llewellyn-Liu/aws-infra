package com.lrl.liustationspring;

import com.lrl.liustationspring.service.SqlConnection;
import com.lrl.liustationspring.dao.mapper.ProductMapper;
import com.lrl.liustationspring.dao.pojo.Product;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;


public class MybatisImpTest {

    Logger logger = LoggerFactory.getLogger(MybatisImpTest.class);
    @Test
    public void testMybatis() throws IOException {
//        String password = "990213";
////        String anotherPswd = "990213";
////
////        User user = DataManipulationService.getInstance().getUserById(13);
////        System.out.println(user.toString());
////        System.out.println(BCrypt.checkpw("000000","$2a$10$yzZcnJMECTuuGz8oMzTxLewWWt2SHZ3/TdGmZb50BGLkHNq5Z9pOq"));
        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        System.out.println("a@bb.com".matches(REGEX));
        System.out.println("a@bb.".matches(REGEX));
        System.out.println("a@bb.c".matches(REGEX));
        System.out.println("@bb.".matches(REGEX));
        System.out.println("a@.".matches(REGEX));
        System.out.println("a@bb.net".matches(REGEX));

    }
}
