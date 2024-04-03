const movie_table = `CREATE TABLE Movie_Information
(
    id varchar(255) UNIQUE PRIMARY KEY,
    Country varchar(255) NOT NULL,
    State varchar(255) NOT NULL,
    City varchar(255) NOT NULL,
    Theater_name varchar(255) NOT NULL,
    Theater_id varchar(255) NOT NULL,
    Movie_id varchar(255) NOT NULL,
    Movie_name varchar(255),
    Available_Booking_data jsonb NOT NULL
)`;

module.export = movie_table;