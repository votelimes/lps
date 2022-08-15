package com.votelimes.lps.controller.manager;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
import com.votelimes.lps.repo.dao.SupplementedUserDao;
import com.votelimes.lps.service.DataService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Collectors;

@Controller
public class ManagerApplicationController {

    @Autowired
    DataService ds;

    @Autowired
    CreditApplicationDao creditApplicationDao;

    @Autowired
    SupplementedUserDao supplementedUserDao;


    @RequestMapping(value = "/manager/client/application", method = RequestMethod.GET)
    public String onManagingClientApplicationGet(Model model, @RequestParam(required=false) String loan_id, RedirectAttributes ra) {
        String applicationUUID = (String) model.getAttribute("loan_id");
        SupplementedUser su = null;
        try {
            if(applicationUUID == null){
                applicationUUID = loan_id;
            }
            su = supplementedUserDao.getSupplementedUserByUUID(applicationUUID);
        } catch (NoResultException e) {
            e.printStackTrace();
            return "manager/client/application";
        }

        String finalApplicationUUID = applicationUUID;
        CreditApplication ca = su.getCreditApplications()
                .stream()
                .filter(item -> item.getUuid().equals(finalApplicationUUID)).collect(Collectors.toList()).get(0);

        model.addAttribute("passport", su.getPassports().get(0));
        model.addAttribute("creditApplication", ca);
        model.addAttribute("jobsList", su.getJobPlaces());
        model.addAttribute("isSignPossible", ca.getLoanState() == LoanState.approved);

        if(ca.getLoanState().toInt() > 1){
            model.addAttribute("isShowPossible", true);
        }

        return "manager/client/application";
    }


    @RequestMapping(value = "/manager/client/application", method = RequestMethod.POST)
    public RedirectView onManagingClientApplicationPost(@RequestBody(required = false) MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes) {
        if(formData.containsKey("showOffer")){
            redirectAttributes.addAttribute("loan_id", formData.getFirst("uuid"));
            redirectAttributes.addFlashAttribute("loan_id", formData.getFirst("uuid"));
            redirectAttributes.addAttribute("signOffer", false);
            redirectAttributes.addFlashAttribute("signOffer", false);

            RedirectView rv = new RedirectView("/offer", true);
            return rv;
        }

        int order = Integer.parseInt(formData.getFirst("loan_state"));
        LoanState state = LoanState.getInOrder(order);
        BigDecimal amount = new BigDecimal(formData.getFirst("approved_amount"));
        int repaymentTime = Integer.parseInt(formData.getFirst("repayment_time"));
        int id = Integer.parseInt(formData.getFirst("loan_id"));

        creditApplicationDao.updateStateAmountTime(state, amount, repaymentTime, id);

        redirectAttributes.addAttribute("loan_id", formData.getFirst("loan_uuid"));
        redirectAttributes.addFlashAttribute("loan_id", formData.getFirst("loan_uuid"));
        redirectAttributes.addAttribute("edited", true);
        redirectAttributes.addFlashAttribute("edited", true);
        redirectAttributes.addFlashAttribute("messageText", "Изменения сохранены");
        redirectAttributes.addFlashAttribute("message", true);

        return new RedirectView("application");
    }
}
