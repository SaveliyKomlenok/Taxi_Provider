package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.exception.CarAlreadyExistsException;
import com.software.modsen.driverservice.exception.CarNotExistsException;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_ALREADY_EXISTS;
import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car getById(Long id) {
        return getOrThrow(id);
    }

    @Override
    public List<Car> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted) {
        if (includeRestricted != null && includeRestricted) {
            return carRepository.findAllByRestrictedIsTrue(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy)));
        } else {
            return carRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent();
        }
    }

    @Override
    public Car save(Car car) {
        if(carRepository.findCarByNumber(
                car.getNumber()).isPresent()){
            throw new CarAlreadyExistsException(CAR_ALREADY_EXISTS);
        }
        return carRepository.save(car);
    }

    @Override
    public Car update(Car car) {
        getOrThrow(car.getId());
        Car existingCar = carRepository.findCarByNumber(
                        car.getNumber())
                .orElse(null);
        if (existingCar != null && !existingCar.getId().equals(car.getId())) {
            throw new CarAlreadyExistsException(CAR_ALREADY_EXISTS);
        }
        return carRepository.save(car);
    }

    @Override
    public Car changeRestrictionsStatus(Long id) {
        Car car = getOrThrow(id);
        car.setRestricted(!car.isRestricted());
        return carRepository.save(car);
    }

    private Car getOrThrow(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotExistsException(String.format(CAR_NOT_EXISTS, id)));
    }
}
