function createTransactionStoringObj(obj1)
{
  let paymentData = {
    cardholdername: obj1.payload.paymentData.cardHolderName,
    cardnumber: obj1.payload.paymentData.cardNumber,
    cvv: obj1.payload.paymentData.cvv,
    expirydate: obj1.payload.paymentData.expiryDate,
    paymentid: obj1.payload.paymentData.paymentId,
    postalcode: obj1.payload.paymentData.postalCode
  }

let bookingData = {
  bookingid: obj1.payload.bookingData.bookingId,
  cityname: obj1.payload.bookingData.cityName,
  countryname: obj1.payload.bookingData.countryName,
  date: obj1.payload.bookingData.date,
  moviename: obj1.payload.bookingData.movieName,
  requestid: obj1.payload.bookingData.requestId,
  requesttype: obj1.payload.bookingData.requestType,
  seating: obj1.payload.bookingData.seating,
  show_id: obj1.payload.bookingData.show_id,
  state: obj1.payload.bookingData.state,
  theater_id: obj1.payload.bookingData.theater_id,
  theater_name: obj1.payload.bookingData.theater_name,
  movieid : obj1.payload.bookingData.movieId,
  movieposter : obj1.payload.bookingData.moviePoster
}

  let trs = {
    bookingdata : bookingData, 
    paymentdata : paymentData,
    paymentstatus : obj1.payload.paymentStatus,
    requestid  : obj1.payload.requestId,
    requesttype : obj1.payload.requestType,
   }
   return trs;
}
module.exports = {createTransactionStoringObj};
