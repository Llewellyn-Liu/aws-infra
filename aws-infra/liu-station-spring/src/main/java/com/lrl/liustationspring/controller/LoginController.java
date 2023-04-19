package com.lrl.liustationspring.controller;

import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    public User login(@RequestParam(value = "username")String username, @RequestParam("password")String password){

        logger.debug("username: "+ username +", pswd: "+password);

        boolean hasVerifiedUser = LoginService.getInstance().loginUser(username,password);
        if(hasVerifiedUser){
            return LoginService.getInstance().getUserByName(username);
        }
        else return new User(null, "~", "~", username, "~",
                new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),"Login failed");
    }

}
