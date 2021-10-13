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
    
    let a = await redis_client.setAsync(
      data.request_id,
      JSON.stringify(obj),
      'EX',
       300
    )

    let b = await redis_client.setAsync(
      data.payment_id,
      "Payment Not Started",
      'EX',
       300
    )
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