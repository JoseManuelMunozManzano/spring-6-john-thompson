package com.jmunoz.spring6webapp.services;

import org.springframework.stereotype.Service;

import com.jmunoz.spring6webapp.domain.Book;
import com.jmunoz.spring6webapp.repositories.BookRepository;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  // Inyección automática de BookRepository gracias a haber anotado esta clase con @Service
  // y a que BookRepository implementa CrudRepository.
  public BookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Iterable<Book> findAll() {
    return bookRepository.findAll();
  }
}
