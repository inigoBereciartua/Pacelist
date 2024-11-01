package com.ibereciartua.pacelist.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.ibereciartua.pacelist")
public class PacelistApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacelistApplication.class, args);
	}

}
