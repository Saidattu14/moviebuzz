const morgan = require('morgan');
const cors = require('cors');
const express = require('express');
const bodyParser = require('body-parser');
const routes = require('./routes');
// const kafka_admin = require('./kafka_producer/admin_kafka')
const app = express();

app.use(bodyParser.json());
app.use(cors());
app.use('/api/v1', routes);


app.listen(8005,() => {
 console.log('server listening on 8005');
});
