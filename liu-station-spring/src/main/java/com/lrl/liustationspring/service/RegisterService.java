package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.UserInfoConfig;
import com.lrl.liustationspring.dao.mapper.UserMapper;
import com.lrl.liustationspring.dao.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RegisterService {

    Logger logger = LoggerFactory.getLogger(RegisterService.class);
    private static RegisterService instance = new RegisterService();

    RegisterService() {

    }

    public static RegisterService getInstance() {
        return instance;
    }

    public int register(User user) {
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        logger.info("User added into database. Values: " + user.toString());
        int result = mapper.insertRegisterUser(user);
        logger.info("Statement conducted. Returned value: " + result);

        return result;
    }

    /**
     * Validate if the received username is lawful.
     *
     * @param username
     * @return true : able to be registered; false: not valid.
     */
    public boolean usernameValidation(String username) {

        //check if info missing or wrong format
        String REGEX="^\\w+((-\\w+)|(\\.\\w+))*@\\w+(\\.\\w{2,3}){1,3}$";
        if (username == "NotAUsername" || !username.matches(REGEX)) {
            logger.info("Not a valid username from frontend.");
            return false;
        }

        //check if username exists
        if (hasUser(username)) {
            logger.info("Username already taken.");
            return false;
        }



        //Other validation check here

        //return
        return true;
    }

    private boolean hasUser(String username) {
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        List<User> list = mapper.selectUsersByName(username);
        return !list.isEmpty();
    }

    public boolean passwordValidation(String password) {
        boolean returnValue = true;

        int passwordLength = password.length();
        returnValue = passwordLength >= UserInfoConfig.PASSWORD_LENGTH_MIN && passwordLength <= UserInfoConfig.PASSWORD_LENGTH_MAX;

        return true;
    }

    public User getRegisteredUser(String username){
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        List<User> list = mapper.selectUsersByName(username);

        return list.get(0);
    }
}
