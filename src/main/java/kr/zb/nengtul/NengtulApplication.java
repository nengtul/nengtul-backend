package kr.zb.nengtul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableWebSecurity
public class NengtulApplication {

	public static void main(String[] args) {
		SpringApplication.run(NengtulApplication.class, args);
	}
}
