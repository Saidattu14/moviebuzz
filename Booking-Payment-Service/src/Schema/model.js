const movie_table = `CREATE TABLE Movie_Information
(
    Country varchar(255),
    State varchar(255) UNIQUE PRIMARY KEY,
    Theater_name varchar(255) NOT NULL,
    Theater_id int NOT NULL UNIQUE,
    Movie_id int NOT NULL,
    Movie_name varchar(255),
    Available_Booking_data jsonb NOT NULL
)`;

module.export = movie_table;