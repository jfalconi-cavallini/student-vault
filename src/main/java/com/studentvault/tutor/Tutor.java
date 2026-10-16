package com.studentvault.tutor;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity                      // "this class maps to a table"
@Table(name = "tutors")
public class Tutor {

    @Id                                            // primary key
    @GeneratedValue(strategy = GenerationType.UUID) // Hibernate generates the UUID
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JdbcTypeCode(SqlTypes.ARRAY)                  // maps List<String> to Postgres text[]
    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> subjects = new ArrayList<>();

    @Column(nullable = false)
    private boolean archived = false;

    @CreationTimestamp                             // set automatically on insert
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Tutor() {
        // JPA needs a no-arg constructor to build objects from rows
    }

    public Tutor(String name, String email, List<String> subjects) {
        this.name = name;
        this.email = email;
        setSubjects(subjects);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getSubjects() { return subjects; }
    public boolean isArchived() { return archived; }
    public Instant getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setSubjects(List<String> subjects) {
        this.subjects = subjects == null ? new ArrayList<>() : new ArrayList<>(subjects);
    }
    public void setArchived(boolean archived) { this.archived = archived; }
}