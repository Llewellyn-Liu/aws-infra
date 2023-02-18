package com.lrl.liustationspring.controller;

import com.lrl.liustationspring.controller.tools.BCryptor;
import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.service.RegisterService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;

@RestController
public class RegisterController {
    Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/UserRegister", method = RequestMethod.POST)
    public User registerUser(@RequestParam(value = "firstname") String firstname,
                             @RequestParam(value = "lastname") String lastname,
                             @RequestParam(value = "username", defaultValue = "NotAUser") String username,
                             @RequestParam(value = "password", defaultValue = "NotAPassword") String password,
                             HttpServletResponse response) {
        logger.info("Register application received: username = " + username + ", firstname = " + firstname
                + ", lastname = " + lastname + ",password = " + password);

        Timestamp currentTimeDateFormat = new Timestamp(System.currentTimeMillis());

        if(!RegisterService.getInstance().usernameValidation(username)){
            response.setStatus(400);
        }

        if(!RegisterService.getInstance().passwordValidation(password)){
            response.setStatus(400);
//            return new User(null, firstname, lastname, username, "N/A",
//                    currentTimeDateFormat, currentTimeDateFormat,"R failed");
        }

        //BCrypt encryption
        String salt = BCryptor.generateSalt();
        String passwordHash = BCrypt.hashpw(password, salt);

        logger.info("Value in register: "+ password + ", " + passwordHash + "," + BCrypt.checkpw(password, passwordHash));

        User returnValue = new User(null, firstname, lastname, username, passwordHash,
                currentTimeDateFormat, currentTimeDateFormat,"Registered");
        RegisterService.getInstance().register(returnValue);
        return RegisterService.getInstance().getRegisteredUser(username);
    }

    //Test comment
}
