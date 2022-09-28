const createTransactionTableQuery =  `CREATE TABLE IF NOT EXISTS TransactionalStorage.storage (
    userId UUID,
    time_stamp TIMESTAMP,
    transactions frozen<userTransaction>,
    PRIMARY KEY (userId,time_stamp))
    WITH CLUSTERING ORDER BY (time_stamp DESC)
    `;
  
const userTransactionDataTypeQuery =  `CREATE TYPE IF NOT EXISTS TransactionalStorage.userTransaction (
   paymentData frozen<paymentData>,
   bookingData frozen<bookingData>,
   paymentStatus TEXT,
   requestId  UUID,
   requestType TEXT,
   time_stamp TIMESTAMP,
  )`;
  const paymentTransactionDataTypeQuery =  `CREATE TYPE IF NOT EXISTS TransactionalStorage.paymentData (
    cardHolderName TEXT,
    cardNumber TEXT,
    cvv TEXT,
    expiryDate TEXT,
    paymentId UUID,
    postalCode TEXT
    )`;
    
  const bookingTransactionDataTypeQuery =  `CREATE TYPE IF NOT EXISTS TransactionalStorage.bookingData (
    bookingId  UUID,
    cityName  TEXT,
    countryName TEXT,
    date  TEXT,
    movieId TEXT,
    moviePoster TEXT,
    movieName TEXT,
    requestId UUID,
    requestType TEXT,
    seating frozen<list<seating>>,
    show_id  TEXT,
    state  TEXT,
    theater_id  TEXT,
    theater_name TEXT
  )`;
  
  const seatingDataTypeQuery =  `CREATE TYPE IF NOT EXISTS TransactionalStorage.seating (
    row_id TEXT,
    seat_numbers list<text>,
  )`;
  

  module.exports = {
    createTransactionTableQuery,
    paymentTransactionDataTypeQuery,
    bookingTransactionDataTypeQuery,
    seatingDataTypeQuery,
    userTransactionDataTypeQuery,
  };