package com.lrl.liustationspring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.dao.pojo.userREST.*;
import com.lrl.liustationspring.service.SqlConnection;
import com.lrl.liustationspring.dao.mapper.ProductMapper;
import com.lrl.liustationspring.dao.pojo.Product;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
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
        IdREST id = new IdREST(1, "true", "false");
        UsernameREST un = new UsernameREST("un", true, false);
        FirstNameREST fn = new FirstNameREST("fn", true, false);
        LastNameREST ln = new LastNameREST("ln", false, true);
        PasswordREST ps = new PasswordREST("123", false, true);
        AccountCreatedREST ac = new AccountCreatedREST(new Timestamp(System.currentTimeMillis()), true,false);
        AccountLastModifiedREST lm = new AccountLastModifiedREST(new Timestamp(System.currentTimeMillis()), true,false);
        TokenREST tk = new TokenREST("bbb", false, true);
        UserREST user = new UserREST(id, un, fn, ln, ps, ac, lm, tk);

        ObjectMapper objectMapper = new ObjectMapper();
        String output = objectMapper.writeValueAsString(user);
        System.out.println(output);
        JsonNode jn = objectMapper.readTree(output);

        System.out.println("text: "+ jn.get("firstname").get("value").asText());
        System.out.println("str: "+ jn.get("firstname").get("value").toString());
        System.out.println("textv: "+ jn.get("firstname").get("value").textValue());

        System.out.println(readFromJson(output).toString());
        System.out.println(readFromJson(output));


    }

    public UserREST readFromJson(String json){

        logger.info("input :" + json);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jnode  = null;
        try {
            jnode = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            logger.info("Parse failed form json to UserREST");
            throw new RuntimeException(e);
        }


        UserREST user;
        try {
            logger.info(jnode.get("id").toString());
            IdREST id = objectMapper.readValue(jnode.get("id").toString(), IdREST.class);
            logger.info(jnode.get("username").toString());
            UsernameREST username = objectMapper.readValue(jnode.get("username").toString(), UsernameREST.class);
            logger.info(jnode.get("lastname").toString());
            FirstNameREST firstName = objectMapper.readValue(jnode.get("firstname").toString(), FirstNameREST.class);
            logger.info(jnode.get("firstname").toString());
            LastNameREST lastName = objectMapper.readValue(jnode.get("lastname").toString(), LastNameREST.class);
            logger.info(jnode.get("password").toString());
            PasswordREST password = objectMapper.readValue(jnode.get("password").toString(), PasswordREST.class);
            logger.info(jnode.get("accountCreated").toString());
            AccountCreatedREST accountCreated = objectMapper.readValue(jnode.get("accountCreated").toString(), AccountCreatedREST.class);
            logger.info(jnode.get("accountLastModified").toString());
            AccountLastModifiedREST accountLastModified = objectMapper.readValue(jnode.get("accountLastModified").toString(), AccountLastModifiedREST.class);
            TokenREST token = objectMapper.readValue(jnode.get("token").toString(), TokenREST.class);








            user = new UserREST(id, username, firstName, lastName, password, accountCreated, accountLastModified, token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info("Read from json: "+ user.toString());
        logger.info("Read from json: "+ user.getUsername().toString());

        return user;
    }
}
