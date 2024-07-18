package com.jmunoz.spring6diassignment.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("qa")
@Service
public class EnvironmentServiceQAImpl implements EnvironmentService {

  @Override
  public String getEnvironment() {
    return "This is the QA environment";
  }
  
}
