package org.logesh.jobportal.Dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.logesh.jobportal.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDao {

    @Autowired
    SessionFactory sf;

        public void saveUser(Student student) {
            Session session = sf.openSession();
            Transaction t = session.beginTransaction();
            session.persist(student);
            t.commit();
            session.close();
        }

    public Student findByEmail(String email) {
        Session session = sf.openSession();
        try {
            String hql = "FROM Student WHERE email = :email";
            return session.createQuery(hql, Student.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }

}
