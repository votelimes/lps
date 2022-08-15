package com.votelimes.lps.repo.dao;

import com.votelimes.lps.model.JobPlace;

import java.sql.SQLException;

public interface JobPlaceDao {
    public Iterable<JobPlace> getByUserId(int userID);

    public int insert(JobPlace jp) throws SQLException;
}
