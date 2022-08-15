package com.votelimes.lps.controller;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.JobPlace;
import com.votelimes.lps.model.Passport;
import com.votelimes.lps.model.User;
import com.votelimes.lps.model.enums.Gender;
import com.votelimes.lps.model.enums.MaritalStatus;
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Controller
public class CreditCreateController {

    @Autowired
    DataService ds;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showPage(Model model){
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RedirectView onCreateRequest(@RequestBody MultiValueMap<String, String> fd, RedirectAttributes ra){
        User usr = new User();
        Passport passport = new Passport();
        passport.setFirstName(fd.getFirst("first_name"));
        passport.setSecondName(fd.getFirst("second_name"));
        passport.setPatronymic(fd.getFirst("patronymic"));
        passport.setBirthDate(parseDate(fd.getFirst("birthdate")));
        passport.setPassportSeries(fd.getFirst("passport_serial"));
        passport.setPassportID(fd.getFirst("passport_number"));
        passport.setIssuedBy(fd.getFirst("issuedBy"));
        passport.setPassportDate(parseDate(fd.getFirst("passport_date")));
        int val = Integer.parseInt(fd.getFirst("passport_gender"));
        passport.setGender(Gender.getInOrderGender(val));
        val = Integer.parseInt(fd.getFirst("passport_marital"));
        passport.setMaritalStatus(MaritalStatus.getInOrderMaritalStatus(val));
        passport.setRegistrationCountry(fd.getFirst("passport_country"));
        passport.setRegistrationRegion(fd.getFirst("passport_region"));
        passport.setRegistrationCity(fd.getFirst("passport_city"));
        passport.setRegistrationStreet(fd.getFirst("passport_street"));
        passport.setRegistrationHouse(fd.getFirst("passport_house"));
        passport.setRegistrationApps(fd.getFirst("passport_apps"));

        CreditApplication ca = new CreditApplication();
        ca.setContactNumber(fd.getFirst("contactNumber"));
        try {
            ca.setDesiredAmount(new BigDecimal(fd.getFirst("desired_amount")));
        }
        catch (Exception ignored){}
        JobPlace jp = new JobPlace();
        jp.setCompanyName(fd.getFirst("company_name"));
        jp.setJobTitle(fd.getFirst("company_job"));
        jp.setDateStart(parseDate(fd.getFirst("company_date_start")));
        try {
            jp.setDateEnd(parseDate(fd.getFirst("company_date_end")));
        }
        catch (Exception ignored){}
        try {
            jp.setSalary(Integer.parseInt(fd.getFirst("company_salary")));
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            jp.setSalary(0);
        }

        String uuid = null;
        try {
            uuid = ds.insertNewLoan(usr, passport, ca, jp);
        } catch (SQLException e) {
            e.printStackTrace();
            return new RedirectView("webserviceUnavailable");
        }
        ra.addAttribute("loan_id", uuid);
        ra.addFlashAttribute("loan_id", uuid);

        return new RedirectView("success");
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String showSuccess(Model model, RedirectAttributes ra){
        String uuid = (String) model.getAttribute("loan_id");

        return "success";
    }
    @RequestMapping(value = "/success", method = RequestMethod.POST)
    public String onSuccessPost(Model model, RedirectAttributes ra) {
        return "index";
    }
    private Date parseDate(String dateStr){
        if(dateStr == null){
            return null;
        }
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            localDate = LocalDate.parse(dateStr, formatter);
        }catch (Exception e){
            e.printStackTrace();
            try{
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                localDate = LocalDate.parse(dateStr, formatter2);
            }
            catch (Exception e2) {
                e2.printStackTrace();
                localDate = LocalDate.of(1970, 01, 01);
            }
        }
        return java.sql.Date.valueOf(localDate);
    }
}
