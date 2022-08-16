package com.votelimes.lps.model;


import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.model.enums.UserRole;
import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



// Таблица, необходимая для работы ManagerController и многокритериального поиска заявок и пользователей. По сути, расширеный
// другими таблицами пользователь. Может быть удалена и заменена отдельными, уже существующими таблицами.
@Entity
@Data
@Table(name = "user_data")
public class SupplementedUser {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(name = "active")
    boolean active;

    @Column(name = "comment")
    String comment;

    @Transient
    Passport lastPassport = null;

    @OneToMany()
    @JoinColumn(name = "user_id")
    List<CreditApplication> creditApplications;

    @OneToMany
    @JoinColumn(name = "user_id")
    List<Passport> passports = null;

    @OneToMany
    @JoinColumn(name = "user_id")
    List<JobPlace> jobPlaces;

    public String getFirstNumber(){
        try {
            return creditApplications.get(0).getContactNumber();
        }
        catch (Exception e){
            return null;
        }
    }

    public String getLastPassportFullAddress(){
        assignLastPassport();
        return lastPassport.getFullAddress();
    }

    public String getLastPassportFullName(){
        assignLastPassport();
        return lastPassport.getFullName();
    }

    public String getLastPassportSeriesAndNumber() {
        assignLastPassport();
        return lastPassport.getSeriesAndNumber();
    }

    public void filterByLoanState(LoanState ls){
        creditApplications = creditApplications
                .stream()
                .filter(creditApplication -> creditApplication
                        .getLoanState()
                        .equals(ls))
                .collect(Collectors.toList());
    }

    public void filterByNumber(String number){
        creditApplications = creditApplications
                .stream()
                .filter(creditApplication -> creditApplication
                        .getContactNumber()
                        .equals(number))
                .collect(Collectors.toList());
    }

    private void assignLastPassport(){
        if(lastPassport == null) {
            try {
                List<Passport> sortedPassports = passports
                        .stream()
                        .sorted(Comparator.comparing(Passport::getPassportDate)).collect(Collectors.toList());
                lastPassport = sortedPassports.get(0);
            } catch (Exception ignored) {
            }
        }
    }
}
