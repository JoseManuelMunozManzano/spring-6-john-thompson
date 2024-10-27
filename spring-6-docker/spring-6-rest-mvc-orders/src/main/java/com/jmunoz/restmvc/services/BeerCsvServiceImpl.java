package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.BeerCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

// No olvidar, como he hecho yo, indicar @Service para que sea un componente de Spring
@Service
public class BeerCsvServiceImpl implements BeerCsvService {

    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) {

        try {
            // En el método .withType() se indica la clase en la que se hace el mapeo.
            // Al hacer esto, de forma automática se realiza este mapeo.
            List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build().parse();

            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
