package com.votelimes.lps.controller.manager;

import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.SupplementedUserQueryBuilder;
import com.votelimes.lps.repo.dao.SupplementedUserDao;
import com.votelimes.lps.repo.dao.UserDao;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Список клиентов
@Controller
public class ManagerController {
    @Autowired
    SupplementedUserDao supplementedUserDao;

    @Autowired
    UserDao userDao;

    // Отображение окна
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String showPage(Model model){
        return "/manager/index";
    }
    // Обработка поискового запроса, переход к детальному просмотру клиента
    @RequestMapping(value = "/manager", method = RequestMethod.POST)
    public RedirectView onUserSelected(Model model, @RequestBody MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes){
        if(formData.containsKey("s")){
            List<SupplementedUser> users = null;
            String searchQuery = formData.getFirst("search_field");
            int searchMode = Integer.parseInt(formData.getFirst("search_mode"));
            int selectedStateInt = Integer.parseInt(formData.getFirst("search_status"));
            LoanState selectedState = LoanState.getInOrder(selectedStateInt);
            SupplementedUserQueryBuilder builder = supplementedUserDao.builder();
            try {
                switch (searchMode) {
                    case 0: {
                        builder.setFullName(searchQuery);
                        break;
                    }
                    case 1: {
                        builder.setContactNumber(searchQuery);
                        break;
                    }
                    case 2: {
                        builder.setPassportSeriesAndId(searchQuery);
                        break;
                    }
                    default:
                        users = new ArrayList<>();
                }
                users = builder
                        .setLoanState(selectedState)
                        .buildAndExecuteGetByFullNameAndStateAndNumberAndPassport();
            }
            catch (NoResultException e){
                redirectAttributes.addFlashAttribute("noResult", true);
                redirectAttributes.addFlashAttribute("errorMessage", "Пользователей с введенными данными не сущестует.");
            }
            if(users.size() == 0){
                redirectAttributes.addFlashAttribute("noResult", true);
                redirectAttributes.addFlashAttribute("errorMessage", "Пользователей с введенными данными не сущестует.");
            }
            //redirectAttributes.addAttribute("users", users);
            redirectAttributes.addFlashAttribute("users", users);
            redirectAttributes.addFlashAttribute("search_field", searchQuery);

            redirectAttributes.addFlashAttribute("search_mode", String.valueOf(searchMode));

            redirectAttributes.addFlashAttribute("search_status", String.valueOf(selectedStateInt));

            return new RedirectView("/manager", true);
        }

        redirectAttributes.addAttribute("user_id", formData.get("user_id"));
        redirectAttributes.addFlashAttribute("user_id", formData.get("user_id"));


        return new RedirectView("manager/client");
    }
}
