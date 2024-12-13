package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.logesh.jobportal.Model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResumeDao extends Dao{

    @Autowired
    private SessionFactory sessionFactory;

    // Save resume
    public void saveResume(Resume resume) {
        try {
            beginTransaction();
            session.persist(resume);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    // Get resume by student email
    public Resume getResumeByEmail(String email) {
        Resume resume = null;
        try {
            openSession();
            String hql = "FROM Resume WHERE studentEmail = :email";
            Query<Resume> query = session.createQuery(hql, Resume.class);
            query.setParameter("email", email);
            resume = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession();
        }
        return resume;
    }

    // Update resume
    public void updateResume(Resume resume) {
        try {
            beginTransaction();
            session.merge(resume);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }

    // Delete resume by ID
    public void deleteResumeById(int id) {
        try {
            beginTransaction();
            Resume resume = session.get(Resume.class, id);
            if (resume != null) {
                session.delete(resume);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        } finally {
            closeSession();
        }
    }
}
