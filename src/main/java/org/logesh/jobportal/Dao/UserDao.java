package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.logesh.jobportal.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    SessionFactory sf;

        public void saveUser(User user) {
            Session session = sf.openSession();
            Transaction t = session.beginTransaction();
            session.persist(user);
            t.commit();
            session.close();
    }
}
