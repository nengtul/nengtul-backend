package kr.zb.nengtul;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableWebSecurity
@EnableFeignClients //mailgun
@RequiredArgsConstructor
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class NengtulApplication {

	public static void main(String[] args) {
		SpringApplication.run(NengtulApplication.class, args);
	}
}
