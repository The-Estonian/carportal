package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.service.car.dto.CarDto;
import ee.bcs.carportal.service.car.dto.CarInfo;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {
    @Mapping(target="make", source ="manufacturer.name")
    @Mapping(target="modelName", source ="model")
    @Mapping(target="releaseYear", source="year")

    CarInfo toCarInfo(Car car);
    List<CarInfo> toCarInfos(List<Car> cars);

    @Mapping(source = "model",     target = "model")
    @Mapping(source = "year",      target = "year")
    @Mapping(source = "emissions", target = "emissions")
    @Mapping(source = "price",     target = "price")
    Car toCar(CarDto carDto);
}