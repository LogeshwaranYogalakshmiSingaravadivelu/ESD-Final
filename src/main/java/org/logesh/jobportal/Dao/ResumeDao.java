package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.logesh.jobportal.Model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResumeDao {

    @Autowired
    private SessionFactory sessionFactory;

    // Save resume
    public void saveResume(Resume resume) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(resume);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Get resume by student email
    public Resume getResumeByEmail(String email) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Resume WHERE studentEmail = :email";
            Query<Resume> query = session.createQuery(hql, Resume.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } finally {
            session.close();
        }
    }

    // Update resume
    public void updateResume(Resume resume) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(resume);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Delete resume by ID
    public void deleteResumeById(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Resume resume = session.get(Resume.class, id);
            if (resume != null) {
                session.delete(resume);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
