package com.example.automobili.repository;

import com.example.automobili.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarHqlRepository extends JpaRepository<CarEntity, String> {
    @Query("SELECT ce FROM CarEntity ce WHERE ce.brand = :brand")
    CarEntity findByBrand(String brand);

}
