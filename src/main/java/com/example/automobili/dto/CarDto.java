package com.example.automobili.dto;

import lombok.Data;

@Data
public class CarDto {
    private String plate;
    private String model;
    private String brand;
    private int buyYear;
    private int km;
    private int idOwner;

}