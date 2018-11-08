package com.example.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;

@SpringBootApplication
@Controller
@EnableAsync
public class EmailApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmailApplication.class, args);
  }

  @Autowired
  EmailServiceImpl emailService;

  @PostMapping(value = "/mail")
  public ResponseEntity sendMail() {

    emailService.sendSimpleMessage("recieving@mail.com", "Test mail");
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping(value = "/mailAsync")
  public ResponseEntity sendMailAsync() {

    emailService.sendSimpleMessageAsync("recieving@mail.com", "Test mail async");
    return new ResponseEntity(HttpStatus.OK);
  }

  @PostMapping(value = "/thymeleaf")
  public ResponseEntity sendMimeMailThymeLeaf() throws MessagingException {
    emailService.sendMimeMessageThymeleaf("Name", "Surname", "recieving@mail.com");
    return new ResponseEntity(HttpStatus.OK);
  }

}

