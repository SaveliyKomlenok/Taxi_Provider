package com.software.modsen.driverservice.service.impl;

import com.software.modsen.driverservice.dto.DriverCreateRequest;
import com.software.modsen.driverservice.dto.DriverResponse;
import com.software.modsen.driverservice.dto.DriverUpdateRequest;
import com.software.modsen.driverservice.entity.Car;
import com.software.modsen.driverservice.entity.Driver;
import com.software.modsen.driverservice.exception.CarIsOccupiedException;
import com.software.modsen.driverservice.exception.DriverIsNotExistsException;
import com.software.modsen.driverservice.repository.DriverRepository;
import com.software.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.software.modsen.driverservice.util.ExceptionMessages.CAR_OCCUPIED;
import static com.software.modsen.driverservice.util.ExceptionMessages.DRIVER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final CarServiceImpl carService;
    private final DriverRepository driverRepository;
    private final ModelMapper mapper;

    @Override
    public DriverResponse getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverIsNotExistsException(String.format(DRIVER_NOT_EXISTS, id)));
        return mapper.map(driver, DriverResponse.class);
    }

    @Override
    public List<DriverResponse> getAll(Integer pageNumber, Integer pageSize, String sortBy) {
        return driverRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))).getContent()
                .stream()
                .map(driver -> mapper.map(driver, DriverResponse.class))
                .toList();
    }

    @Override
    public DriverResponse save(DriverCreateRequest request) {
        Driver driver = mapper.map(request, Driver.class);
        driver.setCar(checkCarOccupancy(request.getCar()));
        return mapper.map(driverRepository.save(driver), DriverResponse.class);
    }

    @Override
    public DriverResponse update(DriverUpdateRequest request) {
        driverRepository.findById(request.getId())
                .orElseThrow(() -> new DriverIsNotExistsException(String.format(DRIVER_NOT_EXISTS, request.getId())));
        Driver driver = mapper.map(request, Driver.class);
        driver.setCar(checkCarOccupancy(request.getCar()));
        return mapper.map(driverRepository.save(driver), DriverResponse.class);
    }

    private Car checkCarOccupancy(Long id) {
        Car car = mapper.map(carService.getById(id), Car.class);
        if (driverRepository.findDriverByCarId(id).isPresent()) {
            throw new CarIsOccupiedException(String.format(CAR_OCCUPIED, id));
        }
        return car;
    }

    @Override
    public DriverResponse changeRestrictionsStatus(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverIsNotExistsException(String.format(DRIVER_NOT_EXISTS, id)));
        driver.setRestricted(!driver.isRestricted());
        driverRepository.save(driver);
        return mapper.map(driver, DriverResponse.class);
    }
}
