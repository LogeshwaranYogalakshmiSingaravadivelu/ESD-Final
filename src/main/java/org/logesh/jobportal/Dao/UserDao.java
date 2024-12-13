package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.logesh.jobportal.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends Dao{

    @Autowired
    SessionFactory sf;

        public void saveUser(User user) {
            try {
                beginTransaction();
                session.persist(user);
                commitTransaction();
            } catch (Exception e) {
                rollbackTransaction();
                throw e;
            } finally {
                closeSession();
            }
        }

    public User findByEmail(String email) {
        openSession();
        try {
            String hql = "FROM User WHERE email = :email";
            return session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            closeSession();
        }
    }

    public String getUsernameByEmail(String email) {
        openSession();
        try {
            String hql = "SELECT name FROM User WHERE email = :email";
            return session.createQuery(hql, String.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            closeSession();
        }
    }

}
