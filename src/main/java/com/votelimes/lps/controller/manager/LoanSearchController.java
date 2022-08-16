package com.votelimes.lps.controller.manager;


import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
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

// Обрабатывает страницу поиска заявок
@Controller
public class LoanSearchController {

    @Autowired
    CreditApplicationDao creditApplicationDao;

    //Отображение
    @RequestMapping(value = "/manager/loanSearch", method = RequestMethod.GET)
    public String onPageShow(Model model){
        return "manager/loanSearch";
    }

    //Обработка поискового запроса
    @RequestMapping(value = "/manager/loanSearch", method = RequestMethod.POST)
    public RedirectView onLoanSearchFilter(Model model, RedirectAttributes redirectAttributes, @RequestBody MultiValueMap<String, String> formData){
        Iterable<CreditApplication> applications = null;
        String searchQuery = formData.getFirst("search_field");
        searchQuery = searchQuery == null ? "" : searchQuery;

        int selectedStateInt = Integer.parseInt(formData.getFirst("search_status"));
        LoanState selectedState = LoanState.getInOrder(selectedStateInt);

        boolean printOffers = false;
        try{
            printOffers = Boolean.parseBoolean(formData.getFirst("printOffersAction"));
        }catch (Exception ignored){}

        if(printOffers){
            if(selectedState != null) {
                redirectAttributes.addFlashAttribute("loanState", selectedState.toInt());
            }
            else{
                redirectAttributes.addFlashAttribute("loanState", selectedStateInt);
            }

           return new RedirectView("printOffers");
        }


        try {
            if(selectedStateInt != 6) {
                applications = creditApplicationDao.getByLoanStateAndFullName(selectedState, searchQuery);
            }
            else{
                applications = creditApplicationDao.getAllCompletedAndSignedByFullName(searchQuery);
            }
            redirectAttributes.addFlashAttribute("applications", applications);
        }
        catch (NoResultException e){
            redirectAttributes.addFlashAttribute("noResult", true);
            redirectAttributes.addAttribute("errorMessage", "Заявок с введенными данными не сущестует.");
        }
        return new RedirectView("loanSearch");
    }

    //Когда выбрана заявка (переход к детальному просмотру)
    @RequestMapping(value = "/manager/loanSearchSelected", method = RequestMethod.POST)
    public RedirectView onLoanSelected(Model model, RedirectAttributes redirectAttributes, @RequestBody MultiValueMap<String, String> formData){
        String uuid = formData.getFirst("loan_id");
        redirectAttributes.addAttribute("loan_id", uuid);
        redirectAttributes.addFlashAttribute("loan_id", uuid);
        return new RedirectView("client/application");
    }
}
