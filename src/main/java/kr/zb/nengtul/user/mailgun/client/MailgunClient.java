package kr.zb.nengtul.user.mailgun.client;

import kr.zb.nengtul.user.mailgun.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name ="mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {
  @PostMapping("sandbox4881823332d54198a2c7f394a4f51361.mailgun.org/messages")
  feign.Response sendEmail(@SpringQueryMap SendMailForm form);
}
