package com.studentvault.note;

import com.studentvault.student.Student;
import com.studentvault.tutor.Tutor;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "session_notes")
public class SessionNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(nullable = false, length = 200)
    private String topic;

    @Column(nullable = false, columnDefinition = "text")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected SessionNote() {}

    public SessionNote(Student student, Tutor tutor, String topic, String notes) {
        this.student = student;
        this.tutor = tutor;
        this.topic = topic;
        this.notes = notes;
    }

    public UUID getId() { return id; }
    public Student getStudent() { return student; }
    public Tutor getTutor() { return tutor; }
    public String getTopic() { return topic; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }

    public void setTopic(String topic) { this.topic = topic; }
    public void setNotes(String notes) { this.notes = notes; }
}