package com.example.KaneStream;

import com.example.KaneStream.config.MinioConfiguration;
import com.example.KaneStream.config.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({MinioProperties.class})
@SpringBootApplication
public class KaneStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaneStreamApplication.class, args);
	}

}
