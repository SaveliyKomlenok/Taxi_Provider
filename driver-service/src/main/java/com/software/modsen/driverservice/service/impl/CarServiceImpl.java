package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.dto.CarCreateRequest;
import com.software.modsen.driverservice.dto.CarResponse;
import com.software.modsen.driverservice.dto.CarUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.exception.CarIsNotExistsException;
import com.software.modsen.driverservice.repository.CarRepository;
import com.software.modsen.driverservice.service.CarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ModelMapper mapper;

    @Override
    public CarResponse getById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarIsNotExistsException(String.format(CAR_NOT_EXISTS, id)));
        return mapper.map(car, CarResponse.class);
    }

    @Override
    public List<CarResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return carRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent()
                .stream()
                .map(car -> mapper.map(car, CarResponse.class))
                .toList();
    }

    @Override
    public CarResponse save(CarCreateRequest request) {
        Car car = carRepository.save(mapper.map(request, Car.class));
        return mapper.map(car, CarResponse.class);
    }

    @Override
    public CarResponse update(CarUpdateRequest request) {
        carRepository.findById(request.getId())
                .orElseThrow(() -> new CarIsNotExistsException(String.format(CAR_NOT_EXISTS, request.getId())));
        Car car = carRepository.save(mapper.map(request, Car.class));
        return mapper.map(car, CarResponse.class);
    }

    @Override
    public CarResponse changeRestrictionsStatus(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarIsNotExistsException(String.format(CAR_NOT_EXISTS, id)));
        car.setRestricted(!car.isRestricted());
        carRepository.save(car);
        return mapper.map(car, CarResponse.class);
    }
}
