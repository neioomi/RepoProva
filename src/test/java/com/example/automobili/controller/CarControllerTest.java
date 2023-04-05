package com.example.automobili.controller;

import com.example.automobili.exception.PlateNotFoundException;
import com.example.automobili.model.Car;
import com.example.automobili.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Test
    void getCarByBrand() {
        // given
        String brand = "Toyota";
        Car car = new Car("1234ABC", "Toyota", "Camry", 25000.0, 5000, 1);
        // when
        when(carService.getCarByBrand(brand)).thenReturn(car);
        ResponseEntity<Car> response = carController.getCarByBrand(brand);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Car responseCar = response.getBody();
        assertNotNull(responseCar);
        assertEquals("1234ABC", responseCar.getPlate());
        assertEquals("Toyota", responseCar.getBrand());
        assertEquals("Camry", responseCar.getModel());
        assertEquals(25000.0, responseCar.getPrice());
        assertEquals(5000, responseCar.getKm());
        assertEquals(1, responseCar.getId_owner());
    }

    @Test
    void addCar() throws PlateNotFoundException {
        // given
        Car carToAdd = new Car("1234ABC", "Toyota", "Camry", 25000.0, 5000, 1);
        Car addedCar = new Car("1234ABC", "Toyota", "Camry", 25000.0, 5000, 1);
        // when
        when(carService.addCar(carToAdd)).thenReturn(addedCar);
        ResponseEntity response = carController.addCar(carToAdd);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Car responseCar = (Car) response.getBody();
        assertNotNull(responseCar);
        assertEquals("1234ABC", responseCar.getPlate());
        assertEquals("Toyota", responseCar.getBrand());
        assertEquals("Camry", responseCar.getModel());
        assertEquals(25000.0, responseCar.getPrice());
        assertEquals(5000, responseCar.getKm());
        assertEquals(1, responseCar.getId_owner());

    }

    @Test
    //test negativo, fallisce perchè il metodo addCar() non valida correttamente l'oggetto Car in ingresso e non gestisce i valori dei campi non validi o mancanti.

    public void addCar_returnsBadRequest_whenCarHasInvalidParameters() throws PlateNotFoundException {
        // given
        Car carToAdd = new Car("ABC-123", "", "Camry", -25000.0, -5000, 1);

        // when
        ResponseEntity response = carController.addCar(carToAdd);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

}