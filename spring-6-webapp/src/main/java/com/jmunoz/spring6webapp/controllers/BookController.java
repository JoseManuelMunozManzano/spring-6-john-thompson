package com.jmunoz.spring6webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jmunoz.spring6webapp.services.BookService;

@Controller
public class BookController {

  private final BookService bookService;

  // Inyección automática en tiempo de ejecución, ya que BookService está anotado
  // con @Service y este controlador está anotado con @Controller.
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @RequestMapping("/books")
  public String getBooks(Model model) {

    model.addAttribute("books", bookService.findAll());

    // Vista de Thymeleaf
    return "books";
  }
}
