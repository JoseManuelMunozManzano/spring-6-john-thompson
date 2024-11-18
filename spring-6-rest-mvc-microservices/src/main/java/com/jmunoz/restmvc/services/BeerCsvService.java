package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    List<BeerCSVRecord> convertCSV(File csvFile);

}
