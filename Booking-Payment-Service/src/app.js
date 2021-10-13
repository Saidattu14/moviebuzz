const kafka = require('./Kafka_consumer/booking-payment_consumer')
const kafka_producer = require('./Kafka_producer/payment-producer')
const Query = require('./Schema/Query')
const Transaction = require('./Schema/tranasction')
const { v4: uuidv4 } = require('uuid');
const {Client} = require('pg');
const client = new Client({
    user: 'user',
    host: 'localhost',
    password: '123456',
    port: 5431,
    database : 'default_database'
})

try {
  client.connect();
} catch (error) {
  console.log("DataBase is not Connected")
}


async function check_booking(data)
{

  let seats = data.seating
  var arr = []
  for(let i=0;i<seats.length;i++)
  {
  
    for(let j = 0;j<seats[i].seat_number.length;j++)
    {
      let seat_number = seats[i].seat_number[j];
      let row_id = seats[i].row_id;
      let obj = {
        "state" : data.state,
        "country_name" : data.country_name,
        "movie_name" : data.movie_name,
        "theater_id" : data.theater_id,
        "Date" : data.Date,
        "show_id" : data.show_id,
        "seat_id" : seat_number,
        "row_id" : row_id
        
      }
      let result = await Query(obj,client);
      obj = {
        "row_id" : row_id,
        "seat_id" : seat_number,
        "result" : result,
        "state" : data.state,
        "country_name" : data.country_name,
        "movie_name" : data.movie_name,
        "theater_id" : data.theater_id,
        "Date" : data.Date,
        "show_id" : data.show_id,
      }
      arr.push(obj)
    }
  }
   let count = 0;
  for(let i = 0; i<arr.length;i++)
  {
    if(arr[i].result == 'UNAVAILAVLE')
    {
     count++;
     break;
    }
    if(count != 0)
    {
      break;
    }
  }
  if(count == 0)
  {
    let data1 = {
      "request_id" : data.request_id,
      "payment_id" : uuidv4(),
      "price"      :  arr.length * 150,
      "booking_data" : arr,
    }
    kafka_producer.booking_status(data1,(err) => {
      console.log(err)
    })
  }
  else
  {
    let data1 = {
      "request_id" : data.request_id,
      "payment_id" : null,
      "booking_data" : arr,
    }
    kafka_producer.booking_status(data1,(err) => {
      console.log(err)
    })
  }
 
  // Query(data,client);
  // kafka_producer.booking_status(data,(err) => {
  //   console.log(err)
  // });
}
async function update_booking(data)
{
 let tickets = data.booking_details.details;
 
let result = await Transaction(tickets,client);
console.log(result)

}




kafka.consumer.connect();
kafka.consumer.on('ready', () => {
  console.log('Booking and Payment consumer ready!')
  kafka.consumer.subscribe(['Booking-Payment'])
  kafka.consumer.consume();
}).on('data', (data) => {
  
   try {
        let message = JSON.parse(data.value.toString())
        let event_type = message.eventType
        if (event_type == "Booking information")
        {
          check_booking(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "Payment")
        {
          update_booking(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else
        {
          
        }
    }
   catch (err) {
        console.error(err)
        
    }
});