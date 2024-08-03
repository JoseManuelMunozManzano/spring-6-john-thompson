package com.jmunoz.restmvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase no es más que una estructura de datos que va a contener cada uno de las filas del fichero CSV.
// Todos los campos podrían haberse tipado con tipo String, pero si se puede, es mejor indicar un tipo de dato
// más relevante.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerCSVRecord {

    private Integer row;
    private Integer count;
    private String abv;
    private String ibu;
    private Integer id;
    private String beer;
    private String style;
    private Integer breweryId;
    private Float ounces;
    private String style2;
    private String count_y;
    private String city;
    private String state;
    private String label;
}
