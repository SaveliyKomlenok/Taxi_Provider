package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.dto.request.CarChangeStatusRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.exception.CarAlreadyExistsException;
import com.software.modsen.driverservice.exception.CarNotExistsException;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_ALREADY_EXISTS;
import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Mono<Car> getById(Long id) {
        return carRepository.findById(id)
                .switchIfEmpty(Mono.error(new CarNotExistsException(String.format(CAR_NOT_EXISTS, id))));
    }

    @Override
    public Flux<Car> getAll(Integer pageNumber, Integer pageSize, String sortBy, Boolean includeRestricted) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        if (includeRestricted != null && includeRestricted) {
            return carRepository.findAllByRestrictedIsTrue(pageable);
        } else {
            return carRepository.findAll();
        }
    }

    @Override
    public Mono<Car> save(Car car) {
        return carRepository.findCarByNumber(car.getNumber())
                .flatMap(existingCar -> Mono.error(new CarAlreadyExistsException(CAR_ALREADY_EXISTS)))
                .switchIfEmpty(carRepository.save(car)).cast(Car.class);
    }

    @Override
    public Mono<Car> update(Car car) {
        return getById(car.getId())
                .flatMap(existingCar -> carRepository.findCarByNumber(car.getNumber())
                        .filter(existingCarByNumber -> !existingCarByNumber.getId().equals(car.getId()))
                        .flatMap(existingCarByNumber -> Mono.error(new CarAlreadyExistsException(CAR_ALREADY_EXISTS)))
                        .switchIfEmpty(carRepository.save(car))).cast(Car.class);
    }

    @Override
    public Mono<Car> changeRestrictionsStatus(CarChangeStatusRequest request) {
        return getById(request.getId())
                .flatMap(car -> {
                    car.setRestricted(request.isStatus());
                    return carRepository.save(car);
                });
    }
}