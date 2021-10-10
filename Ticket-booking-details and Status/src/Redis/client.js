const redis = require('redis');
const bluebird = require('bluebird');
const client = {
    host: 'localhost',
    port : 6379,
    no_ready_check: true,
}

const redis_client = redis.createClient(client)

redis_client.on('error', error => console.log(error));
bluebird.promisifyAll(redis_client);
// async function run()
// {
//     try {
//         // const a = await redis_client.hsetAsync("","hello",'a1')
//         console.log(redis_client)
//         // let a1 =  await redis_client.hsetAsync("Booking_Status","hello1",'a2',"Ex",10)
//         const b= await redis_client.hgetAsync("Booking_Status",1)
//         console.log(b)
//     } catch (error) {
//         console.log(error)
//     }
    
// }
// run()

module.exports = redis_client;