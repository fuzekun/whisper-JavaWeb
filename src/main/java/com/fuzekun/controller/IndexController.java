package com.fuzekun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.PanelUI;

/**
 * @author: Zekun Fu
 * @date: 2024/4/25 23:36
 * @Description:
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello, Are you ready to change?";
    }


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "pages/index";
    }

}
