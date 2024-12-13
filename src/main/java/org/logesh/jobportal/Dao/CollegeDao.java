package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.logesh.jobportal.Model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollegeDao {

    @Autowired
    SessionFactory sf;

    public List<Application> getPendingApprovals(String status) {
        Session session = sf.openSession();
        try {
            String hql = "FROM Application WHERE status = :status";
            Query<Application> query = session.createQuery(hql, Application.class);
            query.setParameter("status", status);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Application getApplicationById(int id) {
        Session session = sf.openSession();
        try {
            return session.get(Application.class, id);
        } finally {
            session.close();
        }
    }

    public void updateApplication(Application application) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.merge(application);
        tx.commit();
        session.close();
    }

}
