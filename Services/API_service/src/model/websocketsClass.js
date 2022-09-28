const Kafka = require('node-rdkafka');
const { connection } = require('websocket');
class WebSocketsClient {
    
    constructor()
    {
      this.requests = {}
      this.clients = {}
      this.clientsList = []
    }
    getClient(requestId)
    {
       if(this.requests[requestId] != undefined)
       {
        return this.requests[requestId];
       }     
       return null;
    }
    addClients(connection)
    {
        this.clientsList.push(connection);
        this.clients[connection] = this.clientsList.length-1;
        
    }
    addrequest(connection,data)
    {
        this.requests[data.requestId] = connection;
    }
    deleteClient(connection)
    {
        let index = this.clients[connection];
        this.clientsList.splice(index,1);
        delete this.clientsList[this.clients[connection]];
        delete this.clients.connection;
    }
    removeRequest(requestId)
    {
       delete this.requests.requestId;
    }
    getClientsSize()
    {
       return this.clientsList.length;
    }
    sendDataToClient(connection,data)
    {
        connection.sendUTF(JSON.stringify(data));
    }
}
const websocketClients = new WebSocketsClient();
Object.freeze(websocketClients)
module.exports = websocketClients;