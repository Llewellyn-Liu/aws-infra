package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.mapper.UserMapper;
import com.lrl.liustationspring.dao.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class LoginService {
    Logger logger = LoggerFactory.getLogger(LoginService.class);

    private static LoginService instance = new LoginService();

    public boolean loginUser(String username, String password){
        logger.info("username:"+username+", pswd:"+password);
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.getUserByUsername(username);
        String passwordEncoded = user.getPassword();

        logger.info("Password: "+password+", passwordEncoded: "+passwordEncoded);
        logger.info("Verification: "+BCrypt.checkpw(password, passwordEncoded) );

        return BCrypt.checkpw(password, passwordEncoded);
    }

    public User getUserByName(String username){
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.getUserByUsername(username);

        if(null != user) return user;
        else{
            logger.info("User missing when fetch user data.");
            return null;
        }
    }

    public static LoginService getInstance() {
        return instance;
    }

}
