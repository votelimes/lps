package com.votelimes.lps.model;

import com.votelimes.lps.model.enums.LoanState;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CreditManager {

    // Stub
    public static boolean isLoanPossible(Passport ps, CreditApplication ca){
        return (new Random()).nextBoolean();
    }

    public static void processClient(Passport ps, CreditApplication ca){
        if(isLoanPossible(ps, ca)){
            ca.setLoanState(LoanState.approved);
            ca.setRepaymentTime((short) (ThreadLocalRandom.current().nextInt(1, 12 + 1) * 30));
            ca.setApprovedAmount(ca.getDesiredAmount());
        }
        else{
            ca.setRepaymentTime((short) 0);
            ca.setApprovedAmount(BigDecimal.valueOf(0));
            ca.setLoanState(LoanState.rejected);
        }
    }
}
