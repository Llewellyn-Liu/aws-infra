package com.lrl.liustationspring.service;

import com.lrl.liustationspring.dao.mapper.UserMapper;
import com.lrl.liustationspring.dao.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class DataManipulationService {

    private Logger logger = LoggerFactory.getLogger(DataManipulationService.class);

    private static DataManipulationService instance = new DataManipulationService();

    public User getUserById(int id) {

        SqlSession session = SqlConnection.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.selectUserById(id);

        if(user == null) return null;
        else return user;
    }

    public boolean verifyByToken(String token){
        SqlSession session = SqlConnection.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        User resultFromDatabase = mapper.getUserByToken(token);
        return resultFromDatabase != null;
    }

    public boolean verify(String username, String password){

//        logger.info("Verifying user: username -> "+ username + ", password -> "+password);
//
//        SqlSession session = SqlConnection.getSession();
//        UserMapper mapper = session.getMapper(UserMapper.class);
//        User user = mapper.getUserByUsername(username);
//        String encodedPassword = user.getPassword();
//
//        return BCrypt.checkpw(password, encodedPassword);
        logger.info("username:"+username+", pswd:"+password);
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.getUserByUsername(username);
        String passwordEncoded = user.getPassword();

        logger.info("Password: "+password+", passwordEncoded: "+passwordEncoded);
        logger.info("Verification: "+BCrypt.checkpw(password, passwordEncoded) );

        return BCrypt.checkpw(password, passwordEncoded);

    }

    public int getIdByUsername(String username){
        logger.info("username:"+username);
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.getUserByUsername(username);
        return user.getId();
    }

    public User getUserByUsername(String username){
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        User user = mapper.getUserByUsername(username);

        if(null != user) return user;
        else{
            logger.info("User missing when fetch user data.");
            return null;
        }
    }

    public int updateUser(User user){
        logger.info("user: "+user.toString());
        SqlSession session = SqlConnection.getSession();

        UserMapper mapper = session.getMapper(UserMapper.class);

        return mapper.updateUser(user);
    }

    public static DataManipulationService getInstance() {
        return instance;
    }
}
