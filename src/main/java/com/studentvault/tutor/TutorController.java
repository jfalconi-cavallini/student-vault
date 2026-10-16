package com.studentvault.tutor;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import com.studentvault.student.StudentResponse;
import com.studentvault.student.StudentService;

@RestController                  // a bean that handles HTTP and returns JSON
@RequestMapping("/api/tutors")   // base path for every method below
public class TutorController {

    private final TutorService tutorService;
    private final StudentService studentService;

    public TutorController(TutorService tutorService, StudentService studentService) {
        this.tutorService = tutorService;
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<TutorResponse> create(@Valid @RequestBody TutorRequest request) {
        TutorResponse created = tutorService.create(request);
        return ResponseEntity
                .created(URI.create("/api/tutors/" + created.id()))  // 201 + Location header
                .body(created);
    }

    @GetMapping
    public PagedModel<TutorResponse> list(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return new PagedModel<>(tutorService.list(pageable));
    }

    @GetMapping("/{id}/students")
    public PagedModel<StudentResponse> students(
            @PathVariable UUID id,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return new PagedModel<>(studentService.listByTutor(id, pageable));
    }

    @GetMapping("/{id}")
    public TutorResponse get(@PathVariable UUID id) {
        return tutorService.get(id);
    }

    @PutMapping("/{id}")
    public TutorResponse update(@PathVariable UUID id, @Valid @RequestBody TutorRequest request) {
        return tutorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)   // 204
    public void archive(@PathVariable UUID id) {
        tutorService.archive(id);
    }
}