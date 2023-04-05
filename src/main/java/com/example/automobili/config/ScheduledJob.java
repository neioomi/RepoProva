package com.example.automobili.config;

import com.example.automobili.entity.CarEntity;
import com.example.automobili.repository.CarHqlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ScheduledJob {
    @Autowired
    CarHqlRepository carHqlRepository;

    @Scheduled(cron = "${spring.task.scheduling.cron}")
    public void dailyStoredCars() {
        List<CarEntity> storedCars = carHqlRepository.findAll();
        System.out.println("Daily report of stored cars:");
        storedCars.forEach(car -> System.out.println(car.getBrand() +", " + car.getModel() + " (" + car.getPlate() + ")"));
        }
    }
