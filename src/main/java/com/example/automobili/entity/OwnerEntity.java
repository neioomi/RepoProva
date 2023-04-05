package com.example.automobili.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table (name= "owner")
public class OwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_owner")
    private int id_owner;
    @Column(name = "name")
    private String name;
    @Column(name= "surname")
    private String surname;


    @OneToMany(mappedBy = "id_owner")
    private List<CarEntity> cars;

}

