package com.example.automobili.repository;

import com.example.automobili.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarNativeRepository extends JpaRepository<CarEntity, String> {

    //Così avremmo definito la query, nel caso di un Repository Native.
    @Query(nativeQuery = true, value = "SELECT * FROM car WHERE brand = :brand")
    CarEntity findByBrand (String brand);

}

