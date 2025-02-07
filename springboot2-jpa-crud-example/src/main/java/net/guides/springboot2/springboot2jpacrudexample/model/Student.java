package net.guides.springboot2.springboot2jpacrudexample.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nis", unique = true)
    private String nis;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    public Student() {
    }

    public Student(String nis, String name) {
        this.nis = nis;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }
}
