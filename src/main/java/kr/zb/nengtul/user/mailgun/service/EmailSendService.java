package kr.zb.nengtul.user.mailgun.service;

import kr.zb.nengtul.user.mailgun.client.MailgunClient;
import kr.zb.nengtul.user.mailgun.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSendService {
  private final MailgunClient mailgunClient;

  public void sendEmail(){
    SendMailForm form = SendMailForm.builder()
        .from("lvet0330@gmail.com")
        .to("lvet0330@gmail.com")
        .subject("Test")
        .text("test")
        .build();
    mailgunClient.sendEmail(form);
  }

}
