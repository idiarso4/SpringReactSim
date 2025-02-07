package net.guides.springboot2.springboot2jpacrudexample.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "assessments")
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(name = "subject")
    private String subject;

    @Column(name = "assessment_type")
    @Enumerated(EnumType.STRING)
    private AssessmentType assessmentType;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private AssessmentCategory category;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "score")
    private Integer score;

    @Column(name = "notes")
    private String notes;

    public enum AssessmentType {
        SUMMATIVE, NON_SUMMATIVE
    }

    public enum AssessmentCategory {
        THEORY, PRACTICAL
    }

    public Assessment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public AssessmentCategory getCategory() {
        return category;
    }

    public void setCategory(AssessmentCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getScore() {
        return score != null ? score : 0;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
