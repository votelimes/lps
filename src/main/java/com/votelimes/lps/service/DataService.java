package com.votelimes.lps.service;

import com.votelimes.lps.model.*;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

@Service
public class DataService {
    @Autowired
    CreditApplicationDao creditApplicationDao;

    @Autowired
    JobPlaceDao jobPlaceDao;

    @Autowired
    PassportDao passportDao;

    @Autowired
    SupplementedUserDao supplementedUserDao;

    @Autowired
    UserDao userDao;



    @Transactional
    public String insertNewLoan(User usr, Passport ps, CreditApplication ca, JobPlace jp) throws SQLException {
        int newUserId = 0;
        Passport existingPassport = null;
        try {
            existingPassport = passportDao.getBySeriesAndId(ps.getPassportSeries(), ps.getPassportID());
        }catch (NoResultException ignored){}

        if(existingPassport != null){
            ps = existingPassport;
            ca.setUser(ps.getUser());
            ca.setPassportId(ps.getId());
            if(jp != null) {
                jp.setUser(ps.getUser());
                int newJobId = jobPlaceDao.insert(jp);
                ca.setJobId(newJobId);
                CreditManager.processClient(ps, ca);
                creditApplicationDao.insert(ca);
                // insertion done
            }
        }
        else{
            newUserId = userDao.insert(usr);
            ps.setUser(newUserId);
            ca.setUser(newUserId);
            if(jp != null) {
                jp.setUser(newUserId);
                int newJobId = jobPlaceDao.insert(jp);
                ca.setJobId(newJobId);
            }
            int newPassportId = passportDao.insert(ps);
            ca.setPassportId(newPassportId);
            CreditManager.processClient(ps, ca);
            creditApplicationDao.insert(ca);
            // insertion done
        }
        return ca.getUuid();
    }

    public SupplementedUser getSupplementedUserByUUID(String uuid) throws NoResultException {
        int userID = creditApplicationDao.getUserIdByUUID(uuid);
        SupplementedUser supplementedUser = supplementedUserDao.getSupplementedUserById(userID);

        return supplementedUser;
    }

    public void signCreditApplication(String uuid){
        CreditApplication ca = creditApplicationDao.getByUUID(uuid);
        int id = ca.getId();
        Date date = Date.valueOf(LocalDate.now());
        creditApplicationDao.updateLoanStateAndSignDate(date, LoanState.signed, id);
    }

    public void updateStateAmountTime(LoanState state, BigDecimal amount, int time){

    }
}
