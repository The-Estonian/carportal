package ee.bcs.carportal.persistence.car;

import java.util.List;

import org.mapstruct.*;

import ee.bcs.carportal.service.car.dto.CarDetailedInfo;
import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    @Mapping(source = "car.manufacturer.name", target = "make")
    @Mapping(source = "car.model", target = "modelName")
    @Mapping(source = "car.year", target = "releaseYear")
    public CarInfo toCarInfo(Car car);

    @InheritConfiguration(name = "toCarInfo")
    @Mapping(source = "car.fuelType.name", target = "fuelType")
    @Mapping(source = "car.emissions", target = "emissions")
    @Mapping(source = "car.price", target = "price ")
    public CarDetailedInfo toCarDetailedInfo(Car car);

    public List<CarInfo> toCarInfos(List<Car> cars);

    @Mapping(source = "manufacturerId", target = "manufacturer.id")
    @Mapping(source = "fuelTypeId", target = "fuelType.id")
    @Mapping(source = "model", target = "model")
    @Mapping(source = "year", target = "year")
    @Mapping(source = "emissions", target = "emissions")
    @Mapping(source = "price", target = "price")
    public Car toCar(CarDto carDto);
}
