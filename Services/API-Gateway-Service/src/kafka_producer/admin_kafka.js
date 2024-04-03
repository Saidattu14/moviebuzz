const Kafka = require('node-rdkafka');
class KafkaAdmin {
    constructor()
    {
        var client = Kafka.AdminClient.create({
            'client.id': 'kafka-admin',
            'metadata.broker.list': ['localhost:9092','localhost:9093','localhost:9094'] 
        });
    //   //   client.deleteTopic('RequestBookingPaymentTopic',10000,function(err) {
    //   //       console.log(err)
    //   //   });
    //   //   client.deleteTopic('ResponseBookingPaymentTopic',10000,function(err) {
    //   //     console.log(err)
    //   // });
    //   // client.deleteTopic('RequestReviewsTopic',10000,function(err) {
    //   //       console.log(err)
    //   // });
    //   // client.deleteTopic('ResponseReviewsTopic',10000,function(err) {
    //   //   console.log(err)
    //   // });
    //   // client.deleteTopic('RequestTheatersTicketsDetailsTopic',10000,function(err) {
    //   //       console.log(err)
    //   // });
    //   // client.deleteTopic('ResponseTheatersTicketsDetailsTopic',10000,function(err) {
    //   //   console.log(err)
    //   // });
    //   // client.deleteTopic('ResponseTransactionalStorageTopic',10000,function(err) {
    //   //       console.log(err)
    //   // });
    //   // client.deleteTopic('RequestTransactionalStorageTopic',10000,function(err) {
    //   //   console.log(err)
    //   // });
    //     client.createTopic({
    //         topic: 'RequestBookingPaymentTopic',
    //         num_partitions: 3,
    //         replication_factor: 1
    //       }, 10000,function(err) {
    //         console.log(err)
    //     });
    //     client.createTopic({
    //       topic: 'ResponseBookingPaymentTopic',
    //       num_partitions: 3,
    //       replication_factor: 1
    //     }, 10000,function(err) {
    //       console.log(err)
    //   });
    //   client.createTopic({
    //         topic: 'RequestReviewsTopic',
    //         num_partitions: 3,
    //         replication_factor: 1
    //       }, 10000,function(err) {
    //         console.log(err)
    //   });
    //   client.createTopic({
    //     topic: 'ResponseReviewsTopic',
    //     num_partitions: 3,
    //     replication_factor: 1
    //   }, 10000,function(err) {
    //     console.log(err)
    //   });
    //     client.createTopic({
    //       topic: 'RequestTheatersTicketsDetailsTopic',
    //       num_partitions: 3,
    //       replication_factor: 1
    //     }, 10000,function(err) {
    //       console.log(err)
    //   });
    //   client.createTopic({
    //     topic: 'ReponseTheatersTicketsDetailsTopic',
    //     num_partitions: 3,
    //     replication_factor: 1
    //   }, 10000,function(err) {
    //     console.log(err)
    // });
   
    // client.createTopic({
    //         topic: 'RequestTransactionalStorageTopic',
    //         num_partitions: 3,
    //         replication_factor: 1
    //       }, 10000,function(err) {
    //         console.log(err)
    //     });
    //     client.createTopic({
    //       topic: 'ResponseTransactionalStorageTopic',
    //       num_partitions: 3,
    //       replication_factor: 1
    //     }, 10000,function(err) {
    //       console.log(err)
    //   });

    //   client.createTopic({
    //     topic: "RequestReviewsTopic",
    //     num_partitions: 1,
    //     replication_factor: 1
    //   }, 10000,function(err) {
    //     console.log(err)
    // });


    client.createTopic({
      topic: "ReviewLikedDisLikedTopic",
      num_partitions: 3,
      replication_factor: 1
    }, 10000,function(err) {
      console.log(err)
   });


        //  client.deleteTopic('RequestReviewsTopic',10000,function(err) {
        //     console.log(err)
        // });



    }
}
const kafka_admin = new KafkaAdmin()
Object.freeze(kafka_admin)
// module.exports = kafka_admin;