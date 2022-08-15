package com.votelimes.lps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {
    @RequestMapping(value = "/webserviceUnavailable", method = RequestMethod.POST)
    public String serviceUnavailable(@RequestBody MultiValueMap<String, String> fd) {
        return "webserviceUnavailable";
    }

}
