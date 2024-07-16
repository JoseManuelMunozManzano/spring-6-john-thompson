package com.jmunoz.primarybean.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyControllerTest {

  @Autowired
  MyController controller;

  @Test
  void testSayHello() {
    System.out.println(controller.sayHello());
  }
}
