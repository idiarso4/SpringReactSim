package net.guides.springboot2.springboot2jpacrudexample.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "classrooms")
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private Set<Student> students;

    public ClassRoom() {
    }

    public ClassRoom(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
