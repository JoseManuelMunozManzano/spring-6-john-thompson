package com.jmunoz.spring6webapp.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jmunoz.spring6webapp.domain.Author;
import com.jmunoz.spring6webapp.domain.Book;
import com.jmunoz.spring6webapp.domain.Publisher;
import com.jmunoz.spring6webapp.repositories.AuthorRepository;
import com.jmunoz.spring6webapp.repositories.BookRepository;
import com.jmunoz.spring6webapp.repositories.PublisherRepository;

// Si Spring encuentra una clase en su contexto (gracias a @Component lo está) que
// implementa CommandLineRunner, va a ejecutar automáticamente su método run()
@Component
public class BootstrapData implements CommandLineRunner {

  // Una buena práctica es declarar componentes del contexto de Spring como final,
  // para que no puedan cambiar.
  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;
  private final PublisherRepository publisherRepository;

  // Como es el único constructor de la clase, Spring va a ver que tiene como argumentos
  // los tres repositorios, que están en su contexto, y hará automáticamente el @Autowired
  // de las implementaciones de los repositorios, que nos proporciona Spring Data JPA.
  public BootstrapData(AuthorRepository authorRepository, BookRepository bookRepository,
                       PublisherRepository publisherRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
    this.publisherRepository = publisherRepository;
  }

  @Override
  public void run(String... args) throws Exception {

    Author eric = new Author();
    eric.setFirstName("Eric");
    eric.setLastName("Evans");

    Book ddd = new Book();
    ddd.setTitle("Domain Driven Design");
    ddd.setIsbn("123456");

    // Cuando llamamos al método save(), la interface repositorio devuelve un nuevo objeto.
    // Es buena práctica recoger este nuevo objeto salvado.
    Author ericSaved = authorRepository.save(eric);
    Book dddSaved = bookRepository.save(ddd);

    Author rod = new Author();
    rod.setFirstName("Rod");
    rod.setLastName("Johnson");

    Book noEJB = new Book();
    noEJB.setTitle("J2EE Development without EJB");
    noEJB.setIsbn("56567324");

    Author rodSaved = authorRepository.save(rod);
    Book noEJBSaved = bookRepository.save(noEJB);

    // Añadir un publisher y persistirlo
    Publisher rba = new Publisher();
    rba.setPublisherName("RBA Editores");
    rba.setState("Spain");
    rba.setCity("Madrid");
    rba.setAddress("C\\Benedicto 21");
    rba.setZipCode("28003");
    Publisher rbaSaved = publisherRepository.save(rba);

    // Asociación entre autor - libro y entre libro - publisher, y los persistimos en ambos casos.

    // Estas dos instrucciones pueden dar error en tiempo de ejecución (null).
    // Si ocurre, es porque el tipo de dato Set tiene que estar inicializado.
    // Se ha inicializado en sus clases Entity.
    ericSaved.getBooks().add(dddSaved);
    rodSaved.getBooks().add(noEJBSaved);

    dddSaved.setPublisher(rbaSaved);
    noEJBSaved.setPublisher(rbaSaved);

    authorRepository.save(ericSaved);
    authorRepository.save(rodSaved);
    bookRepository.save(dddSaved);
    bookRepository.save(noEJBSaved);    

    System.out.println("In Bootstrap");
    System.out.println("Author Count: " + authorRepository.count());
    System.out.println("Book Count: " + bookRepository.count());
        
    System.out.println("Publisher Count: " + publisherRepository.count());
  }
}
