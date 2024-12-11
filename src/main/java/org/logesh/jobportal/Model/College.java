package org.logesh.jobportal.Model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component
@Entity
@Table(name = "college")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String name;
    private String email;

    public College(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public College() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
