package com.Security.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EnableJpaAuditing
@SpringBootApplication
public class MyblogApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(MyblogApplication.class, args);
//	}
public static void main(String[] args) {
	SpringApplication application = new SpringApplication(MyblogApplication.class);

	// 파일에서 값 로드 및 설정
	application.addInitializers((ApplicationContextInitializer<ConfigurableApplicationContext>) context -> {
		ConfigurableEnvironment environment = context.getEnvironment();
		Map<String, Object> properties = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader("secretKey.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] keyValue = line.split("=", 2);
				properties.put("oauth.google." + keyValue[0].trim(), keyValue[1].trim());
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to read secretKey.txt", e);
		}

		environment.getPropertySources().addFirst(new MapPropertySource("fileProperties", properties));
	});

	application.run(args);
}

}
