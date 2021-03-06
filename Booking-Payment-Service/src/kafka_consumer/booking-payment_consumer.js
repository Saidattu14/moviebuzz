const Kafka = require('node-rdkafka');

class KafkaconsumerClass {
    constructor()
    {
       var consumer = new Kafka.KafkaConsumer({
        'group.id': 'kafka-consumer-Booking-Payment',
        'metadata.broker.list': ['localhost:9092','localhost:9093','localhost:9094'],
       });
       
       consumer.on('event.error', function(err) {
        console.error('Error from Consumer:' + JSON.stringify(err));
        });
       
        this.consumer = consumer;
        
    }
    on(event, callback) {
        this.consumer.on(event, callback)
    } 
}
const kafka = new KafkaconsumerClass()
Object.freeze(kafka)

module.exports = kafka;