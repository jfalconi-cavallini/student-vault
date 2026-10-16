package com.studentvault.note;

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

@RestController
@RequestMapping("/api/students/{studentId}/notes")
public class SessionNoteController {

    private final SessionNoteService noteService;

    public SessionNoteController(SessionNoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<SessionNoteResponse> create(@PathVariable UUID studentId,
                                                      @Valid @RequestBody SessionNoteRequest request) {
        SessionNoteResponse created = noteService.create(studentId, request);
        return ResponseEntity
                .created(URI.create("/api/students/" + studentId + "/notes/" + created.id()))
                .body(created);
    }

    @GetMapping
    public PagedModel<SessionNoteResponse> list(
            @PathVariable UUID studentId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return new PagedModel<>(noteService.list(studentId, pageable));
    }

    @GetMapping("/{noteId}")
    public SessionNoteResponse get(@PathVariable UUID studentId, @PathVariable UUID noteId) {
        return noteService.get(studentId, noteId);
    }

    @PutMapping("/{noteId}")
    public SessionNoteResponse update(@PathVariable UUID studentId, @PathVariable UUID noteId,
                                      @Valid @RequestBody SessionNoteUpdateRequest request) {
        return noteService.update(studentId, noteId, request);
    }

    @DeleteMapping("/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID studentId, @PathVariable UUID noteId) {
        noteService.delete(studentId, noteId);
    }
}