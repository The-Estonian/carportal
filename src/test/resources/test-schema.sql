-- Explicitly create sequence for H2 (needed due to case sensitivity)
CREATE SEQUENCE CAR_ID_SEQ START WITH 1 INCREMENT BY 1;

-- Create tables and constraints

-- tables
-- Table: car
CREATE TABLE car
(
    id              int DEFAULT NEXTVAL('CAR_ID_SEQ'),
    manufacturer_id int           NOT NULL,
    fuel_type_id    int           NOT NULL,
    model           varchar(255)  NOT NULL,
    "year"          int           NOT NULL,
    emissions       decimal(3, 2) NOT NULL,
    price           int           NOT NULL,
    CONSTRAINT car_pk PRIMARY KEY (id)
);

-- Ensure 'CAR_ID_SEQ' starts fresh for new inserts
ALTER SEQUENCE CAR_ID_SEQ RESTART WITH 1;

-- Table: fuel_type
CREATE TABLE fuel_type
(
    id   serial       NOT NULL,
    name varchar(255) NOT NULL,
    code char(1)      NOT NULL,
    CONSTRAINT fuel_type_pk PRIMARY KEY (id)
);

-- Table: fuel_type_tax
CREATE TABLE fuel_type_tax
(
    id           serial        NOT NULL,
    fuel_type_id int           NOT NULL,
    tax_type_id  int           NOT NULL,
    adjustment   decimal(4, 1) NOT NULL,
    CONSTRAINT fuel_type_tax_pk PRIMARY KEY (id)
);

-- Table: manufacturer
CREATE TABLE manufacturer
(
    id   serial       NOT NULL,
    name varchar(255) NOT NULL,
    CONSTRAINT manufacturer_pk PRIMARY KEY (id)
);

-- Table: tax_type
CREATE TABLE tax_type
(
    id                    serial        NOT NULL,
    name                  varchar(255)  NOT NULL,
    code                  char(1)       NOT NULL,
    base_value            decimal(4, 1) NOT NULL,
    emissions_adjustment  decimal(4, 1) NOT NULL,
    model_year_multiplier decimal(4, 1) NOT NULL,
    CONSTRAINT tax_type_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: car_fuel_type (table: car)
ALTER TABLE car
    ADD CONSTRAINT car_fuel_type
        FOREIGN KEY (fuel_type_id)
            REFERENCES fuel_type (id);

-- Reference: car_manufacturer (table: car)
ALTER TABLE car
    ADD CONSTRAINT car_manufacturer
        FOREIGN KEY (manufacturer_id)
            REFERENCES manufacturer (id);

-- Reference: fuel_type_tax_fuel_type (table: fuel_type_tax)
ALTER TABLE fuel_type_tax
    ADD CONSTRAINT fuel_type_tax_fuel_type
        FOREIGN KEY (fuel_type_id)
            REFERENCES fuel_type (id);

-- Reference: fuel_type_tax_tax_type (table: fuel_type_tax)
ALTER TABLE fuel_type_tax
    ADD CONSTRAINT fuel_type_tax_tax_type
        FOREIGN KEY (tax_type_id)
            REFERENCES tax_type (id);

-- End of file.

