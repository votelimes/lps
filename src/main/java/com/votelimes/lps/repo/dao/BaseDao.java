package com.votelimes.lps.repo.dao;

import javax.persistence.NoResultException;

public interface BaseDao<T> {
    public Iterable<T> getAll()  throws NoResultException;
    public void insertAll(Iterable<T> items);
}
