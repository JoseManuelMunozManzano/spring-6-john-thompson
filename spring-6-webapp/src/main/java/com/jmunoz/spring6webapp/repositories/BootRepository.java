package com.jmunoz.spring6webapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jmunoz.spring6webapp.domain.Book;

public interface BootRepository extends CrudRepository<Book, Long> {
  
}
