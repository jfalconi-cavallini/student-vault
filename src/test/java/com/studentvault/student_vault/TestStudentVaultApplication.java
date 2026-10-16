package com.studentvault.student_vault;

import org.springframework.boot.SpringApplication;

public class TestStudentVaultApplication {

	public static void main(String[] args) {
		SpringApplication.from(StudentVaultApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
