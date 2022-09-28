const Kafka = require('node-rdkafka');

class MovieReviewsKafkaProducer {
    constructor()
    {
       var producer = new Kafka.Producer({
        'client.id': 'kafka-Producer-Reviews-Rating',
        'metadata.broker.list': ['localhost:9093'],
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
   
    getTransactionsHistoryEvent(data, callback) {
        let topicName = 'RequestTransactionalStorageTopic';
        let eventType = 'getTransactionsDetails';
        let message = Buffer.from(JSON.stringify({eventType, payload: data}))
        try {
            this.producer.produce(
                topicName,
                null,
                message,
                Date.now()
            );
            callback(null)
        } catch (err) {
            console.log('caught')
            callback(err)
        }
    }
}
const kf = new MovieReviewsKafkaProducer()
Object.freeze(kf)
module.exports = kf;