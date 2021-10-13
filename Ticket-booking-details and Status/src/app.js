const morgan = require('morgan');
const cors = require('cors');
const routes = require('./routes');
const express = require('express');
const bodyParser = require('body-parser');
const app = express();
const kafka = require('./kafka-conusmer/payment-consumer')
const redis_client = require('./Redis/client')


app.use(bodyParser.json());
app.use(cors());
app.use('/status', routes);



async function update_booking_status(data)
{

  let a = await redis_client.getAsync(data.request_id);
  if(data.payment_id == null)
  {
    let obj = {
      "status" : "Booking Failed Tickets are unavailable",
      "details" : data.booking_data
    }
    let a = await redis_client.setAsync(
      data.request_id,
      JSON.stringify(obj),
      'EX',
      300
    )
    let b = await redis_client.getAsync(data.request_id) 

  }
  else
  {
    let obj = {
      "payment_id" : data.payment_id,
      "status" : "Booking Tickets are Available",
      "details" : data.booking_data,
      "price" : data.price
    }
    
    await redis_client.setAsync(
      data.request_id,
      JSON.stringify(obj),
      'EX',
       300
    )

    await redis_client.setAsync(
      data.payment_id,
      "Payment Not Started",
      'EX',
       300
    )
  }
}
async function update_payment_status(data)
{
  try {
    let obj = {
      "Transaction_id" : data.Transaction_id,
      "Payment_id" : data.payment_data.payment_id,
      "Booking_details" : data.details,
      "price" : data.payment_data.price,
      "mobile_no" : data.payment_data.mobile_no,
      "Date" : new Date(),
      "Status" : data.status
    }
    console.log(obj)
    await redis_client.hsetAsync(
      "Transaction_data",
      data.Tranasction_id,
      JSON.stringify(obj),
    );
    await redis_client.setAsync(
      data.payment_data.payment_id,
      data.status,
      'EX',
      300
    )
  } catch (error) {
    console.log(error)
  }
 
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
        if (event_type == "Payment-status")
        {
          update_payment_status(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "Booking-status")
        {
          
          update_booking_status(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else
        {
            kafka.consumer.commitMessage(data)
        }
    }
   catch (err) {
        console.error(err)
        
    }
});



app.listen(8001,() => {
 console.log('server listening on 8001')
})