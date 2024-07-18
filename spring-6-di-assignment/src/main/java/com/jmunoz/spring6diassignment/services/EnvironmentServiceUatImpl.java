package com.jmunoz.spring6diassignment.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("uat")
@Service
public class EnvironmentServiceUatImpl implements EnvironmentService {

  @Override
  public String getEnvironment() {
    return "This is the UAT environment";
  }
  
}
