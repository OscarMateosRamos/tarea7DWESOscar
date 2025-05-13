package com.oscar.vivero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Tarea7ViveroApplication {

	public static void main(String[] args) {
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String password = "admin";
		String hashedPassword = passwordEncoder.encode(password);
		System.out.println("*Contraseña encriptada: " + hashedPassword);
		
		SpringApplication.run(Tarea7ViveroApplication.class, args);
	}

}
