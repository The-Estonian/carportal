package ee.bcs.carportal.persistence.car;

import ee.bcs.carportal.persistence.fueltype.FuelType;
import ee.bcs.carportal.persistence.manufacturer.Manufacturer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "car_id_gen")
    @SequenceGenerator(name = "car_id_gen", sequenceName = "car_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fuel_type_id", nullable = false)
    private FuelType fuelType;

    @Size(max = 255)
    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "\"year\"", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "emissions", nullable = false, precision = 3, scale = 2)
    private BigDecimal emissions;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

}