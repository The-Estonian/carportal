package ee.bcs.carportal.persistence.car;

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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Car partialUpdate(CarInfo carInfo, @MappingTarget Car car);
}