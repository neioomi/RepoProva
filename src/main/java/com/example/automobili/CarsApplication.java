package com.example.automobili;

import com.example.automobili.model.Car;
import com.example.automobili.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class CarsApplication implements CommandLineRunner {
    @Autowired
    private CarService carService;
    private static List<Car> carList;

    public static void main(String[] args) {

        SpringApplication.run(CarsApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        /*
        carList = Arrays.asList(
                new Car("DM100AA", "Alfa Romeo", "Brera", 30000, 108903, 3),
                new Car("EV000CH", "Audi", "A3", 40000, 3633, 2),
                new Car("EW051CH", "Citroen", "C3", 25000, 25362, 1),
                new Car("DX063BE", "Chevrolet", "Silverado", 13000, 37257, 4),
                new Car("FJ050AA", "Citroen", "C4", 15000, 374861, 4),
                new Car("EY040BW", "Alfa Romeo", "Giulietta", 35000, 283437, 3),
                new Car("EV064FC", "Audi", "A6", 40000, 3028764, 2),
                new Car("BC076DL", "Dacia", "Duster", 50000, 5242, 1),
                new Car("GM114FE", "Fiat", "Tipo", 35000, 2300, 2),
                new Car("GE102DZ", "Fiat", "500", 25000, 35000, 3),
                new Car("AD089DN", "Ford", "Mustang", 60000, 2350, 4),
                new Car("FK042CL", "Ford", "Fiesta", 30000, 40000, 1)
        );

        System.out.println(carService.carsByBrand(carList));
        System.out.println(carService.averageKm(carList));
        System.out.println(carService.carsByBrandAndKm(carList, "Alfa Romeo", 10000));
        System.out.println(carService.getCarWithLowestCost(carList));
        System.out.println(carService.carsByKmAndDiscountedPrice(carList, 50000, 0.9));
        */
        }
    }
