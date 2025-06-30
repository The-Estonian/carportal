-- Insert sample data

INSERT INTO public.fuel_type (id, name, code) VALUES (DEFAULT, 'ELECTRIC', 'E');
INSERT INTO public.fuel_type (id, name, code) VALUES (DEFAULT, 'HYBRID', 'H');
INSERT INTO public.fuel_type (id, name, code) VALUES (DEFAULT, 'PETROL', 'P');

INSERT INTO public.tax_type (id, name, code, base_value, emissions_adjustment, model_year_multiplier) VALUES (DEFAULT, 'REGISTRATION', 'R', 5.0, 10.0, 0.2);
INSERT INTO public.tax_type (id, name, code, base_value, emissions_adjustment, model_year_multiplier) VALUES (DEFAULT, 'ANNUAL', 'A', 50.0, 500.0, 2.0);

INSERT INTO public.manufacturer (id, name) VALUES (DEFAULT, 'Tesla');
INSERT INTO public.manufacturer (id, name) VALUES (DEFAULT, 'Honda');
INSERT INTO public.manufacturer (id, name) VALUES (DEFAULT, 'Toyota');
INSERT INTO public.manufacturer (id, name) VALUES (DEFAULT, 'Ford');

INSERT INTO public.car (id, model, "year", emissions, price, manufacturer_id, fuel_type_id) VALUES (DEFAULT, 'Model 3', 2020, 0.00, 44000, 1, 1);
INSERT INTO public.car (id, model, "year", emissions, price, manufacturer_id, fuel_type_id) VALUES (DEFAULT, 'Civic', 2021, 0.05, 25000, 2, 3);
INSERT INTO public.car (id, model, "year", emissions, price, manufacturer_id, fuel_type_id) VALUES (DEFAULT, 'Camry', 2022, 0.04, 28000, 3, 3);
INSERT INTO public.car (id, model, "year", emissions, price, manufacturer_id, fuel_type_id) VALUES (DEFAULT, 'F-150', 2023, 0.10, 45000, 4, 3);
INSERT INTO public.car (id, model, "year", emissions, price, manufacturer_id, fuel_type_id) VALUES (DEFAULT, 'Prius', 2020, 0.03, 30000, 3, 2);

INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 1, 1, -1.5);
INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 2, 1, -0.5);
INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 3, 1, 1.5);
INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 1, 2, 0.0);
INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 2, 2, 20.0);
INSERT INTO public.fuel_type_tax (id, fuel_type_id, tax_type_id, adjustment) VALUES (DEFAULT, 3, 2, 30.0);