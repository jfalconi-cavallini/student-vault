package com.studentvault;

import com.studentvault.student.Student;
import com.studentvault.student.StudentRepository;
import com.studentvault.tutor.Tutor;
import com.studentvault.tutor.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest                              // boot the whole app
@Import(TestcontainersConfiguration.class)   // ...against a throwaway Postgres
@Transactional                               // roll back after each test
class RepositoryIntegrationTests {

    @Autowired TutorRepository tutorRepository;     // DI: Spring hands us the beans
    @Autowired StudentRepository studentRepository;

    @Test
    void savesStudentAndFindsThemByAssignedTutor() {
        Tutor tutor = tutorRepository.save(
                new Tutor("Ana Rivera", "ana@example.com", List.of("Math", "SAT")));
        studentRepository.save(new Student("Sam Lee", "sam@example.com", "7", tutor));

        Page<Student> page = studentRepository.findByAssignedTutorId(
                tutor.getId(), PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(1);
        Student found = page.getContent().get(0);
        assertThat(found.getName()).isEqualTo("Sam Lee");
        assertThat(found.getId()).isNotNull();
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(tutor.getSubjects()).containsExactly("Math", "SAT");
    }
}