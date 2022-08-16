package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.Passport;
import com.votelimes.lps.model.enums.LoanState;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface CreditApplicationDao {
    public List<CreditApplication> getAll();

    public List<CreditApplication> getAllCompletedAndSigned() throws NoResultException;

    public List<CreditApplication> getByLoanState(LoanState state) throws NoResultException;

    public Iterable<CreditApplication> getByLoanStateAndFullName(LoanState state, String fullName) throws NoResultException;

    public List<CreditApplication> getAllCompletedAndSignedByFullName(String fullName) throws NoResultException;

    public Iterable<CreditApplication> getByFullName(String fullName) throws NoResultException;

    public List<CreditApplication> getByUser(int userID);

    public CreditApplication getByUUID(String UUID);

    public int insert(CreditApplication ca) throws SQLException;

    public int getUserIdByUUID(String UUID);

    public int updateLoanStateAndSignDate(Date date, LoanState loanState, int id);

    public int updateLoanStateByUUID(LoanState loanState, String uuid);

    public int updateStateAmountTime(LoanState state, BigDecimal amount, int days, int id);
}
