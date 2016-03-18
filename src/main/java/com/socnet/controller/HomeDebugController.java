package com.socnet.controller;

import com.socnet.persistence.MessagePersistence;
import com.socnet.persistence.RoomPersistence;
import com.socnet.persistence.UserPersistence;
import com.socnet.persistence.asset.ImagePersistence;
import com.socnet.utility.TestDataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.apache.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class HomeDebugController {
    @Autowired
    private TestDataInitializer initializer;
    @Autowired
    private UserPersistence userPersistence;
    @Autowired
    private RoomPersistence roomPersistence;
    @Autowired
    private MessagePersistence messagePersistence;

    private Logger logger = Logger.getLogger(HomeDebugController.class);

    @RequestMapping("/")
    public String getHomePage(HttpServletRequest request) {
        logger.info("home page redirect to index.html");
        return "redirect:/index.html";
    }


    @RequestMapping("/initialize")
    public String initialize() throws IOException {
        initializer.addTestData();
        return "home";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/time")
    public String time(){
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.toLocalTime().toString();
    }

}
