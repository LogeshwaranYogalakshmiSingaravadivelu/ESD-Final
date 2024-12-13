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
public class JobDao extends Dao {


    public void saveJob(Job job) {
        try {
            openSession();
            beginTransaction();
            session.persist(job);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }


    public void updateJob(Job job) {
        try {
            openSession();
            beginTransaction();
            session.merge(job);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    public void deleteJob(Job job) {
        try {
            openSession();
            beginTransaction();
            session.delete(job);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    public void deleteJobById(int jobId) {
        try {
            openSession();
            beginTransaction();
            Job job = session.get(Job.class, jobId);
            if (job != null) {
                session.delete(job);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    public List<Job> getAllJobs() {
        try {
            openSession();
            String hql = "FROM Job";
            Query<Job> query = session.createQuery(hql, Job.class);
            return query.list();
        } finally {
            closeSession();
        }
    }

    public List<Job> getAllJobsByRecruiter(String email) {
        try {
            openSession();
            String hql = "FROM Job WHERE recruiterEmail = :email";
            Query<Job> query = session.createQuery(hql, Job.class);
            query.setParameter("email", email);
            return query.list();
        } finally {
            closeSession();
        }
    }

    public String getRecruiterEmail(int id) {
        try {
            openSession();
            String hql = "SELECT recruiterEmail FROM Job WHERE id = :id";
            Query<String> query = session.createQuery(hql, String.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } finally {
            closeSession();
        }
    }

    public Job getJobById(int jobId) {
        try {
            openSession();
            return session.get(Job.class, jobId);
        } finally {
            closeSession();
        }
    }

}
