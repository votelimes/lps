package com.votelimes.lps.model;
import com.votelimes.lps.model.enums.LoanState;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "credit_application")
@Data
public class CreditApplication {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int user;
    @Column(name = "desired_amount")
    private BigDecimal desiredAmount;

    @Column(name = "repayment_time")
    private short repaymentTime;
    @Column(name = "comment")
    private String comment;

    @Column(name = "loan_state")
    @Enumerated(EnumType.STRING)
    private LoanState loanState;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    @Column(name = "sign_date")
    private Date signDate;

    @Column(name = "passport_id")
    int passportId;

    @Column(name = "job_id")
    int jobId;

    @OneToOne
    @JoinColumn(name = "passport_id", insertable = false, updatable = false)
    Passport passport;

    public String getSignDateAsString(){
        if(signDate == null){
            return "НЕ ПОДПИСАН";
        }
        else{
            return signDate.toString();
        }
    }

    public CreditApplication() {
        desiredAmount = new BigDecimal(0);
        comment = "";
        contactNumber = "";
        loanState = LoanState.created;
        regenerateUUID();
    }

    public void regenerateUUID(){
        setUuid(UUID.randomUUID().toString());
    }
}
