package com.lrl.liustationspring.controller;


import com.lrl.liustationspring.controller.tools.BCryptor;
import com.lrl.liustationspring.dao.pojo.User;
import com.lrl.liustationspring.dao.pojo.UserForResponse;
import com.lrl.liustationspring.service.DataManipulationService;
import com.lrl.liustationspring.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;

@RestController
public class RESTController {

    /**
     * Feature: if it's too long since the first request, the request FAIL.
     */
    private static HashMap<String, Long> authorizedUserMenu = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(RESTController.class);

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public void securityCheck(HttpServletRequest request, HttpServletResponse response) {

        String attr = request.getHeader("Authorization");
        if (null == attr) {
            logger.info("No Authorization header received.");
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.addHeader("WWW-Authenticate", "Basic realm=\"Protect Space\"");
        } else {

            if (authentication(request)) {
                try {
                    String[] authValue = attr.split(" ");
                    String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");
                    int id = DataManipulationService.getInstance().getIdByUsername(decodedToken[0]);
                    response.sendRedirect("/data/user/" + id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                response.setStatus(403);
            }
        }


//        try {
//            response.sendRedirect("/error.html");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    @RequestMapping(value = "/data/user/{id}", method = RequestMethod.GET)
    private UserForResponse userGetter(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
        User user = DataManipulationService.getInstance().getUserById(id);
        if (user == null) {
            logger.info("No id found in database.");
            response.setStatus(403);
            return null;
        }

        if(request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return null;
        }

        if (authentication(request)) return user.parseFormat();
        else {
            logger.info("authentication failed.");
            response.setStatus(403);
            return null;
        }
    }

    @RequestMapping(value = "data/user", method = RequestMethod.POST)
    public UserForResponse userCreator(@RequestParam(value = "firstname") String firstname,
                                       @RequestParam(value = "lastname") String lastname,
                                       @RequestParam(value = "username", defaultValue = "NotAUser") String username,
                                       @RequestParam(value = "password", defaultValue = "NotAPassword") String password,
                                       HttpServletResponse response) {


        logger.info("Register application received: username = " + username + ", firstname = " + firstname
                + ", lastname = " + lastname + ",token = " + password);

        Timestamp currentTimeDateFormat = new Timestamp(System.currentTimeMillis());

        if (!RegisterService.getInstance().usernameValidation(username)) {
            response.setStatus(400);
            return new User(null, firstname, lastname, "N/A", "password",
                    currentTimeDateFormat, currentTimeDateFormat, "R failed - Invalid name").parseFormat();
        }

        if (!RegisterService.getInstance().passwordValidation(password)) {
            response.setStatus(400);
            return new User(null, firstname, lastname, username, "N/A",
                    currentTimeDateFormat, currentTimeDateFormat, "R failed - Invalid password").parseFormat();
        }

        //BCrypt encryption
        String salt = BCryptor.generateSalt();
        String passwordHash = BCrypt.hashpw(password, salt);

        User returnValue = new User(null, firstname, lastname, username, passwordHash,
                currentTimeDateFormat, currentTimeDateFormat, "Registered");

        RegisterService.getInstance().register(returnValue);
        return RegisterService.getInstance().getRegisteredUser(username).parseFormat();
    }

    @RequestMapping(value = "/data/user/{id}", method = RequestMethod.PUT)
    public UserForResponse userUpdater(@PathVariable int id, @RequestParam(value = "firstname") String firstname, @RequestParam(value = "lastname") String lastname,
                                       @RequestParam(value = "password") String password, @RequestParam(value = "username") String username, HttpServletRequest request, HttpServletResponse response) {


        User user = DataManipulationService.getInstance().getUserById(id);

        //If cannot find user, return 400 status
        if (null == user) {
            response.setStatus(400);
            logger.info("Cannot find user in database.");
            return null;
        }

        if(request.getHeader("Authorization") == null) {
            response.setStatus(401);
            return null;
        }

        //If authentication fails, return null
        if (!authentication(request)) {
            logger.info("WWW-Authenticate verification failed");
            response.setStatus(400);
            return null;
        }

        //Check if the ids match and if user try to modify username, true to return 400 status;
        logger.info("Authenticating if happens to the owner: Sent id:" + id + ", request id:" + user.getId() + " result:" + user.getUsername().equals(username));
        if (!user.getUsername().equals(username)) {
            logger.info("User try to modify username.");
            response.setStatus(403);
        }



        //

        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPassword(BCrypt.hashpw(password, BCryptor.generateSalt()));
        user.setAccountLastModified(new Timestamp(System.currentTimeMillis()));

        DataManipulationService.getInstance().updateUser(user);
        return DataManipulationService.getInstance().getUserById(user.getId()).parseFormat();
    }

//
//    @RequestMapping(value = "/v1/user", method = RequestMethod.GET)
//    public User retrieveUserInfo(@RequestParam(value = "id") String id) {
//        logger.info("User id: " + id);
//        return new DataManipulationService().getUserById(Integer.parseInt(id));
//    }
//
//    @RequestMapping(value = "/v1/user/*", method = RequestMethod.GET)
//    public User retrieveUserInfoFromUrl(@RequestParam(value = "id") String id) {
//        logger.info("User id: " + id);
//        return new DataManipulationService().getUserById(Integer.parseInt(id));
//    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public UserForResponse userTester(HttpServletRequest request){
        User user = new User(1, "test", "testLN", "testFN", "testpswd", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),"abc");
        logger.info("Called Tester.");

        return user.parseFormat();
    }

    /**
     * Tool method - verifyHealth: Giving each user a fixed span to retrieve data.
     * Any request made too long from the first request fail.
     *
     * @return true: still available
     */
    private boolean authentication(HttpServletRequest request) {
        String attr = request.getHeader("Authorization");
        if (attr == null) return false;

        logger.info("Authenticating: WWW-Authenticate field perceived: " + attr);
        String[] authValue = attr.split(" ");
        String[] decodedToken = new String(Base64.getDecoder().decode(authValue[1])).split(":");
        logger.info("Authenticating: Name: " + decodedToken[0] + "Pswd: " + decodedToken[1]);

        User user = DataManipulationService.getInstance().getUserByUsername(decodedToken[0]);
        if(user == null) return false;

        return BCrypt.checkpw(decodedToken[1], user.getPassword());
    }
}
