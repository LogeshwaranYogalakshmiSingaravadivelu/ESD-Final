package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class Dao {

    @Autowired
    private SessionFactory sessionFactory;

    protected Session session;
    protected Transaction transaction;

    // Open session
    protected void openSession() {
        session = sessionFactory.openSession();
    }

    // Begin transaction
    protected void beginTransaction() {
        if (session == null) {
            openSession();
        }
        transaction = session.beginTransaction();
    }

    // Commit transaction
    protected void commitTransaction() {
        if (transaction != null) {
            transaction.commit();
        }
    }

    // Rollback transaction
    protected void rollbackTransaction() {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    // Close session
    protected void closeSession() {
        if (session != null) {
            session.close();
        }
    }
}
