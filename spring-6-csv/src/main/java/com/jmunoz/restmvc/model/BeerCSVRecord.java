package com.jmunoz.restmvc.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Esta clase no es más que una estructura de datos que va a contener cada uno de las filas del fichero CSV.
// Todos los campos podrían haberse tipado con tipo String, pero si se puede, es mejor indicar un tipo de dato
// más relevante.
//
// Los campos que no se indican aquí no se mapean desde el CSV. No los queremos.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BeerCSVRecord {

    // @CsvBindByName - Si la propiedad coincide con el nombre de la propiedad, coincide con el nombre de la cabecera.
    // Si no coincide, se indica el nombre de la cabecera del CSV, como se ha hecho en count.
    @CsvBindByName
    private Integer row;

    @CsvBindByName(column = "count.x")
    private Integer count;

    @CsvBindByName
    private String abv;

    @CsvBindByName
    private String ibu;

    @CsvBindByName
    private Integer id;

    @CsvBindByName
    private String beer;

    @CsvBindByName
    private String style;

    @CsvBindByName(column = "brewery_id")
    private Integer breweryId;

    @CsvBindByName
    private Float ounces;

    @CsvBindByName
    private String style2;

    @CsvBindByName(column = "count.y")
    private String count_y;

    @CsvBindByName
    private String city;

    @CsvBindByName
    private String state;

    @CsvBindByName
    private String label;
}
