package com.studentvault.student;

import com.studentvault.tutor.Tutor;
import com.studentvault.tutor.TutorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.UUID;

@Service
public class StudentService {

    private static final String DEFAULT_TIMEZONE = "America/Chicago";

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public StudentService(StudentRepository studentRepository, TutorRepository tutorRepository) {
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public StudentResponse create(StudentRequest request) {
        String email = normalizeEmail(request.email());
        if (studentRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Student student = new Student(
                request.name().trim(), email, request.grade(), resolveTutor(request.assignedTutorId()));
        applyOptionalFields(student, request);
        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> list(Pageable pageable) {
        return studentRepository.findAll(pageable).map(StudentResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse get(UUID id) {
        return StudentResponse.from(findOrThrow(id));
    }

    @Transactional
    public StudentResponse update(UUID id, StudentRequest request) {
        Student student = findOrThrow(id);
        String email = normalizeEmail(request.email());
        if (studentRepository.existsByEmailAndIdNot(email, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        student.setName(request.name().trim());
        student.setEmail(email);
        student.setGrade(request.grade());
        student.setAssignedTutor(resolveTutor(request.assignedTutorId()));
        applyOptionalFields(student, request);
        return StudentResponse.from(student);   // dirty checking saves it
    }

    @Transactional
    public void archive(UUID id) {
        findOrThrow(id).setArchived(true);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> listByTutor(UUID tutorId, Pageable pageable) {
        if (!tutorRepository.existsById(tutorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tutor not found");
        }
        return studentRepository.findByAssignedTutorId(tutorId, pageable).map(StudentResponse::from);
    }

    // ---- helpers ----

    private void applyOptionalFields(Student student, StudentRequest request) {
        student.setSubjects(request.subjects());
        student.setParentName(request.parentName());
        student.setParentEmail(request.parentEmail() == null ? null : normalizeEmail(request.parentEmail()));
        student.setTimezone(resolveTimezone(request.timezone()));
    }

    private Tutor resolveTutor(UUID tutorId) {
        if (tutorId == null) {
            return null;
        }
        Tutor tutor = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Assigned tutor does not exist"));
        if (tutor.isArchived()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot assign an archived tutor");
        }
        return tutor;
    }

    private static String resolveTimezone(String timezone) {
        if (timezone == null || timezone.isBlank()) {
            return DEFAULT_TIMEZONE;
        }
        try {
            return ZoneId.of(timezone.trim()).getId();   // rejects typos like "America/Dalas"
        } catch (DateTimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid timezone: " + timezone);
        }
    }

    private Student findOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}