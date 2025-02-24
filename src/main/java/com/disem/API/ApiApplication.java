package com.disem.API;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {
	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();

		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("MINIO_URL", dotenv.get("MINIO_URL"));
		System.setProperty("MINIO_USER", dotenv.get("MINIO_USER"));
		System.setProperty("MINIO_SECRET", dotenv.get("MINIO_SECRET"));
		System.setProperty("MINIO_BUCKET", dotenv.get("MINIO_BUCKET"));

		System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
		System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
		System.setProperty("MAIL_USER", dotenv.get("MAIL_USER"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("WEB_SERVICE_TOKEN", dotenv.get("WEB_SERVICE_TOKEN"));
		System.setProperty("WEB_SERVICE_LINK", dotenv.get("WEB_SERVICE_LINK"));



		SpringApplication.run(ApiApplication.class, args);
	}
}
