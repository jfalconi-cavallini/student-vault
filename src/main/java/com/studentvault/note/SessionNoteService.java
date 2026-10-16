package com.studentvault.note;

import com.studentvault.common.error.BadRequestException;
import com.studentvault.common.error.NotFoundException;
import com.studentvault.student.Student;
import com.studentvault.student.StudentRepository;
import com.studentvault.tutor.Tutor;
import com.studentvault.tutor.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SessionNoteService {

    private final SessionNoteRepository noteRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public SessionNoteService(SessionNoteRepository noteRepository,
                              StudentRepository studentRepository,
                              TutorRepository tutorRepository) {
        this.noteRepository = noteRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public SessionNoteResponse create(UUID studentId, SessionNoteRequest request) {
        Student student = findStudentOrThrow(studentId);
        if (student.isArchived()) {
            throw new BadRequestException("Cannot add notes to an archived student");
        }
        Tutor tutor = tutorRepository.findById(request.tutorId())
                .orElseThrow(() -> new BadRequestException("Tutor does not exist"));
        if (tutor.isArchived()) {
            throw new BadRequestException("Archived tutors cannot write notes");
        }
        SessionNote note = new SessionNote(student, tutor, request.topic().trim(), request.notes());
        return SessionNoteResponse.from(noteRepository.save(note));
    }

    @Transactional(readOnly = true)
    public Page<SessionNoteResponse> list(UUID studentId, Pageable pageable) {
        findStudentOrThrow(studentId);   // 404 if the student doesn't exist (not an empty page)
        return noteRepository.findByStudentId(studentId, pageable).map(SessionNoteResponse::from);
    }

    @Transactional(readOnly = true)
    public SessionNoteResponse get(UUID studentId, UUID noteId) {
        return SessionNoteResponse.from(findNoteOrThrow(studentId, noteId));
    }

    @Transactional
    public SessionNoteResponse update(UUID studentId, UUID noteId, SessionNoteUpdateRequest request) {
        SessionNote note = findNoteOrThrow(studentId, noteId);
        note.setTopic(request.topic().trim());
        note.setNotes(request.notes());
        return SessionNoteResponse.from(note);
    }

    @Transactional
    public void delete(UUID studentId, UUID noteId) {
        noteRepository.delete(findNoteOrThrow(studentId, noteId));
    }

    private Student findStudentOrThrow(UUID studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found"));
    }

    private SessionNote findNoteOrThrow(UUID studentId, UUID noteId) {
        // A note under the wrong student is reported as "not found", not "forbidden":
        // don't confirm that the id exists somewhere else.
        return noteRepository.findByIdAndStudentId(noteId, studentId)
                .orElseThrow(() -> new NotFoundException("Session note not found"));
    }
}