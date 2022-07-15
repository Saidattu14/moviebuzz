const Kafka = require('node-rdkafka');

class KafkaProducer {
    constructor()
    {
       var producer = new Kafka.Producer({
        'client.id': 'kafka-Producer-Reviews-Rating',
        'metadata.broker.list': ['localhost:9092','localhost:9093'],
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
        producer.connect();
        this.producer = producer;
    }
    on(event, callback) {
        this.producer.on(event, callback)
    }
    add_review(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'add_review'
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
    update_review(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'update_review'
        let message = Buffer.from(JSON.stringify({eventType, payload: data}))
        try {
            this.producer.produce(
                topicName,
                null,
                message,
                Date.now()
            );
            console.log("message sent")
            callback(null)
        } catch (err) {
            console.log('caught')
            callback(err)
        }
    }
    delete_review(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'delete_review'
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
    add_rating(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'add_rating'
        let message = Buffer.from(JSON.stringify({eventType, payload: data}))
        try {
            this.producer.produce(
                topicName,
                null,
                message,
                Date.now()
        
            )
            console.log("Message sent")
            callback(null)
        } catch (err) {
            console.log('caught')
            callback(err)
        }
    }
    update_rating(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'update_rating'
        let message = Buffer.from(JSON.stringify({eventType, payload: data}))
        try {
            this.producer.produce(
                topicName,
                null,
                message,
                Date.now()
            )
            callback(null)
        } catch (err) {ord
            console.log('caught')
            callback(err)
        }
    }
    delete_rating(data, callback) {
        let topicName = 'Reviews_and_Rating'
        let eventType = 'delete_rating'
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
const kf = new KafkaProducer()

Object.freeze(kf)
module.exports = kf;