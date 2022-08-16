package com.votelimes.lps.repo.impl;

import com.votelimes.lps.model.Passport;
import com.votelimes.lps.model.SupplementedUser;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.SupplementedUserQueryBuilder;
import com.votelimes.lps.repo.dao.SupplementedUserDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;


@Repository
public class SupplementedUserDaoImpl implements SupplementedUserDao {
    @Autowired
    SessionFactory sessionFactory;
    @Override
    public SupplementedUser getSupplementedUserById(int userID) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("from SupplementedUser usr where usr.id = :userID");
        query.setLong("userID", userID);

        SupplementedUser result = query.getSingleResult();
        return result;
    }

    @Override
    public SupplementedUser getSupplementedUserByUUID(String uuid) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr join CreditApplication ca on usr.id = ca.user where ca.uuid = :uuid");
        query.setString("uuid", uuid);

        SupplementedUser result = query.getSingleResult();
        return result;
    }

    @Override
    public int insert(SupplementedUser supplementedUser) {
        return 0;
    }

    @Override
    public Iterable<SupplementedUser> getByFullNameAndState(String fullName, LoanState state) throws NoResultException{
        Session session = SessionGetter.getSession(sessionFactory);

        /*Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr join Passport ps on usr.id = ps.user where concat(ps.secondName, ' ', ps.firstName, ' ', ps.patronymic) like ?1 AND ");*/
        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr left join CreditApplication ca on usr.id = ca.user join Passport p on ca.passportId = p.id where (cast(ca.loanState as text) = cast(?2 as text)) AND concat(p.secondName, ' ', p.firstName, ' ', p.patronymic) like ?1");

        query.setString(2, String.valueOf(state));
        query.setString(1, "%" + fullName + "%");

        List<SupplementedUser> result = query.list();

        result.parallelStream().forEach(item -> item.filterByLoanState(state));
        return result;
    }

    @Override
    public Iterable<SupplementedUser> getByFullName(String fullName) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr join Passport ps on usr.id = ps.user where concat(ps.secondName, ' ', ps.firstName, ' ', ps.patronymic) like ?1");
        query.setString(1, "%" + fullName + "%");

        Iterable<SupplementedUser> result = query.list();
        return result;
    }

    @Override
    public Iterable<SupplementedUser> getByNumberAndState(String number, LoanState state) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr left join CreditApplication ca on usr.id = ca.user where cast(ca.loanState as text) = cast(?2 as text) AND ca.contactNumber = ?1");

        query.setString(2, String.valueOf(state));
        query.setString(1, number);

        Iterable<SupplementedUser> result = query.list();
        return result;
    }

    @Override
    public Iterable<SupplementedUser> getByNumber(String number) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr left join CreditApplication ca on usr.id = ca.user where ca.contactNumber = ?1");
        query.setString(1, number);

        List<SupplementedUser> result = query.list();

        result.parallelStream().forEach(item -> item.filterByNumber(number));


        return result;
    }

    @Override
    public Iterable<SupplementedUser> getByPassportAndState(String passportID, LoanState state) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        /*Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr join Passport ps on usr.id = ps.user where concat(ps.secondName, ' ', ps.firstName, ' ', ps.patronymic) like ?1 AND ");*/
        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr left join CreditApplication ca on usr.id = ca.user join Passport p on ca.passportId = p.id where cast(ca.loanState as text) = cast(?2 as text) AND concat(p.passportSeries, ' ', p.passportID) like ?1");

        query.setString(2, String.valueOf(state));
        query.setString(1, "%" + passportID + "%");


        Iterable<SupplementedUser> result = query.list();
        return result;
    }

    @Override
    public Iterable<SupplementedUser> getByPassport(String passportID) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<SupplementedUser> query = session.createQuery("select usr from SupplementedUser usr join Passport ps on usr.id = ps.user where concat(ps.passportSeries, ' ', ps.passportID) like ?1");
        query.setString(1, "%" + passportID + "%");

        List<SupplementedUser> result = query.list();

        return result;
    }

    @Override
    public List<SupplementedUser> getByFullNameAndStateAndNumberAndPassport(String fullName, LoanState state, String number, String seriesAndNumber) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        StringBuilder queryBuilder = new StringBuilder("select distinct usr from SupplementedUser usr join CreditApplication ca on usr.id = ca.user join Passport ps on ca.passportId = ps.id where");
        boolean addAnd = false;
        if(fullName != null && fullName.length() > 0){
            queryBuilder.append(" (concat(ps.secondName, ' ', ps.firstName, ' ', ps.patronymic) like ?1)");
            addAnd = true;
        }
        if(state != null){
            if(addAnd){
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" (cast(ca.loanState as text) = cast(?2 as text)) ");
            addAnd = true;
        }
        if(number != null && number.length() > 0){
            if(addAnd){
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" (ca.contactNumber = ?3)");
            addAnd = true;
        }
        if(seriesAndNumber != null && seriesAndNumber.length() > 0){
            if(addAnd){
                queryBuilder.append(" AND");
            }
            queryBuilder.append(" (concat(ps.passportSeries, ' ', ps.passportID) like ?4)");
            addAnd = true;
        }
        if(addAnd == false){
            queryBuilder = new StringBuilder("select distinct usr from SupplementedUser usr where usr.role = 'client'");
        }

        Query<SupplementedUser> query = session.createQuery(queryBuilder.toString());

        if(fullName != null && fullName.length() > 0){
            query.setString(1, "%"+fullName+"%");
        }
        if(state != null){
            query.setString(2, String.valueOf(state));
        }
        if(number != null && number.length() > 0){
            query.setString(3, number);
        }
        if(seriesAndNumber != null && seriesAndNumber.length() > 0){
            query.setString(4, "%" + seriesAndNumber + "%");
        }

        return query.list();
    }

    @Override
    public Iterable<SupplementedUser> getLoanState(LoanState loanState) throws NoResultException {
        return null;
    }

    @Override
    public SupplementedUserQueryBuilder builder() {
        return new Builder();
    }

    @Override
    public Iterable<SupplementedUser> getAll() throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        List<SupplementedUser> result = session.createQuery("select distinct usr from SupplementedUser usr where usr.role = 'client'").list();
        return result;
    }



    @Override
    public void insertAll(Iterable<SupplementedUser> items) {
        Session session = SessionGetter.getSession(sessionFactory);
        Transaction transaction = session.beginTransaction();
        try {
            items.forEach(this::insert);
        }
        catch (Exception ignored){}
        finally {
            transaction.commit();
        }
    }

    // Билдер для составного запроса выборки пользователей в каталоге /lps/manager. Возвращает результат запроса.
    public class Builder implements SupplementedUserQueryBuilder {
        String fullName = null;
        LoanState loanState = null;
        String contactNumber = null;
        String passportSeriesAndId = null;
        @Override
        public SupplementedUserQueryBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        @Override
        public SupplementedUserQueryBuilder setLoanState(LoanState loanState) {
            this.loanState = loanState;
            return this;
        }

        @Override
        public SupplementedUserQueryBuilder setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
            return this;
        }

        @Override
        public SupplementedUserQueryBuilder setPassportSeriesAndId(String passportSeriesAndId) {
            this.passportSeriesAndId = passportSeriesAndId;
            return this;
        }

        @Override
        public List<SupplementedUser> buildAndExecuteGetByFullNameAndStateAndNumberAndPassport() {
            return SupplementedUserDaoImpl.this.getByFullNameAndStateAndNumberAndPassport(fullName, loanState, contactNumber, passportSeriesAndId);
        }
    }
}
