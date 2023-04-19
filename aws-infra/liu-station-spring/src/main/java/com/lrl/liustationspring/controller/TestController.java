package com.lrl.liustationspring.controller;

import com.lrl.liustationspring.dao.pojo.Image;
import com.timgroup.statsd.StatsDClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    private StatsDClient statsDClient;

    @RequestMapping(value = "/test/imgs", method = RequestMethod.POST)
    public void imageListUploadTest(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Image> images){
        for(Image img: images){
            System.out.println(img.toString());
        }
    }

    @RequestMapping(value = "/healthz", method = RequestMethod.GET)
    public String healthTest(HttpServletRequest request, HttpServletResponse response){
        statsDClient.incrementCounter("TestController.healthz.GET");
        return "Hello "+System.currentTimeMillis();
    }

}
