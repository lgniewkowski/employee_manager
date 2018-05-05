package com.lgniewkowski.em.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController
{
    @RequestMapping()
    public String defaultResponse() {
        return "";
    }
}
