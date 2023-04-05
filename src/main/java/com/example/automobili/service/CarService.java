package com.example.automobili.service;

import com.example.automobili.config.CsvReader;
import com.example.automobili.config.MyProperties;
import com.example.automobili.entity.CarEntity;
import com.example.automobili.exception.MileageDecrementException;
import com.example.automobili.exception.PlateNotFoundException;
import com.example.automobili.model.Car;
import com.example.automobili.repository.CarHqlRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.nio.file.Files.*;
import static java.util.Comparator.comparing;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.*;

@Service
public class CarService {
    @Autowired
    CarHqlRepository carHqlRepository;

    @Autowired
    MyProperties myProperties;

    @Autowired
    CsvReader csvReader;

    @Autowired
    private ModelMapper modelMapper;


    //1 scrivi un metodo che restituisce un elenco di auto raggruppate per marca
    public Map<String, List<Car>> carsByBrand(List<Car> carList) {
        return carList.stream() //x accedere a una collection e x accedere al dato in modo funzionale
                .collect(groupingBy(Car::getBrand)); //permette di chiamare un metodo senza istanziare la classe, all'interno di una lambda
    } //in italics metodi statici

    //2 scrivi un metodo che restituisce una media dei km di tutte le auto

    public Double averageKm(List<Car> carList) {
        return carList.stream()
                .collect(averagingDouble(Car::getKm));
    }

    //3 scrivi un metodo che restituisce una lista di auto con almeno 10.000 km e di una data marca
/*
        public List<Car> carsByBrandAndKm (List<Car> carList, String brand){
        List<Car> cars = new ArrayList<>();
        for(Car car : carList){
            if(car.getBrand().equals(brand) && car.getKm() >= 10000){
                cars.add(car);
            }
        }
        return cars;
    }
    */

    //3 CON LAMBDA

    public List<Car> carsByBrandAndKm(String brand, int km) {

        List<CarEntity> carList = carHqlRepository.findAll();

        return carList.stream()
                .filter(car -> car.getBrand().equals(brand))
                .filter(car -> car.getKm() >= km)
                .map(i -> toModel(i)).collect(Collectors.toList());
    }


    //4 scrivi un metodo che restituisce una lista di auto con almeno 50.000 km ed applica uno sconto del 10% sul prezzo
/*
    public List<Car> carsByKmAndDiscountedPrice(List<Car> carList) {
        List<Car> discountedCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getKm() >= 50000) {
                double discountedPrice = car.getPrice() - (car.getPrice() * 0.1);
                Car discountedCar = new Car(car.getPlate(), car.getBrand(), car.getModel(), discountedPrice, car.getKm());
                discountedCars.add(discountedCar);
            }
        }
        return discountedCars;
    }
*/


    public List<Car> carsByKmAndDiscountedPrice(List<Car> carList, int km, double discount) {
        return carList.stream()
                .filter(car -> car.getKm() >= km)
                .map(car -> new Car(car.getPlate(), car.getBrand(), car.getModel(), car.getPrice() * discount, car.getKm(), car.getId_owner()))
                .collect(toList());
    }


    //5 scrivi un metodo che restituisce l'auto con il costo più basso
    public Car getCarWithLowestCost(List<Car> carList) {
        return carList.stream()
                .sorted(comparing(Car::getPrice))
                .findFirst().get();
    }


    public Car getCarByPlate(String plate) throws PlateNotFoundException {
        Optional<CarEntity> byId = carHqlRepository.findById(plate);
        byId.orElseThrow(PlateNotFoundException::new);

        return toModel(byId.get());
    }

    private boolean validateLicensePlate(String plate) {
        Pattern pattern = compile(myProperties.getLicensePlateRegex()); //il metodo compile è utilizzato per creare un pattern dalla regex passata come parametro all'interno del metodo stesso
        Matcher matcher = pattern.matcher(plate);
        return matcher.matches();
    }
    /*
    La classe Matcher viene utilizzata per cercare in un testo le occorrenze multiple di un'espressione regolare.
    È possibile utilizzare un Matcher anche per cercare la stessa espressione regolare in testi diversi.

     Una volta creato, un matcher può essere usato per eseguire tre diversi tipi di operazioni di corrispondenza:
     Il metodo matches() tenta di confrontare l'intera sequenza di input con lo schema.
     Il metodo lookingAt() tenta di far corrispondere la sequenza di input, a partire dall'inizio, allo schema.
     Il metodo find() analizza la sequenza di input alla ricerca della sottosequenza successiva che corrisponde allo schema.
        Ciascuno di questi metodi restituisce un booleano che indica il successo o il fallimento.

     */

    public Car getCarByBrand(String brand) {
        return toModel(carHqlRepository.findByBrand(brand));
    }

    public void updateKm(String plate, int km) throws PlateNotFoundException, MileageDecrementException {
        Car carModel = getCarByPlate(plate);
        if (km < carModel.getKm()) {  //SE VENGONO INSERITI MENO KM RISPETTO AI PREESISTENTI, IL FRAMEWORK LANCIA UN'ECCEZIONE CUSTOM
            throw new MileageDecrementException("It is impossible to decrease mileage");
        }
        carModel.setKm(km);
        carHqlRepository.save(toEntity(carModel));
    }

    public Car addCar(Car car) throws PlateNotFoundException {
        if (validateLicensePlate(car.getPlate())) {
            CarEntity savedCar = carHqlRepository.save(toEntity(car));
            return toModel(savedCar);

        } else {
            throw new PlateNotFoundException("Car license not found");
        }
    }

    private CarEntity toEntity(Car carModel) {
        return modelMapper.map(carModel, CarEntity.class);
    }

    private Car toModel(CarEntity carEntity) {

        return modelMapper.map(carEntity, Car.class);
    }

    private void saveTemporaryFile(InputStream inputStream, Path targetPath, StandardCopyOption option) throws IOException {
        Files.copy(inputStream, targetPath, option);
    }

    /*String x = "ciao";
           x.concat(" noemi");
           System.out.println(x);
           StringBuffer sb = new StringBuffer();
           sb.append("ciao").append(" ").append("noemi").toString();

            */
    public List<Car> uploadCars(MultipartFile file, Path targetPath, StandardCopyOption option) throws IOException {

        // Salvare il file caricato in una directory temporanea sul server
        saveTemporaryFile(file.getInputStream(), targetPath, option);

        // Analizzare il file CSV e convertirlo in un elenco di oggetti Car
        Reader reader = newBufferedReader(targetPath);
        List<Car> cars = new CsvToBeanBuilder<Car>(reader)
                .withType(Car.class)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator(',')
                .build()
                .parse();

        //Convertire l'elenco di oggetti Car in un elenco di oggetti CarEntity e salvarlo nel database.
        List<CarEntity> carEntityList = cars.stream()
                .map(this::toEntity)
                .collect(toList());
        carHqlRepository.saveAll(carEntityList);

        return cars;
    }

    /*
    public List<Car> uploadCars(FileInputStream csvFile) throws IOException {
            csvReader.saveTemporaryFile((FileInputStream) csvFile.getInputStream());
            Reader reader = newBufferedReader(Path.of(csvReader.getCsvFilePath().concat(csvReader.getCsvFileName()))); //legge file csv
            List<Car> cars = new CsvToBeanBuilder<Car>(reader) //converte file csv in oggetto
                    .withType(Car.class)//".withType" imposta il tipo di bean da popolare
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build()//Costruisce il CsvToBean a partire dalle informazioni fornite.
                    .parse(); // converte l'oggetto `CsvToBean` in una lista di cars
            List<CarEntity> carEntityList = cars.stream()
                    .map(i -> toEntity(i)).collect(toList());
            carHqlRepository.saveAll(carEntityList);
            return cars;
    }

     */


}




