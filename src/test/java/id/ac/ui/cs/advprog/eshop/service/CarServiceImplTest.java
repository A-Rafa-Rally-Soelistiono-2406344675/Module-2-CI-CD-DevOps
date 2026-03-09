package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarReadRepository;
import id.ac.ui.cs.advprog.eshop.repository.CarWriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @InjectMocks
    private CarServiceImpl carService;

    @Mock
    private CarReadRepository carReadRepository;

    @Mock
    private CarWriteRepository carWriteRepository;

    @Test
    void createShouldDelegateToRepositoryAndReturnCar() {
        Car car = new Car();

        Car result = carService.create(car);

        verify(carWriteRepository).create(car);
        assertSame(car, result);
    }

    @Test
    void findAllShouldConvertIteratorToList() {
        Car car1 = new Car();
        Car car2 = new Car();
        Iterator<Car> iterator = List.of(car1, car2).iterator();
        when(carReadRepository.findAll()).thenReturn(iterator);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertSame(car1, result.get(0));
        assertSame(car2, result.get(1));
    }

    @Test
    void findByIdShouldDelegateToRepository() {
        Car car = new Car();
        when(carReadRepository.findById("car-1")).thenReturn(car);

        Car result = carService.findById("car-1");

        verify(carReadRepository).findById("car-1");
        assertSame(car, result);
    }

    @Test
    void updateShouldDelegateToRepository() {
        Car car = new Car();

        carService.update("car-1", car);

        verify(carWriteRepository).update("car-1", car);
    }

    @Test
    void deleteCarByIdShouldDelegateToRepository() {
        carService.deleteCarById("car-1");

        verify(carWriteRepository).delete("car-1");
    }
}
