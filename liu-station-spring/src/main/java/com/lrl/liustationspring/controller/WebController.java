package com.lrl.liustationspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;

/**
 * This controller solves every communication related exception.
 */
@Controller
public class WebController {

    Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/error")
    public void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response){

        logger.info("Error happened when open link "+request.getRequestURL()+". Default redirection for /error to error.html");
        try {
            Thread.sleep(2000);
            response.sendRedirect("/error.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/data/healthz")
    public void healthCheck(HttpServletResponse response){
        logger.info("health check triggered.");
        response.setStatus(200);
    }

}
