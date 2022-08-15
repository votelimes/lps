package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.User;

import java.sql.SQLException;

public interface UserDao extends BaseDao<User>{
    public User getByUserId(int userId);

    public int insert(User usr) throws SQLException;
}
