const cors = require('cors');
const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./routes');
const {setupWebSocketServer} = require('./WebSockets/websocketServer')
//const kafka_admin = require('./kafka_producer/admin_kafka')
const kafkaConsumer = require('./Kafka_consumer/consumer');
const redis_client = require('./Redis/redis_client');
const session = require("express-session");
const websocketClients = require('./model/websocketsClass');
let RedisStore = require("connect-redis")(session)
const app = express();

app.use(
  session({
    store: new RedisStore({ client: redis_client }),
    saveUninitialized: false,
    secret: "keyboard cat",
    resave: false,
  })
)
app.use(bodyParser.json());
app.use(cors());
app.use('/api/v1', routes);


async function sendResponseData(data) 
{
  try {
    let connection = websocketClients.getClient(data.requestId);
    websocketClients.sendDataToClient(connection,data);
    websocketClients.removeRequest(data.requestId)
  } catch (error) {
    console.log(error)
  }
}

kafkaConsumer.consumer.connect();
kafkaConsumer.consumer.on('ready', () => {
  console.log('Kafka Consumer Connected')
  kafkaConsumer.consumer.subscribe(['ResponseTheatersTicketsDetailsTopic','ResponseTransactionalStorageTopic',
  'ResponseReviewsTopic','ResponseBookingPaymentTopic']);
  kafkaConsumer.consumer.consume();
}).on('data', async(data) => {
   try {
        let message = JSON.parse(data.value.toString())
        let event_type = message.eventType;
        console.log(message)
        if (event_type == "reviewsInformation")
        {
          
          await sendResponseData(message.payload);
          kafkaConsumer.consumer.commitMessage(data)
        }
        else if(event_type == 'theatersAndTicketsInformation')
        {
          await sendResponseData(message.payload);
          kafkaConsumer.consumer.commitMessage(data);
        }
        else if (event_type == "paymentValidationResponse")
        {
          await sendResponseData(message.payload);
          kafkaConsumer.consumer.commitMessage(data)
        }
        else if(event_type == "bookingValidationResponse")
        {
          await sendResponseData(message.payload);
          kafkaConsumer.consumer.commitMessage(data);
        }
        else if(event_type == 'transactionalHistoryResponse')
        {
          await sendResponseData(message.payload);
          kafkaConsumer.consumer.commitMessage(data);
        }        
        else
        {
          kafkaConsumer.consumer.commitMessage(data);
        }
    }
   catch (err) {
        console.error(err);
    }
});

const server = app.listen(8005,() => {
 console.log('server listening on 8005');
});
setupWebSocketServer(server);