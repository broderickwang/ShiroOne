package com.ttb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ttb.dao")
@SpringBootApplication
public class ShiroOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroOneApplication.class, args);
	}
}
