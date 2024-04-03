const {queryAvailability} = require('./Query')
const { v4: uuidv4 } = require('uuid');

const createQueryObj = (data,seat_number,row_id) => {    
      let obj = {
        "city" : data.cityName,
        "state" : data.state,
        "country_name" : data.countryName,
        "movie_name" : data.movieName,
        "theater_id" : data.theater_id,
        "Date" : data.date,
        "show_id" : data.show_id,
        "seat_id" : seat_number,
        "row_id" : row_id,
      }
    return obj;
}


const createQueryResultObj = (result,obj) => {
    if(result == "AVAILABLE" || result == "UNAVAILAVLE")
    {
      obj.result = result;
    }
    else
    {
        obj.result = "UNAVAILABLE";
    }
    return obj;
}

const createPaymentId = (arr,data) => {
  let count = 0;
  for(let i = 0; i<arr.length;i++)
  {
    if(arr[i].result == 'UNAVAILAVLE')
    {
     count++;
     break;
    }
  }
  console.log(count)
  let response_obj;
  if(count == 0)
  {
    response_obj = {
        "payment_id" : uuidv4(),
        "booking_data" : arr,
        "status" : "Success",
        "requestId"  : data.requestId,
    }
  }
  else
  {
    response_obj = {
        "payment_id" : null,
        "booking_data" : arr,
        "status" : "Failed",
        "requestId"  : data.requestId,
    }
  }
  return response_obj;

}


const checkBookingAvailabilityQuery = async(data,client) => {
  let seats = data.seating
  var arr = []
  for(let i=0;i<seats.length;i++)
  {
    for(let j = 0;j<seats[i].seat_numbers.length;j++)
    {
        let obj = createQueryObj(data,seats[i].seat_numbers[j],seats[i].row_id)
        let result = await queryAvailability(obj,client);
        let resultObj = createQueryResultObj(result,obj); 
        arr.push(resultObj)
    }
  }
  let responseData = createPaymentId(arr,data);
  return responseData;
}
module.exports = {checkBookingAvailabilityQuery};