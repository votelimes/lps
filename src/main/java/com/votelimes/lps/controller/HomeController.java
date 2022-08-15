package com.votelimes.lps.controller;

import java.sql.SQLException;
import java.util.Locale;

import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.User;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
import com.votelimes.lps.repo.dao.JobPlaceDao;
import com.votelimes.lps.repo.dao.SupplementedUserDao;
import com.votelimes.lps.repo.dao.UserDao;
import com.votelimes.lps.repo.impl.SessionGetter;
import com.votelimes.lps.service.AuthUtils;
import com.votelimes.lps.service.AuthenticationSystem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

@Controller
public class HomeController {
	@Autowired
	UserDao userDao;

	@Autowired
	CreditApplicationDao creditApplicationDao;

	@Autowired
	AuthUtils authUtils;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getHome(Locale locale, Model model) {
		return "index";
	}
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String getHomeIndex(Locale locale, Model model) {
		return "index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public RedirectView postHome(Model model, @RequestBody MultiValueMap<String, String> formData, RedirectAttributes redirectAttributes) {
		if(formData.get("create") != null){
			return new RedirectView("create");
		}
		else if(formData.get("view") != null){
			try {
				creditApplicationDao.getByUUID(formData.get("loan_id").get(0));
				redirectAttributes.addFlashAttribute("loan_id", formData.get("loan_id").get(0));
				return new RedirectView("status");
			} catch (NoResultException e){
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("error", true);
				redirectAttributes.addFlashAttribute("errorMessage", "Такой заявки не существует. Проверьте правильность введенного идентификатора.");
				return new RedirectView("index");
			}
		}
		else{
			return new RedirectView("index");
		}
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String onAboutSelected(){
		boolean logged = authUtils.isLogged();
		return "about";
	}


}
