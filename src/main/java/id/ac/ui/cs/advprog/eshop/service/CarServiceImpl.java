package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarReadRepository;
import id.ac.ui.cs.advprog.eshop.repository.CarWriteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    //implemented dip: service bergantung pada abstraksi repository, bukan class konkret.
    //implemented isp: service menggunakan kontrak read/write yang spesifik.
    private final CarReadRepository carReadRepository;
    private final CarWriteRepository carWriteRepository;

    public CarServiceImpl(CarReadRepository carReadRepository,
                          CarWriteRepository carWriteRepository) {
        this.carReadRepository = carReadRepository;
        this.carWriteRepository = carWriteRepository;
    }

    @Override
    public Car create(Car car) {
        carWriteRepository.create(car);
        return car;
    }

    @Override
    public List<Car> findAll() {
        Iterator<Car> carIterator = carReadRepository.findAll();
        List<Car> allCar = new ArrayList<>();

        carIterator.forEachRemaining(allCar::add);
        return allCar;
    }

    @Override
    public Car findById(String carId) {
        return carReadRepository.findById(carId);
    }

    @Override
    public void update(String carId, Car car) {
        carWriteRepository.update(carId, car);
    }

    @Override
    public void deleteCarById(String carId) {
        carWriteRepository.delete(carId);
    }
}
