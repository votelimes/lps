package com.votelimes.lps.repo.impl;

import com.votelimes.lps.model.JobPlace;
import com.votelimes.lps.repo.dao.JobPlaceDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Repository
public class JobPlaceDaoImpl implements JobPlaceDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Iterable<JobPlace> getByUserId(int userID) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<JobPlace> query = session.createQuery("from JobPlace jp where user = :userID");
        query.setInteger("userID", userID);

        return query.list();
    }

    @Transactional
    @Override
    public int insert(JobPlace jp) throws SQLException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query query = session
                .createSQLQuery(
                        "insert into job_place (job_title, company_name, period_start, period_end, salary, user_id) values (?, ?, ?, ?, ?, ?) returning id");

        query.setString(1, jp.getJobTitle());
        query.setString(2, jp.getCompanyName());
        query.setDate(3, jp.getDateStart());
        query.setDate(4, jp.getDateEnd());
        query.setInteger(5, jp.getSalary());
        query.setInteger(6, jp.getUser());

        return (Integer) query.getSingleResult();
    }
}
