package com.example.automobili.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//classe che permette di inserire il file csv, il cui percorso è stato indicato nelle application.properies, all'interno dei una variabile di tipo String
@Component
@Data
public class CsvReader {
    @Value("${myapp.csv-file.path}")
    private String csvFilePath;
    @Value("${myapp.csv-file.filename}")
    private String csvFileName;
}



