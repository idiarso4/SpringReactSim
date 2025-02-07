package net.guides.springboot2.springboot2jpacrudexample.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journals")
public class Journal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassRoom classRoom;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false, length = 500)
    private String materialCovered;

    @Column(length = 500)
    private String homework;

    @Column(length = 1000)
    private String notes;

    public Journal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMaterialCovered() {
        return materialCovered;
    }

    public void setMaterialCovered(String materialCovered) {
        this.materialCovered = materialCovered;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
