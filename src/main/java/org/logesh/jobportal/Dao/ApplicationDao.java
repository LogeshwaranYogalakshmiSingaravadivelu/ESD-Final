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
public class ApplicationDao extends Dao{

    @Autowired
    SessionFactory sf;

    public void saveApplication(Application application) {
        try {
            beginTransaction();
            session.persist(application);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e; // Consider logging or wrapping in a custom exception
        } finally {
            closeSession();
        }
    }

    public List<Application> getApplicationsByStudentEmail(String email) {
        try {
            openSession();
            String hql = "FROM Application WHERE studentEmail = :email";
            return session.createQuery(hql, Application.class)
                    .setParameter("email", email)
                    .list();
        } finally {
            closeSession();
        }
    }

    public List<Object[]> getStudentsEmail(String email) {
        try {
            openSession();
            String hql = "SELECT a.jobId, a.studentEmail, a.status, a.id FROM Application a WHERE a.recruiterEmail = :recruiterEmail";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("recruiterEmail", email);
            return query.list();
        } finally {
            closeSession();
        }
    }

    public boolean existsByJobIdAndStudentEmail(Long jobId, String studentEmail) {
        try {
            openSession();
            String hql = "SELECT COUNT(a) FROM Application a WHERE a.jobId = :jobId AND a.studentEmail = :studentEmail";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("jobId", jobId);
            query.setParameter("studentEmail", studentEmail);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            closeSession();
        }
    }

    public void updateApplication(Application application) {
        try {
            beginTransaction();
            session.merge(application);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e; // Consider logging or wrapping in a custom exception
        } finally {
            closeSession();
        }
    }

    public Application getApplicationById(int applicationId) {
        try {
            openSession();
            return session.get(Application.class, applicationId);
        } finally {
            closeSession();
        }
    }


}
