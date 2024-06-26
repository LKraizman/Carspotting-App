CREATE TABLE car_spots (
    car_spot_id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    car_spot_title varchar NOT NULL,
    spot_date varchar NOT NULL,
    car_model_id BIGINT REFERENCES car_models(car_model_id),
    description varchar NOT NULL,
    picture_url varchar NOT NULL
)