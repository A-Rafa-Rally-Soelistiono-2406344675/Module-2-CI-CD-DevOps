package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {
    private static final String CAR_KEY = "car";
    private static final String CAR_ID = "car-1";

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @Mock
    private Model model;

    @Test
    void createCarPageShouldReturnCreateCarViewAndSetModel() {
        String viewName = carController.createCarPage(model);

        ArgumentCaptor<Car> carCaptor = ArgumentCaptor.forClass(Car.class);
        verify(model).addAttribute(eq(CAR_KEY), carCaptor.capture());
        assertNotNull(carCaptor.getValue());
        assertEquals("CreateCar", viewName);
    }

    @Test
    void createCarPostShouldCreateCarAndRedirectToListCar() {
        Car car = new Car();

        String viewName = carController.createCarPost(car);

        verify(carService).create(car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    void carListPageShouldSetCarsInModelAndReturnCarListView() {
        List<Car> cars = List.of(new Car(), new Car());
        when(carService.findAll()).thenReturn(cars);

        String viewName = carController.carListPage(model);

        verify(model).addAttribute("cars", cars);
        assertEquals("CarList", viewName);
    }

    @Test
    void editCarPageShouldSetCarInModelAndReturnEditCarView() {
        Car car = new Car();
        when(carService.findById(CAR_ID)).thenReturn(car);

        String viewName = carController.editCarPage(CAR_ID, model);

        verify(model).addAttribute(CAR_KEY, car);
        assertEquals("EditCar", viewName);
    }

    @Test
    void editCarPostShouldUpdateCarAndRedirectToListCar() {
        Car car = new Car();
        car.setCarId(CAR_ID);

        String viewName = carController.editCarPost(car);

        verify(carService).update(CAR_ID, car);
        assertEquals("redirect:listCar", viewName);
    }

    @Test
    void deleteCarShouldDeleteByIdAndRedirectToListCar() {
        String viewName = carController.deleteCar(CAR_ID);

        verify(carService).deleteCarById(CAR_ID);
        assertEquals("redirect:listCar", viewName);
    }
}
