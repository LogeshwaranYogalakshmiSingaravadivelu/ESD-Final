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
public class CollegeDao extends Dao{


    public List<Application> getPendingApprovals(String status) {
        try {
            openSession();
            String hql = "FROM Application WHERE status = :status";
            Query<Application> query = session.createQuery(hql, Application.class);
            query.setParameter("status", status);
            return query.list();
        } finally {
            closeSession();
        }
    }

    public Application getApplicationById(int id) {
        try {
            openSession();
            return session.get(Application.class, id);
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
            throw e;
        } finally {
            closeSession();
        }
    }

}
