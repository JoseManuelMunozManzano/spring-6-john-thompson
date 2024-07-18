package com.jmunoz.spring6diassignment.controllers;

import org.springframework.stereotype.Controller;

import com.jmunoz.spring6diassignment.services.EnvironmentService;

@Controller
public class EnvironmentController {
  
  private final EnvironmentService environmentService;

  public EnvironmentController(EnvironmentService environmentService) {
    this.environmentService = environmentService;
  }

  public String getEnvironment() {
    return environmentService.getEnvironment();
  }
}
