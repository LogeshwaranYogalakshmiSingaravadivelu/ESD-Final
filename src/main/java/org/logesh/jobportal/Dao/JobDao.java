package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.logesh.jobportal.Model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobDao {

    @Autowired
    SessionFactory sf;

    public void saveJob(Job job) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(job);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Consider using custom exceptions
        } finally {
            session.close();
        }
    }

    public void updateJob(Job job) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.merge(job);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Consider using custom exceptions
        } finally {
            session.close();
        }
    }

    public void deleteJob(Job job) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(job);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Consider using custom exceptions
        } finally {
            session.close();
        }
    }

    public void deleteJobById(int jobId) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Job job = session.get(Job.class, jobId);
            if (job != null) {
                session.delete(job);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Handle exceptions properly or log them
        } finally {
            session.close();
        }
    }

    public List<Job> getAllJobs() {
        Session session = sf.openSession();
        try {
            String hql = "FROM Job";
            Query<Job> query = session.createQuery(hql, Job.class);
            return query.list();
        } finally {
            session.close();
        }
    }

    public Job getJobById(int jobId) {
        Session session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            return session.get(Job.class, jobId);
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; // Handle exceptions properly or log them
        } finally {
            session.close();
        }
    }

}
