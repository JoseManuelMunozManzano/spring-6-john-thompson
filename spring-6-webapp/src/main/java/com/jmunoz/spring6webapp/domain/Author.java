package com.jmunoz.spring6webapp.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String firstName;
  private String lastName;

  // Preferimos usar Set en vez de List, porque cada uno de sus elementos va a ser único.
  // Una lista permite elementos duplicados.
  @ManyToMany(mappedBy = "authors")
  private Set<Book> books;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<Book> getBooks() {
    return books;
  }

  public void setBooks(Set<Book> books) {
    this.books = books;
  }

  @Override
  public String toString() {
    return "Author {id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", books=" + books + "}";
  }

  // Una buena práctica cuando se trabaja con entidades JPA, es sobreescribir los métodos
  // equals() y hashCode() porque Hibernate los va a usar internamente para determinar la igualdad
  // entre objetos.
  // Las estrategias para determinar la unicidad de un objeto son:
  //    - Algunas personas usan solo la propiedad id.
  //    - Otras usan todas las propiedades de la clase.
  // No es mejor una estrategia que otra. Depende del caso de uso.
  // En este proyecto vamos a considerar que dos objetos son iguales si tienen el mismo id.
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Author other = (Author) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
