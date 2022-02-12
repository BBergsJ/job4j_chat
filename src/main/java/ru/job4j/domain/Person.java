package ru.job4j.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public static Person of(int id, String name, String password, Role role) {
        Person person = new Person();
        person.id = id;
        person.name = name;
        person.password = password;
        person.role = role;
        return person;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name)
                && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
