package com.purvar.springbootshirocas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringbootShiroCasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootShiroCasApplication.class, args);
	}
}
