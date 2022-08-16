package com.votelimes.lps.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

// Вывод одного или нескольких договоров на печать
@Controller
public class CreditOfferController {

    @Autowired
    CreditApplicationDao creditApplicationDao;

    @Autowired
    PassportDao passportDao;

    // Вывод одного договора от конкретной заявки из application или status
    @RequestMapping(value = "/offer", method = RequestMethod.GET)
    public String showOfferPage(Model model, @RequestParam(required=false) String uuid, @RequestParam(required=false) boolean signOffer, @RequestParam(required=false) boolean showOffer) {
        CreditApplication ca = null;
        Passport ps = null;
        try {
            if(uuid == null){
                uuid = (String) model.getAttribute("loan_id");
            }
            ca = creditApplicationDao.getByUUID(uuid);
            ps = passportDao.getById(ca.getPassportId());
        } catch (NoResultException e){
            e.printStackTrace();
            return "webserviceUnavailable";
        }
        model.addAttribute("ca", ca);
        model.addAttribute("ps", ps);
        try {
            if (signOffer) {
                model.addAttribute("signDate", Date.valueOf(LocalDate.now()).toString());
                model.addAttribute("signOffer", signOffer);
            } else {
                model.addAttribute("signDate", ca.getSignDate().toString());
            }
        }catch (NullPointerException ignored){}

        return "offer";
    }

    // Обработка подписи договора пользователем, переход обратно в status (к детальному просмотру договора)
    @RequestMapping(value = "/offer", method = RequestMethod.POST)
    public RedirectView onUserOffer(Model model, @RequestBody MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes) {
        Object openOffer = formData.getFirst("openOffer");
        Object userSign = formData.getFirst("userSign");
        int id = -1;
        String uuid = null;
        try {
            id = Integer.valueOf(formData.get("loan_id").get(0));
            uuid = formData.get("loan_uuid").get(0);
        }catch (Exception e){
            e.printStackTrace();
            new RedirectView("webserviceUnavailable");
        }
        redirectAttributes.addFlashAttribute("loan_id", uuid);

        if(openOffer != null){
            return new RedirectView("offer");
        }
        if(userSign != null){
            try {
                creditApplicationDao.updateLoanStateAndSignDate(Date.valueOf(LocalDate.now()), LoanState.signed, id);
            }
            catch (PersistenceException e){
                e.printStackTrace();
                new RedirectView("webserviceUnavailable");
            }
        }
        return new RedirectView("status");
    }

    // Вывод нескольких договоров из LoanSearch
    @RequestMapping(value = "/manager/printOffers", method = RequestMethod.GET)
    public String showAllOffersPage(Model model) {
        List<CreditApplication> applications = null;
        if(model.containsAttribute("loanState")) {
            int loanStateOrder = (Integer) model.getAttribute("loanState");
            if(loanStateOrder < 5) {
                LoanState loanState = LoanState.getInOrder(loanStateOrder);
                try {
                    applications = creditApplicationDao.getByLoanState(loanState);
                    model.addAttribute("applications", applications);
                } catch (NoResultException ignored) {
                    model.addAttribute("error", true);
                    model.addAttribute("errorMessage", "Отсутствуют договоры для печати с указанным фильтром");
                }
            }
            else if(loanStateOrder == 6){
                try {
                    applications = creditApplicationDao.getAllCompletedAndSigned();
                    model.addAttribute("applications", applications);
                } catch (NoResultException ignored) {model.addAttribute("error", true);
                    model.addAttribute("errorMessage", "Отсутствуют договоры для печати с указанным фильтром");
                }
            }
            else{
                try {
                    applications = creditApplicationDao.getAll();
                    model.addAttribute("applications", applications);
                }
                catch (NoResultException ignored){
                    model.addAttribute("error", true);
                    model.addAttribute("errorMessage", "Отсутствуют договоры для печати");
                }
            }
        }
        if(applications.size() == 0){
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Отсутствуют договоры для печати с указанным фильтром");
        }
        return "/manager/printOffers";
    }
}
