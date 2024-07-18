package com.jmunoz.spring6diassignment.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service
public class EnvironmentServiceProdImpl implements EnvironmentService {

  @Override
  public String getEnvironment() {
    return "This is the PROD environment";
  }
  
}
