package com.votelimes.lps.model;

import com.votelimes.lps.model.enums.Gender;
import com.votelimes.lps.model.enums.MaritalStatus;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;


// Если пользователь, при создании заявки вводит существующий паспорт (серия и номер одинаковы),
// он не будет добавлен в бд.
// Вместо него будет использоваться паспорт, который был введен ранее, таким образом у одного паспорта может быть
// нексолько заявок. У одного пользователя может быть несколько паспортов, однако данный функционал не внедрен до конца.
@Entity
@Table(name = "passport")
@Data
public class Passport {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "second_name")
    String secondName;

    @Column(name = "patronymic")
    String patronymic;

    @Column(name = "birthdate")
    Date birthDate;

    @Column(name = "passport_series")
    String passportSeries;

    @Column(name = "passport_id")
    String passportID;

    @Column(name = "passport_date")
    Date passportDate;

    @Column(name = "registration_country")
    String registrationCountry;

    @Column(name = "registration_region")
    String registrationRegion;

    @Column(name = "registration_city")
    String registrationCity;

    @Column(name = "registration_street")
    String registrationStreet;

    @Column(name = "registration_house")
    String registrationHouse;

    @Column(name = "registration_apps")
    String registrationApps;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    Gender gender;

    // Кем выдан
    @Column(name = "issuedBy")
    String issuedBy;

    // Пользователь, ассоциируемый с паспортом
    @Column(name = "user_id")
    int user;

    public String getFullAddress(){
        StringBuilder sb = new StringBuilder();
        if(isStringValid(registrationCountry)){
            sb.append(registrationCountry);
            sb.append(", ");
        }
        if(isStringValid(registrationRegion)){
            sb.append(registrationRegion);
            sb.append(", ");
        }
        if(isStringValid(registrationCity)){
            sb.append(registrationCity);
            sb.append(", ");
        }
        if(isStringValid(registrationStreet)){
            sb.append("улица ");
            sb.append(registrationStreet);
            sb.append(", ");
        }
        if(isStringValid(registrationHouse)){
            sb.append("дом ");
            sb.append(registrationHouse);
            if(isStringValid(registrationApps)){
                sb.append(", ");
            }
        }
        if(isStringValid(registrationApps)){
            sb.append("кв. ");
            sb.append(registrationApps);
        }
        return sb.toString();
    }
    public String getFullName(){
        StringBuilder sb = new StringBuilder();
        if(isStringValid(secondName)){
            sb.append(secondName);
            sb.append(" ");
        }
        if(isStringValid(firstName)){
            sb.append(firstName);
            sb.append(" ");
        }
        if(isStringValid(patronymic)){
            sb.append(patronymic);

        }
        return sb.toString();
    }

    public String getSeriesAndNumber(){
        StringBuilder sb = new StringBuilder();
        if(isStringValid(passportSeries)){
            sb.append(passportSeries);
            sb.append(" ");
        }
        if(isStringValid(passportID)){
            sb.append(passportID);
        }
        return sb.toString();
    }

    private boolean isStringValid(String str){
        if(str != null){
            return str.length() > 0;
        }
        return false;
    }


}
