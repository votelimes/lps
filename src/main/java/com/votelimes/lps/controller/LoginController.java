package com.votelimes.lps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

// Логин менеджера
@Controller
public class LoginController {
    // Показ страницы логина
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showLoginPage(Model model,@RequestParam(required=false) boolean error){
        model.addAttribute("error", error);
        return "login";
    }

    // Обработка Post авторизации
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RedirectView onLogin(Model model, @RequestBody MultiValueMap<String, String> formData, RedirectAttributes ra){
        return new RedirectView("login");
    }
}
