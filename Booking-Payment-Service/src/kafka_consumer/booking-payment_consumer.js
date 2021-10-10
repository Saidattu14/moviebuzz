const Kafka = require('node-rdkafka');

class KafkaconsumerproducerClass {
    constructor()
    {
       var consumer = new Kafka.KafkaConsumer({
        'group.id': 'kafka-consumer-Booking-Payment',
        'metadata.broker.list': ['localhost:9092','localhost:9093','localhost:9094'],
       });
       
       consumer.on('event.error', function(err) {
        console.error('Error from Consumer:' + JSON.stringify(err));
        });
        var producer = new Kafka.Producer({
            'client.id': 'kafka-Producer-Store-Transaction',
            'metadata.broker.list': ['localhost:9092','localhost:9093','localhost:9094'],
            'compression.codec': 'gzip',
            'retry.backoff.ms': 200,
            'message.send.max.retries': 10,
            'socket.keepalive.enable': true,
            'queue.buffering.max.messages': 100000,
            'queue.buffering.max.ms': 1000,
            'batch.num.messages': 1000000,
            'dr_cb': true
           });
        producer.on('event.error', function(err) {
        console.error('Error from producer:' + JSON.stringify(err));
        });
        producer.on('delivery-report', function(err, report) {
            if (err) {
                console.error(err);
            } else {
                console.log(report);
            }
        });
     
        this.producer = producer;
        this.consumer = consumer;
        
    }
    on(event, callback) {
        this.consumer.on(event, callback)
    }
    on(event, callback) {
        this.producer.on(event, callback)
    }
    Store_transaction(data, callback) {
        let topicName = 'Transaction'
        let eventType = 'Store_transaction_details'
        let message = Buffer.from(JSON.stringify({eventType, payload: data}))
        try {
            this.producer.produce(
                topicName,
                null,
                message,
                Date.now()
            )
            callback(null)
        } catch (err) {
            console.log('caught')
            callback(err)
        }
    }
    
}
const kafka = new KafkaconsumerproducerClass()
Object.freeze(kafka)

module.exports = kafka;



