
const redis = require('redis-promisify')




const redis_client = redis.createClient({
    host: '127.0.0.1',
    port: 6379,
    no_ready_check: true,
})
// const redis_client = redis.createClient(client)

// redis_client.on('error', error => console.log(error));

// bluebird.promisifyAll(redis_client);


module.exports = redis_client;