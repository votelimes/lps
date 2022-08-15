package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.Passport;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;

public interface PassportDao {
    public Passport getBySeriesAndId(String series, String id) throws NoResultException;

    public Iterable<Passport> getAll();

    public int insert(Passport passport) throws SQLException;

    public List<Passport> getByUserId(int userId) throws NoResultException;

    public Passport getById(int id) throws NoResultException ;
}
