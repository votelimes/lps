package com.votelimes.lps.repo.impl;

import com.votelimes.lps.model.CreditApplication;
import com.votelimes.lps.model.enums.LoanState;
import com.votelimes.lps.repo.dao.CreditApplicationDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Repository
public class CreditApplicationDaoImpl implements CreditApplicationDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<CreditApplication> getAll() {
        Session session = SessionGetter.getSession(sessionFactory);

        List<CreditApplication> result = session.createQuery("from CreditApplication").list();
        return result;
    }

    @Override
    public List<CreditApplication> getAllCompletedAndSigned() throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("select ca from CreditApplication ca where cast(ca.loanState as text) = cast(?1 as text) OR cast(ca.loanState as text) = cast(?2 as text)");
        query.setString(1, String.valueOf(LoanState.signed));
        query.setString(2, String.valueOf(LoanState.completed));
        List<CreditApplication> result = query.list();
        return result;
    }

    @Override
    public List<CreditApplication> getByLoanState(LoanState state) throws NoResultException{
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("select ca from CreditApplication ca where cast(ca.loanState as text) = cast(?1 as text)");
        query.setString(1, String.valueOf(state));
        List<CreditApplication> result = query.list();
        return result;
    }

    @Override
    public Iterable<CreditApplication> getByLoanStateAndFullName(LoanState state, String fullName) throws NoResultException{
        if(state != null) {
            Session session = SessionGetter.getSession(sessionFactory);
            Query<CreditApplication> query = session.createQuery("select ca from CreditApplication ca inner join Passport p on ca.passportId = p.id where cast(ca.loanState as text) = cast(?1 as text) AND concat(p.secondName, ' ', p.firstName, ' ', p.patronymic) like ?2");
            query.setString(1, String.valueOf(state));
            query.setString(2, "%" + fullName + "%");
            List<CreditApplication> result = query.list();
            return result;
        }
        else{
            return getByFullName(fullName);
        }
    }

    @Override
    public List<CreditApplication> getAllCompletedAndSignedByFullName(String fullName) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("select ca from CreditApplication ca inner join Passport p on ca.passportId = p.id where (cast(ca.loanState as text) = cast(?1 as text) OR cast(ca.loanState as text) = cast(?2 as text)) AND (concat(p.secondName, ' ', p.firstName, ' ', p.patronymic) like ?3)");
        query.setString(1, String.valueOf(LoanState.signed));
        query.setString(2, String.valueOf(LoanState.completed));
        query.setString(3, "%" + fullName + "%");
        List<CreditApplication> result = query.list();
        return result;
    }

    @Override
    public Iterable<CreditApplication> getByFullName(String fullName) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("select ca from CreditApplication ca inner join Passport p on ca.passportId = p.id where concat(p.secondName, ' ', p.firstName, ' ', p.patronymic) like ?1");
        query.setString(1, "%"+fullName+"%");
        List<CreditApplication> result = query.list();
        return result;
    }

    @Override
    public List<CreditApplication> getByUser(int userID) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("from CreditApplication ca where user = :userID order by ca.creationDate DESC");
        query.setLong("userID", userID);

        List<CreditApplication> result = query.list();
        return result;
    }

    @Override
    public CreditApplication getByUUID(String UUID) throws NoResultException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<CreditApplication> query = session.createQuery("from CreditApplication ca where uuid = :uuid");
        query.setString("uuid", UUID);

        return query.getSingleResult();
    }

    @Transactional
    @Override
    public int insert(CreditApplication ca) throws SQLException {
        Session session = SessionGetter.getSession(sessionFactory);

        Query query = session
                .createSQLQuery(
                        "insert into credit_application (desired_amount, repayment_time, loan_state, comment, uuid, user_id, passport_id, job_id, contact_number, approved_amount) values (?, ?, cast(? as loan_state), ?, ?, ?, ?, ?, ?, ?) returning id");

        query.setBigDecimal(1, ca.getDesiredAmount());
        query.setInteger(2, ca.getRepaymentTime());
        query.setString(3, String.valueOf(ca.getLoanState()));
        query.setString(4, ca.getComment());
        query.setString(5, ca.getUuid());
        query.setInteger(6, ca.getUser());
        query.setInteger(7, ca.getPassportId());
        query.setInteger(8, ca.getJobId());
        query.setString(9, ca.getContactNumber());
        query.setBigDecimal(10, ca.getApprovedAmount());

        return (Integer) query.getSingleResult();
    }

    @Override
    public int getUserIdByUUID(String UUID) {
        Session session = SessionGetter.getSession(sessionFactory);

        Query<Integer> query = session.createQuery("select ca.user from CreditApplication ca where uuid = :uuid");
        query.setString("uuid", UUID);

        return query.getSingleResult();
    }

    @Transactional
    @Override
    public int updateLoanStateAndSignDate(Date date, LoanState loanState, int id) {
        Session session = SessionGetter.getSession(sessionFactory);
        Query query = session.createSQLQuery("update credit_application ca set loan_state = (cast(?1 as loan_state)), sign_date = ?3 where id = ?2");
        query.setString(1, String.valueOf(loanState));
        query.setInteger(2, id);
        query.setDate(3, date);

        return query.executeUpdate();
    }

    @Transactional
    @Override
    public int updateLoanStateByUUID(LoanState loanState, String uuid) {
        Session session = SessionGetter.getSession(sessionFactory);
        Query query = session.createSQLQuery("update credit_application ca set loan_state = (cast(?1 as loan_state)) where ca.uuid = ?2");
        query.setString(1, String.valueOf(loanState));
        query.setString(2, uuid);

        return query.executeUpdate();
    }

    @Transactional
    @Override
    public int updateStateAmountTime(LoanState state, BigDecimal amount, int days, int id) {
        Session session = SessionGetter.getSession(sessionFactory);
        Query query = session.createSQLQuery("update credit_application ca set loan_state = cast(?1 as loan_state), approved_amount=?2, repayment_time=?3  where id = ?4");
        query.setString(1, String.valueOf(state));
        query.setBigDecimal(2, amount);
        query.setInteger(3, days);
        query.setInteger(4, id);

        return query.executeUpdate();
    }
}
