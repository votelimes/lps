package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.User;

public interface ClientDao {

    public User getById();

    public Iterable<User> getByName(String firstName, String secondName, String patronymic);

    public Iterable<User> getByTelephone(String number);

    public Iterable<User> getByPassport(String number);
}
