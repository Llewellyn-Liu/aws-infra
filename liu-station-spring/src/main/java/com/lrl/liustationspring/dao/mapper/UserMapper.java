package com.lrl.liustationspring.dao.mapper;

import com.lrl.liustationspring.dao.pojo.User;

import java.util.List;

public interface UserMapper {

    /**
     * Add a user
     */

    int insertRegisterUser(User user);

    User selectUserById(int id);

    List<User> selectUsers();

    List<User> selectUsersByName(String username);

    /**
     * Deprecated way of authentication.
     * Use verifyToken instead
     * @param username
     * @param password Also token, but in password disguise.
     * @return
     */
    List<User> verify(String username, String password);

    User getUserByToken(String token);

    User getUserByUsername(String username);

    int updateUser(User user);
}
