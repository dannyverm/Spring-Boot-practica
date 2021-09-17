package com.jpa.app.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DatabaseApplication implements CommandLineRunner {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	//como el BCryptP.. ya esta anotado como @bean podemos usarlo de esta manera
	public static void main(String[] args) {
		SpringApplication.run(DatabaseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		String password="12345";
		for(int i=0; i<2;i++){
			String bcryptPassword= passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}

		
	}

}
