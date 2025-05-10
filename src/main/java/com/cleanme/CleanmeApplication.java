package com.cleanme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CleanmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanmeApplication.class, args);

		TestLombok t = new TestLombok();
		t.setName("Test");
		System.out.println(t.getName());
	}

}
