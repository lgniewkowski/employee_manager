package com.lgniewkowski.em;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimplyController
{
    @RequestMapping(method= RequestMethod.GET, name = "/home")
    public String home() {
        return "Hello World!";
    }
}
