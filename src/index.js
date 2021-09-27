const https = require('https');
const fs = require('fs');
const morgan = require('morgan');
const http = require('http');
const cors = require('cors');
const routes = require('./routes');
const express = require('express');
const bodyParser = require('body-parser');

// const kafka_admin = require('./kafka_producer/admin_kafka')
const app = express();

app.use(bodyParser.json());
app.use(cors());
app.use('/api', routes);


app.listen(8000,() => {
 console.log('server listening on 8000')
})