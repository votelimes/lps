package com.votelimes.lps.controller.manager;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.Passport;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
import com.votelimes.lps.repo.dao.PassportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserRequestListController {
    @Autowired
    CreditApplicationDao creditApplicationDao;

    @Autowired
    PassportDao passportDao;

    @RequestMapping(value = "/manager/client", method = RequestMethod.GET)
    public String showUserRequests(Model model, RedirectAttributes redirectAttributes){
        try {
            int userId = Integer.parseInt(((List<String>) model.getAttribute("user_id")).get(0));
            Passport passport = passportDao.getByUserId(userId).get(0);

            List<CreditApplication> applications = creditApplicationDao.getByUser(userId);


            model.addAttribute("applications", applications);
            model.addAttribute("passport", passport);
            return "/manager/client/index";
        }
        catch (NullPointerException e){
            return "redirect:/manager";
        }
        catch (Exception e){
            return "redirect:/webserviceUnavailable";
        }
    }
    @RequestMapping(value = "/manager/client", method = RequestMethod.POST)
    public RedirectView onRequestSelect(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes){
        String uuid = formData.getFirst("loan_id");
        redirectAttributes.addAttribute("loan_id", uuid);
        redirectAttributes.addFlashAttribute("loan_id", uuid);
        return new RedirectView("client/application");
    }
}
