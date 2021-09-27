const Kafka = require('node-rdkafka');
class KafkaAdmin {
    constructor()
    {
        var client = Kafka.AdminClient.create({
            'client.id': 'kafka-admin',
            'metadata.broker.list': ['localhost:9092','localhost:9093','localhost:9094']
        });
        // client.deleteTopic('Booking-Payment',10000,function(err) {
        //     console.log(err)
        // });
        // client.deleteTopic('Reviews_and_Rating',10000,function(err) {
        //     console.log(err)
        // });
        client.createTopic({
            topic: 'Booking-Payment',
            num_partitions: 2,
            replication_factor: 2
          }, 10000,function(err) {
            console.log(err)
        });
        client.createTopic({
            topic: 'Reviews_and_Rating',
            num_partitions: 2,
            replication_factor: 2
          }, 10000,function(err) {
            console.log(err)
        });
    }
}
const kafka_admin = new KafkaAdmin()

Object.freeze(kafka_admin)
module.exports = kafka_admin;