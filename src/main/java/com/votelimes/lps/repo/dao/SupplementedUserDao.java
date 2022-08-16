package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.SupplementedUserQueryBuilder;

import javax.persistence.NoResultException;
import java.sql.SQLException;

public interface SupplementedUserDao extends BaseDao<SupplementedUser> {
    public SupplementedUser getSupplementedUserById(int id) throws NoResultException;

    public SupplementedUser getSupplementedUserByUUID(String uuid) throws NoResultException;

    public int insert(SupplementedUser supplementedUser);

    public Iterable<SupplementedUser> getByFullNameAndState(String fullName, LoanState state) throws NoResultException;

    public Iterable<SupplementedUser> getByFullName(String fullName) throws NoResultException;

    public Iterable<SupplementedUser> getByNumberAndState(String number, LoanState state) throws NoResultException;

    public Iterable<SupplementedUser> getByNumber(String fullName) throws NoResultException;

    public Iterable<SupplementedUser> getByPassportAndState(String passportID, LoanState state) throws NoResultException;

    public Iterable<SupplementedUser> getByPassport(String passportID) throws NoResultException;

    public Iterable<SupplementedUser> getByFullNameAndStateAndNumberAndPassport(String fullName, LoanState state, String number, String seriesAndNumber) throws NoResultException, SQLException;
    public Iterable<SupplementedUser> getLoanState(LoanState loanState) throws NoResultException;

    public SupplementedUserQueryBuilder builder();
}
