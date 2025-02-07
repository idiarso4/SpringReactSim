package net.guides.springboot2.springboot2jpacrudexample.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counseling_sessions")
public class Counseling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "counselor_id", nullable = false)
    private User counselor;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(length = 1000)
    private String recommendation;

    @Column(length = 500)
    private String followUpAction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounselingType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounselingStatus status;

    public enum CounselingType {
        ACADEMIC,
        BEHAVIORAL,
        CAREER,
        PERSONAL,
        SOCIAL
    }

    public enum CounselingStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NEEDS_FOLLOWUP
    }

    public Counseling() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getCounselor() {
        return counselor;
    }

    public void setCounselor(User counselor) {
        this.counselor = counselor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getFollowUpAction() {
        return followUpAction;
    }

    public void setFollowUpAction(String followUpAction) {
        this.followUpAction = followUpAction;
    }

    public CounselingType getType() {
        return type;
    }

    public void setType(CounselingType type) {
        this.type = type;
    }

    public CounselingStatus getStatus() {
        return status;
    }

    public void setStatus(CounselingStatus status) {
        this.status = status;
    }
}
