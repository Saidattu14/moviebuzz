const kafka = require('./Kafka_consumer/booking-payment_consumer')
const kafka_producer = require('./Kafka_producer/payment-producer')
const {transactionQuery} = require('./Schema/tranasction')
const {Client} = require('pg');
const {checkBookingAvailabilityQuery} = require('./Schema/check_booking')
const {getTheatersTickets} = require('./Schema/getTickets');
const fs = require('fs')
const client = new Client({
    user: 'user',
    host: 'localhost',
    password: "123456".toString(),
    port: 5432,
    database : 'default_database'
})

try {
  client.connect();
  //getTheatersTicketsData(null)
} catch (error) {
  console.log("DataBase is not Connected")
}


async function checkAvailableBookingData(data,client)
{
   try {
   
    let result = await checkBookingAvailabilityQuery(data,client);
    //console.log(result)
    //console.log(data)
    kafka_producer.sendBookingStatusEvent(result,(err) => {
      console.log(err)
    })
   } catch (error) {
     console.log(error)
   }
}


async function updateBookingData(data)
{
 let tickets = data.bookingData;
 let result = await transactionQuery(tickets,client);
  if(result == 'Success')
  {
    data.paymentStatus = 'TransactionDone';
  }
  else
  {
    data.paymentStatus =  'TransactionFailed';
  }
  kafka_producer.sendPaymentStatusEvent(data,(err) => {
    console.log(err)
  })
  kafka_producer.sendTransactionStoreEvent(data,(err) => {
    console.log(err)
  })
}

async function getTheatersTicketsData(data)
{
    try {
      let ticketsData = await getTheatersTickets(data,client);
      let obj = {
        "requestId"  : data.requestId,
        "requestType" : data.requestType,
        "theatersAndTicketsModelList" : ticketsData
      }
      console.log(ticketsData)
      kafka_producer.sendAvalilableTheatersAndTicketsEvent(obj,(err)=> {
        console.log(err)
      });
    } catch (error) {
      console.log(error);
    }
}


kafka.consumer.connect();
kafka.consumer.on('ready', () => {
  console.log('Booking and Payment consumer ready!')
  kafka.consumer.subscribe(['RequestTheatersTicketsDetailsTopic','RequestBookingPaymentTopic'])
  kafka.consumer.consume();
}).on('data', (data) => {
  
   try {
        let message = JSON.parse(data.value.toString())
        console.log(message)
        let event_type = message.eventType
        if (event_type == 'validateBookingInformation')
        {

          checkAvailableBookingData(message.payload,client)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == 'validatePayment')
        {
          updateBookingData(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == 'getTheatersAndTickets')
        {
        
          getTheatersTicketsData(message.payload);
          kafka.consumer.commitMessage(data);
        }

        else
        {
          kafka.consumer.commitMessage(data)
        }
    }
   catch (err) {
        console.error(err);
    }
});