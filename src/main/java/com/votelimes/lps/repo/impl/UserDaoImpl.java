package com.votelimes.lps.repo.impl;

import com.votelimes.lps.model.User;
import com.votelimes.lps.repo.dao.UserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    SessionFactory sessionFactory;


    @Override
    public User getByUserId(int userID) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<User> query = session.createQuery("from User ca where id = :userID");
        query.setLong("userID", userID);

        User result = query.getSingleResult();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<User> getAll() {
        Session session = SessionGetter.getSession(sessionFactory);

        List<User> result = session.createQuery("from User").list();
        return result;
    }

    @Transactional
    @Override
    public int insert(User usr) {
        Session session = SessionGetter.getSession(sessionFactory);
        Query query = session
                .createSQLQuery(
                        "insert into user_data (active, role, comment) values (?, cast(? as user_role), ?) returning id");

        query.setBoolean(1, usr.isActive());
        query.setString(2, String.valueOf(usr.getRole()));
        query.setString(3, usr.getComment());

        return (Integer) query.getSingleResult();
    }

    @Transactional
    @Override
    public void insertAll(Iterable<User> usrs) {
        Session session = SessionGetter.getSession(sessionFactory);
        Transaction transaction = session.beginTransaction();
        try {
            usrs.forEach(this::insert);
        }
        catch (Exception ignored){}
        finally {
            transaction.commit();
        }
    }
}
