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

}
