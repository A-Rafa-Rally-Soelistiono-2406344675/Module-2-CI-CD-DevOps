package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;

//implemented isp: operasi tulis dipisahkan untuk klien yang hanya memodifikasi data.
public interface CarWriteRepository {
    Car create(Car car);

    Car update(String id, Car updatedCar);

    void delete(String id);
}
