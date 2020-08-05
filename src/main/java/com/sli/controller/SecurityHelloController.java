package com.sli.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class SecurityHelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @RequestMapping(value = "/root/1", method = RequestMethod.GET)
    public String root(HttpServletResponse response) {
        return "root";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/user/1", method = RequestMethod.GET)
    public String user(HttpServletResponse response) {
        return "user";
    }
}
