const WebSocketServer = require('websocket').server;
const movieBookingKafkaProducer = require('../kafka_producer/booking-payment-producer');
const movieReviewsKafkaProducer = require('../kafka_producer/review-rating-producer');
const transactionalHistoryProducer = require('../kafka_producer/transactional-producer')
const websocketClients = require('../model/websocketsClass');
const redis_client = require('../Redis/redis_client')
const checkJwtToken = async(token) => {
  try {
    let user = await redis_client.getAsync(String(token));
    if(user != null) 
    {
      return true;
    }
    return false;
  } catch (error) {
    console.log(error)
    return false;
  } 
}


const sendEvents = async(data,connection) => {
  try {
    let result = await checkJwtToken(data.token);
    console.log(result)
    delete data.token;
    if(data.requestType == "ValidateSelectedTickets" && result)
    {
      movieBookingKafkaProducer.validatebookingInformationEvent(data,(err) => {
        console.log(err)
      });
    }
    else if(data.requestType == "GetTheaters" && result)
    {
      movieBookingKafkaProducer.getTheatersAndTicketsEvent(data,(err) => {
        console.log(err)
      });
    }
    else if(data.requestType == "ValidatePayment" && result)
    {
      movieBookingKafkaProducer.validatePaymentDataEvent(data,(err) => {
        console.log(err)
      });
    }
    else if(data.requestType == "GetReviews" && result)
    {
      console.log("hello")
      movieReviewsKafkaProducer.getReviewsEvent(data,(err) => {
        console.log(err);
      })
    }
    else if(data.requestType == "GetBookingHistoryDetails" && result)
    {
      transactionalHistoryProducer.getTransactionsHistoryEvent(data,(err) => {
        console.log(err)
      })
    }
    websocketClients.addrequest(connection,data)
  } catch (error) {
    console.log(error)
  }
}

const setupWebSocketServer = (server) => {
const wsServer = new WebSocketServer({
    httpServer: server,
    autoAcceptConnections: false
  });
  
  function originIsAllowed(origin) {
  // put logic here to detect whether the specified origin is allowed.
  return true;
  }
  
  wsServer.on('request', function(request) {
    if (!originIsAllowed(request.origin)) {
      // Make sure we only accept requests from an allowed origin
      request.reject();
      console.log((new Date()) + ' Connection from origin ' + request.origin + ' rejected.');
      return;
    }
    var connection = request.accept('echo-protocol', request.origin);
    websocketClients.addClients(connection);
    console.log((new Date()) + ' Connection accepted.');
    
    connection.on('message', function(message) {
        if (message.type === 'utf8') {
            console.log('Received Message: ' + message.utf8Data);
            let json = message.utf8Data;
            sendEvents(JSON.parse(json),connection)
            //connection.sendUTF(message.utf8Data);
        }

        else if (message.type === 'binary') {
            //console.log('Received Binary Message of ' + message.binaryData.length + ' bytes');
            connection.sendBytes(message.binaryData);
        }
    });
    connection.on('close', function(reasonCode, description) {
        websocketClients.deleteClient(connection);
        //console.log(websocketClients.getClientsSize());
        console.log((new Date()) + ' Peer ' + connection.remoteAddress + ' disconnected.');
    });
  });
  return wsServer;
}

module.exports = {setupWebSocketServer};