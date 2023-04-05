package com.example.automobili.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private String plate;
    private String brand;
    private String model;
    private double price;
    private int km;
    private int id_owner;

}
