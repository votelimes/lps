package com.votelimes.lps.repo.impl;

import com.votelimes.lps.model.Passport;
import com.votelimes.lps.repo.dao.PassportDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PassportDaoImpl implements PassportDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Passport getBySeriesAndId(String series, String id) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<Passport> query = session.createQuery("from Passport ps where passportSeries = :passSeries AND passportID = :passID");
        query.setString("passSeries", series);
        query.setString("passID", id);

        return query.getSingleResult();
    }

    @Override
    public Iterable<Passport> getAll() {
        Session session = SessionGetter.getSession(sessionFactory);

        List<Passport> result = session.createQuery("from Passport").list();
        return result;
    }

    @Transactional
    @Override
    public int insert(Passport ps) throws SQLException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query query = session
                .createSQLQuery(
                        "insert into passport(first_name ,second_name, patronymic, gender, birthdate, marital_status, passport_id, passport_series, registration_country, registration_region, registration_city, registration_street, registration_house, registration_apps, user_id, issuedby, passport_date) values (?, ?, ?, cast(? as gender), ?, cast(? as marital_status), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id");

        query.setString(1, ps.getFirstName());
        query.setString(2, ps.getSecondName());
        query.setString(3, ps.getPatronymic());
        query.setString(4, String.valueOf(ps.getGender()));
        query.setDate(5, ps.getBirthDate());
        query.setString(6, String.valueOf(ps.getMaritalStatus()));
        query.setString(7, ps.getPassportID());
        query.setString(8, ps.getPassportSeries());
        query.setString(9, ps.getRegistrationCountry());
        query.setString(10, ps.getRegistrationRegion());
        query.setString(11, ps.getRegistrationCity());
        query.setString(12, ps.getRegistrationStreet());
        query.setString(13, ps.getRegistrationHouse());
        query.setString(14, ps.getRegistrationApps());
        query.setInteger(15, ps.getUser());
        query.setString(16, ps.getIssuedBy());
        query.setDate(17, ps.getPassportDate());

        return (Integer) query.getSingleResult();
    }

    @Override
    public List<Passport> getByUserId(int userId) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<Passport> query = session.createQuery("select ps from Passport ps join User u on ps.user = u.id where u.id = ?1 order by ps.passportDate");
        query.setInteger(1, userId);
        List<Passport> list = query.list();
        return query.list();
    }

    @Override
    public Passport getById(int id) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<Passport> query = session.createQuery("select ps from Passport ps where ps.id = ?1");
        query.setInteger(1, id);
        Passport result = query.getSingleResult();
        return result;
    }
}
