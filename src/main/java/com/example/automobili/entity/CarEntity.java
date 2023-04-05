package com.example.automobili.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    private String plate;

    @Column(name = "model")
    private String model;
    @Column(name = "brand")
    private String brand;
    @Column(name = "price")
    private Double price;
    @Column(name = "km")
    private int km;

    @ManyToOne(fetch = FetchType.LAZY)
    //FetchType.LAZY indica a Hibernate di recuperare solo le entità correlate dal database quando si usa la relazione
    @JoinColumn(name = "id_owner")
    //usata per specificare la mappatura di una colonna a chiave esterna tra due entità correlate.

    private OwnerEntity id_owner;

}
