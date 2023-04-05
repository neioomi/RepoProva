package com.example.automobili.controller;

import com.example.automobili.config.CsvReader;
import com.example.automobili.exception.PlateNotFoundException;
import com.example.automobili.model.Car;
import com.example.automobili.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RequestMapping
@Controller
public class CarController {
    @Autowired
    CarService carService;
    @Autowired
    CsvReader csvReader;



    // ENDPOINTS
    @GetMapping("/getCarPlate/{plate}")  //RECUPERARE AUTO TRAMITE TARGA
    public ResponseEntity<Car> getCarByPlate(@PathVariable String plate) throws PlateNotFoundException {
        Car car = carService.getCarByPlate(plate);
        return new ResponseEntity<>(car, HttpStatus.OK);


    }

    /*
    ResponseEntity represents the whole HTTP response: status code, headers, and body.
    As a result, we can use it to fully configure the HTTP response.
    If we want to use it, we have to return it from the endpoint
     */

    @GetMapping("/getCarBrand/{brand}")  //RECUPERARE AUTO TRAMITE BRAND
    public ResponseEntity<Car> getCarByBrand(@PathVariable String brand) {

        Car car = carService.getCarByBrand(brand);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/getCarsByBrandAndKm/{brand}/{km}")
    //RECUPERARE UNA LISTA DI AUTO con almeno 10.000 km e di una data marca
    public ResponseEntity<List<Car>> carsByBrandAndKm(@PathVariable String brand, int km) {
        List<Car> carList = carService.carsByBrandAndKm(brand, km);
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }


    //ResponseEntity -> è una classe che rappresenta la risposta HTTP che il server invierà al client in risposta a una richiesta.
    @PostMapping("/addCar") //AGGIUNGERE AUTO
    public ResponseEntity addCar(@RequestBody Car car) {//@RequestBody -> per indicare che un parametro del metodo deve essere legato al corpo della richiesta HTTP
        Car addedCar = null;
        try {
            addedCar = carService.addCar(car);
        } catch (PlateNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(addedCar);
    }
/*
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCars(@RequestParam("file") MultipartFile file, CarEntity carEntity) throws IOException{
        try {
            file.transferTo( new File("C:\\Users\\noemi.palmiero\\Desktop\\test\\" +csvReader.getCsvFileName())); //x salvare il file caricato in una directory del server
            List<Car> cars = carService.uploadCars();
            return ResponseEntity.ok("Successfully uploaded " + cars.size() + " cars");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload cars: " + e.getMessage());
        }
    }
*/

    @PostMapping("/upload")
    public ResponseEntity<List<Car>> uploadCars(@RequestHeader("Authorization") String myAuth, @RequestParam("file") MultipartFile file) {
        if (myAuth.equals("Bearer 12345")) {
            try {
                Path path = Paths.get(csvReader.getCsvFilePath(), csvReader.getCsvFileName());
                List<Car> cars = carService.uploadCars(file, path, StandardCopyOption.REPLACE_EXISTING);
                return ResponseEntity.ok(cars);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateCarKm/{plate}")//AGGIORNAMENTO KM AUTO
    public ResponseEntity<String> updateKm(@PathVariable String plate, @RequestParam int km) { // @RequestParam -> per mappare i parametri della richiesta ai parametri del metodo "updatekm()". @PathVariable -> per mappare una variabile nell'URL a un parametro
        String message;
        try {
            message = "Mileage update correctly";
            carService.updateKm(plate, km);
        } catch (PlateNotFoundException e) {
            message = "Mileage operation impossible due to plate not found";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
}



