package com.jmunoz.spring6webapp.services;

import com.jmunoz.spring6webapp.domain.Book;

public interface BookService {
  
  Iterable<Book> findAll();
}
