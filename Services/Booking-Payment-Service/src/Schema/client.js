const {Client} = require('pg');
const client = new Client({
    user: 'user',
    host: 'localhost',
    password: '123456',
    port: 5431,
    database : 'default_database'
});

try {
  client.connect();
  console.log("Database Connected")
} catch (error) {
  console.log("DataBase is not Connected")
}
module.export = client;