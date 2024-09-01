package com.flywire.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FlywireSpringBootApp extends SpringBootServletInitializer
{

  public static void main(String[] args) throws Exception
  {
    SpringApplication.run(FlywireSpringBootApp.class, args);
  }
}
