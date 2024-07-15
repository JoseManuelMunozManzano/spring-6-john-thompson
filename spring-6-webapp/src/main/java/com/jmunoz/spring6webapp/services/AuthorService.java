package com.jmunoz.spring6webapp.services;

import com.jmunoz.spring6webapp.domain.Author;

public interface AuthorService {
  
  Iterable<Author> findAll();
}
