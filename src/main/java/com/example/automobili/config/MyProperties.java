package com.example.automobili.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

//Questa classe è stata creata per implementare l'application property "app.regex".
@Component
@Data
public class MyProperties {
    @Value("${app.regex}")
    private String licensePlateRegex;

}
