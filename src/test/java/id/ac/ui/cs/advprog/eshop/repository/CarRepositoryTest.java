package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class CarRepositoryTest {
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void createShouldGenerateIdWhenIdIsNull() {
        Car car = createCar(null, "BMW", "Black", 1);

        Car result = carRepository.create(car);

        assertNotNull(result.getCarId());
    }

    @Test
    void createShouldKeepIdWhenIdExists() {
        Car car = createCar("car-1", "BMW", "Black", 1);

        Car result = carRepository.create(car);

        assertEquals("car-1", result.getCarId());
    }

    @Test
    void findAllShouldReturnIteratorOverCars() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("car-1", iterator.next().getCarId());
    }

    @Test
    void findByIdShouldReturnCarWhenFound() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));

        Car found = carRepository.findById("car-1");
        assertNotNull(found);
        assertEquals("car-1", found.getCarId());
    }

    @Test
    void findByIdShouldReturnNullWhenNotFound() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));

        assertNull(carRepository.findById("missing"));
    }

    @Test
    void updateShouldModifyExistingCarWhenFound() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));
        Car updatedCar = createCar("car-2", "Toyota", "White", 3);

        Car result = carRepository.update("car-1", updatedCar);

        assertNotNull(result);
        assertEquals("car-1", result.getCarId());
        assertEquals("Toyota", result.getCarName());
        assertEquals("White", result.getCarColor());
        assertEquals(3, result.getCarQuantity());
    }

    @Test
    void updateShouldReturnNullWhenCarNotFound() {
        Car updatedCar = createCar("car-2", "Toyota", "White", 3);
        assertNull(carRepository.update("missing", updatedCar));
    }

    @Test
    void updateShouldFindMatchingCarAfterSkippingNonMatchingEntry() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));
        carRepository.create(createCar("car-2", "Honda", "Red", 2));
        Car updatedCar = createCar("car-3", "Toyota", "White", 3);

        Car result = carRepository.update("car-2", updatedCar);

        assertNotNull(result);
        assertEquals("car-2", result.getCarId());
        assertEquals("Toyota", result.getCarName());
    }

    @Test
    void deleteShouldRemoveMatchingId() {
        carRepository.create(createCar("car-1", "BMW", "Black", 1));
        carRepository.create(createCar("car-2", "Toyota", "White", 3));

        carRepository.delete("car-1");

        assertNull(carRepository.findById("car-1"));
        assertNotNull(carRepository.findById("car-2"));
    }

    @Test
    void deleteShouldDoNothingWhenIdNotFound() {
        carRepository.create(createCar("car-2", "Toyota", "White", 3));

        carRepository.delete("missing");

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        assertEquals("car-2", iterator.next().getCarId());
        assertFalse(iterator.hasNext());
    }

    private Car createCar(String id, String name, String color, int quantity) {
        Car car = new Car();
        car.setCarId(id);
        car.setCarName(name);
        car.setCarColor(color);
        car.setCarQuantity(quantity);
        return car;
    }
}
