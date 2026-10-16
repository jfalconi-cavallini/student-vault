package com.studentvault.tutor;

import com.studentvault.common.Emails;
import com.studentvault.common.error.ConflictException;
import com.studentvault.common.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service   // marks this as a bean holding business logic
public class TutorService {

    private final TutorRepository tutorRepository;

    // Constructor injection: Spring sees this constructor and passes in the repository bean.
    // `final` + constructor = can't be null or swapped later, and easy to fake in unit tests.
    public TutorService(TutorRepository tutorRepository) {
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public TutorResponse create(TutorRequest request) {
        String email = Emails.normalize(request.email());
        if (tutorRepository.existsByEmail(email)) {
            throw new ConflictException( "Email already in use");
        }
        Tutor tutor = new Tutor(request.name().trim(), email, request.subjects());
        return TutorResponse.from(tutorRepository.save(tutor));
    }

    @Transactional(readOnly = true)
    public Page<TutorResponse> list(Pageable pageable) {
        return tutorRepository.findAll(pageable).map(TutorResponse::from);
    }

    @Transactional(readOnly = true)
    public TutorResponse get(UUID id) {
        return TutorResponse.from(findOrThrow(id));
    }

    @Transactional
    public TutorResponse update(UUID id, TutorRequest request) {
        Tutor tutor = findOrThrow(id);
        String email = Emails.normalize(request.email());
        if (tutorRepository.existsByEmailAndIdNot(email, id)) {
            throw new ConflictException( "Email already in use");
        }
        tutor.setName(request.name().trim());
        tutor.setEmail(email);
        tutor.setSubjects(request.subjects());
        return TutorResponse.from(tutor);   // no save() needed — see note below
    }

    @Transactional
    public void archive(UUID id) {
        findOrThrow(id).setArchived(true);  // soft delete: keep the history
    }

    private Tutor findOrThrow(UUID id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException( "Tutor not found"));
    }

}