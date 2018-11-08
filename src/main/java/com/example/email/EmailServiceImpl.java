package com.example.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl {

  public JavaMailSender emailSender;
  private SpringTemplateEngine templateEngine;

  @Autowired
  public EmailServiceImpl(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
    this.emailSender = emailSender;
    this.templateEngine = templateEngine;
  }

  public void sendSimpleMessage(String to, String subject) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText("Message sent synchronously");
    emailSender.send(message);
  }

  @Async
  public void sendSimpleMessageAsync(String to, String subject) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText("Message sent asynchronously");
    emailSender.send(message);
  }

  @Async
  public void sendMimeMessageThymeleaf(String name, String lastName, String email) throws MessagingException {
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());

    Map<String, Object> model = new HashMap<>();
    model.put("name", name);
    model.put("lastName", lastName);
    Context context = new Context();
    context.setVariables(model);

    String html = templateEngine.process("email-template", context);

    helper.setText(html, true);
    helper.setSubject("Email with inline image ");
    helper.setTo(email);
    helper.setFrom("sending@mail.com");
    helper.addInline("logo", new ClassPathResource("static/logo.png"));
    emailSender.send(message);
  }

}
