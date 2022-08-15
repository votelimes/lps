package com.votelimes.lps.controller;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
import com.votelimes.lps.repo.dao.SupplementedUserDao;
import com.votelimes.lps.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.NoResultException;
import java.util.stream.Collectors;

@Controller
public class CreditStatusController {

    @Autowired
    DataService ds;

    @Autowired
    SupplementedUserDao supplementedUserDao;

    @Autowired
    CreditApplicationDao creditApplicationDao;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String showPage(Model model, RedirectAttributes redirectAttributes) {
        String applicationUUID = (String) model.getAttribute("loan_id");
        SupplementedUser su = null;
        try {

            su = supplementedUserDao.getSupplementedUserByUUID(applicationUUID);
        } catch (NoResultException e) {
            e.printStackTrace();
            return "statusNotFound";
        }

        String finalApplicationUUID = applicationUUID;
        CreditApplication ca = su.getCreditApplications()
                .stream()
                .filter(item -> item.getUuid().equals(finalApplicationUUID)).collect(Collectors.toList()).get(0);

        model.addAttribute("passport", su.getPassports().get(0));
        model.addAttribute("creditApplication", ca);
        model.addAttribute("jobsList", su.getJobPlaces());
        model.addAttribute("isSignPossible", ca.getLoanState() == LoanState.approved);

        if(ca.getLoanState().toInt() > 2){
            model.addAttribute("isShowPossible", true);
        }

        return "status";
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public RedirectView onUserSign(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes) {
        String uuid = formData.get("uuid").get(0);
        redirectAttributes.addAttribute("loan_id", uuid);
        redirectAttributes.addFlashAttribute("loan_id", uuid);
        ds.signCreditApplication(uuid);
        return new RedirectView("status");
    }

}
