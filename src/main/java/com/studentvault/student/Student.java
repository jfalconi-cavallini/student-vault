package com.studentvault.student;

import com.studentvault.tutor.Tutor;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 20)
    private String grade;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> subjects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)             // many students -> one tutor
    @JoinColumn(name = "assigned_tutor_id")        // the FK column
    private Tutor assignedTutor;

    @Column(nullable = false)
    private boolean archived = false;

    @Column(name = "parent_name", length = 100)
    private String parentName;

    @Column(name = "parent_email")
    private String parentEmail;

    @Column(nullable = false, length = 64)
    private String timezone = "America/Chicago";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Student() {}

    public Student(String name, String email, String grade, Tutor assignedTutor) {
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.assignedTutor = assignedTutor;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGrade() { return grade; }
    public List<String> getSubjects() { return subjects; }
    public Tutor getAssignedTutor() { return assignedTutor; }
    public boolean isArchived() { return archived; }
    public String getParentName() { return parentName; }
    public String getParentEmail() { return parentEmail; }
    public String getTimezone() { return timezone; }
    public Instant getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects == null ? new ArrayList<>() : new ArrayList<>(subjects);
    }
    public void setAssignedTutor(Tutor assignedTutor) { this.assignedTutor = assignedTutor; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}