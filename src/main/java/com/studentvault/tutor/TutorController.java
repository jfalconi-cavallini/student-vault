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

@RestController                  // a bean that handles HTTP and returns JSON
@RequestMapping("/api/tutors")   // base path for every method below
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
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