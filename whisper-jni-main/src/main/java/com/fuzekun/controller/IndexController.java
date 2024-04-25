package com.fuzekun.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Zekun Fu
 * @date: 2024/4/25 23:36
 * @Description:
 */
@RestController
public class IndexController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello, Are you ready to change?";
    }
}
