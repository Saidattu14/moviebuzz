const cors = require('cors');
const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./routes');
const {setupWebSocketServer} = require('./WebSockets/websocketServer')
// const kafka_admin = require('./kafka_producer/admin_kafka')
const kafkaConsumer = require('./Kafka_consumer/consumer');
const redis_client = require('./Redis/redis_client');
const session = require("express-session");
const websocketClients = require('./model/websocketsClass');
const RedisStore = require("connect-redis").default

const {updateAllMoviesTrendingReviews} = require('./Elastic_Search/search');

const kafka_producer = require('./kafka_producer/review-rating-producer');

let redisStore = new RedisStore({
  client: redis_client,
  prefix: "myapp:",
})

const app = express();

app.use(
  session({
    store: redisStore,
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
  //  kafkaConsumer.consumer.subscribe([
  // 'ResponseReviewsTopic','Avg']);
  kafkaConsumer.consumer.consume();
}).on('data', async(data) => {
   try {
        let message = JSON.parse(data.value.toString())
        let event_type = message.eventType;
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
        else if(event_type == 'trendingMovieReviewsInformation')
        {
          
          updateAllMoviesTrendingReviews(message.payload)
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
 let dt = {
  "movieId" : 'tt0499097',
  "reviewId" : 'rw6864223',
  "likedCount" : 100,
  "disLikedCount" : 200,
 }
 
//  kafka_producer.producer.on('ready',() => {
//    kafka_producer.upLikesReviewsEvent(dt,(err) => {
//     console.log(err)
//    })
//  })

 
 
});
setupWebSocketServer(server);
