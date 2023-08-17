package kr.zb.nengtul;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients //mailgun
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@ComponentScan({
    "s3bucket", "kr.zb.nengtul"
})
public class NengtulApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(NengtulApplication.class, args);
  }
}
