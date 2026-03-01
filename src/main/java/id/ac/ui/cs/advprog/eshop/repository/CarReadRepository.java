package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;

import java.util.Iterator;

//implemented isp: operasi baca dipisahkan dari operasi tulis.
public interface CarReadRepository {
    Iterator<Car> findAll();

    Car findById(String id);
}
