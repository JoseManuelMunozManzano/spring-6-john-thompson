package com.jmunoz.spring6diassignment.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"dev", "default"})
@Service
public class EnvironmentServiceDevImpl implements EnvironmentService {

  @Override
  public String getEnvironment() {
    return "This is the DEV environment";
  }
}
