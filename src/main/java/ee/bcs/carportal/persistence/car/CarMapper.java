package ee.bcs.carportal.persistence.car;

import java.util.List;

import org.mapstruct.*;

import ee.bcs.carportal.service.car.dto.CarInfo;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    @Mapping(source="car.manufacturer.name", target="make")
    @Mapping(source="car.model", target="modelName")
    @Mapping(source="car.year", target="releaseYear")
    public CarInfo toCarInfo(Car car);

    public List<CarInfo> toCarInfos(List<Car> cars);
}
