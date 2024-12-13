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
public class ApplicationDao {

    @Autowired
    SessionFactory sf;

    public void saveApplication(Application application) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(application);
        tx.commit();
        session.close();
    }

    public List<Application> getApplicationsByStudentEmail(String email) {
        Session session = sf.openSession();
        try {
            String hql = "FROM Application WHERE studentEmail = :email";
            return session.createQuery(hql, Application.class)
                    .setParameter("email", email)
                    .list();
        } finally {
            session.close();
        }
    }

    public List<Object[]> getStudentsEmail(String email) {
        Session session = sf.openSession();
        try {
            String hql = "SELECT a.jobId, a.studentEmail, a.status, a.id FROM Application a WHERE a.recruiterEmail = :recruiterEmail";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("recruiterEmail", email);
            return query.list();
        } finally {
            session.close();
        }
    }

    public boolean existsByJobIdAndStudentEmail(Long jobId, String studentEmail) {
        Session session = sf.openSession();
        try {
            String hql = "SELECT COUNT(a) FROM Application a WHERE a.jobId = :jobId AND a.studentEmail = :studentEmail";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("jobId", jobId);
            query.setParameter("studentEmail", studentEmail);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    public void updateApplication(Application application) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.update(application);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Application getApplicationById(int applicationId) {
        Session session = sf.openSession();
        try {
            return session.get(Application.class, applicationId);
        } finally {
            session.close();
        }
    }


}
