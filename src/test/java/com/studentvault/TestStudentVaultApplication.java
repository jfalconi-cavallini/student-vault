package com.studentvault;

import org.springframework.boot.SpringApplication;

public class TestStudentVaultApplication {

	public static void main(String[] args) {
		SpringApplication.from(StudentVaultApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
