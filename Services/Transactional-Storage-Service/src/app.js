const cassandra = require('cassandra-driver');
const KafkaConsumer = require('./Kafka_consumer/transactional_consumer');
const kafkaProducer = require('./Kafka_producer/transactional-producer');
const keyspace = "TransactionalStorage";
const localDataCenter = "datacenter1";
const {
  createTransactionTableQuery,
  createTransactionTableQuery1,
  createTransactionTableQuery2,
  paymentTransactionDataTypeQuery,
  bookingTransactionDataTypeQuery,
  seatingDataTypeQuery,
  userTransactionDataTypeQuery,
} = require('./Schema/udtQueries');
const {
  createTransactionStoringObj
} = require('./Schema/utils');
const client = new cassandra.Client({ contactPoints: ['127.0.0.1'], localDataCenter: localDataCenter });

const mapper = new cassandra.mapping.Mapper(client, { models: {
  'Video': {
    tables: ['TransactionalStorage.storage',],
    keyspace: keyspace,
    columns: {
      'userId': 'userId',
    },
  }
}});

async function createKeyspace() 
{
    try {
      const createKeyspaceQuery = `CREATE KEYSPACE IF NOT EXISTS ${keyspace} ` +`WITH REPLICATION = {'class':'NetworkTopologyStrategy','${localDataCenter}': 1}`;
      await client.execute(createKeyspaceQuery);
      console.log("keySpace Created")
    } catch (error) {
      console.log(error)
    }
}

async function insertQuery(userId,transaction) 
{
    try {
      const query = 'INSERT INTO TransactionalStorage.storage(userId,transactions,time_stamp) VALUES (?, ?,?) IF NOT EXISTS';
      return client.execute(query, [userId,transaction,Math.round(Date.now()/1000)], { prepare: true});
    } catch (error) {
      console.log(error)
    }
}

async function dropQueries()
{
  try {
      const a1 = "DROP TABLE IF EXISTS TransactionalStorage";
      const a2 = "DROP TYPE IF EXISTS TransactionalStorage.bookingData";
      const a3 = "DROP KEYSPACE IF EXISTS TransactionalStorage";
      await client.execute(a3);
  } catch (error) {
    console.log(error)
  }
}

async function createTable() 
{
  try {
     await client.execute(createTransactionTableQuery);
  } catch (error) {
    console.log(error)
  }
}

async function createUserDataTypesForKeySpace () 
{
  try {
     await client.execute(seatingDataTypeQuery);
     await client.execute(paymentTransactionDataTypeQuery);
     await client.execute(bookingTransactionDataTypeQuery);
     await client.execute(userTransactionDataTypeQuery);
  } catch (error) {
    console.log(error)
  }
}

async function createKeyspace() 
{
    try {
      const createKeyspaceQuery = `CREATE KEYSPACE IF NOT EXISTS ${keyspace} ` +`WITH REPLICATION = {'class':'NetworkTopologyStrategy','${localDataCenter}': 1}`;
      await client.execute(createKeyspaceQuery);
    } catch (error) {
      console.log(error)
    }
}

async function sendTransactionHistoryData(data)
{
    try {
     let transactionResults = await getTransactionData(data.userId);
     let response = {
      "requestId"  : data.requestId,
      "requestType" : data.requestType,
      "movieBookingHistoryDetails" : transactionResults
    }
     kafkaProducer.transactionalHistoryResponseEvent(response,(err) => {
      console.log(err)
     })
    } catch (error) {
      console.log(error)
    }
}

async function getTransactionData(userId)
{
  try {
      let params = `transactions.paymentdata.paymentid AS paymentid, 
      transactions.bookingdata.bookingid AS bookingid,  
      transactions.bookingdata.seating AS seating, 
      transactions.bookingdata.show_id AS show_id,  
      transactions.bookingdata.countryname AS countryname,  
      transactions.bookingdata.cityname AS cityname, 
      transactions.bookingdata.moviename AS moviename, 
      transactions.bookingdata.date AS date, 
      transactions.bookingdata.movieid AS movieid,
      transactions.bookingdata.movieposter AS movieposter,
      transactions.paymentstatus AS paymentstatus,
      transactions.bookingdata.theater_name AS theatername`;
      const query = `SELECT ${params} from TransactionalStorage.storage where userId = ? ALLOW FILTERING`;
      const rowsDataResult = await client.execute(query, [userId], { prepare: true });
      return rowsDataResult.rows;
  } catch (error) {
    console.log(error)
    return null;
  }
}

client.connect()
  .then(async function() {
    console.log('Connected to cluster with %d host(s): %j', client.hosts.length, client.hosts.keys());
    await createKeyspace();
    await createUserDataTypesForKeySpace();
    await createTable();
    //let transactionStoreObj = createTransactionStoringObj(my);
    //await insertQuery(my.payload.userId,transactionStoreObj);
    //console.log(await getTransactionData(my.payload.userId))
    //await dropQueries()
  })
  .catch(function (err) {
    console.error('There was an error when connecting', err);
    return client.shutdown().then(() => { throw err; });
  });

KafkaConsumer.consumer.connect();
KafkaConsumer.consumer.on('ready', () => {
  console.log('Transactional consumer ready!')
  KafkaConsumer.consumer.subscribe(['RequestTransactionalStorageTopic'])
  KafkaConsumer.consumer.consume();
}).on('data', async(data) => {
  
   try {
        let message = JSON.parse(data.value.toString())
        console.log(message)
        let event_type = message.eventType
        if (event_type == 'storeTransaction')
        {
          let transactionStoreObj = createTransactionStoringObj(message);
          await insertQuery(message.payload.userId,transactionStoreObj);
          KafkaConsumer.consumer.commitMessage(data)
        }
        else if(event_type == "getTransactionsDetails")
        {

           sendTransactionHistoryData(message.payload)
           KafkaConsumer.consumer.commitMessage(data);
        }
        else
        {
          KafkaConsumer.consumer.commitMessage(data);
        }
    }
   catch (err) {
        console.error(err);
    }
});