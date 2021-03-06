const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/booking-payment-producer');
const fetch = (...args) => import('node-fetch').then(({default: fetch}) => fetch(...args));

router.post(
    '/payment-transction-details/:booking_request_id',
    [
      param('booking_request_id'),
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      
      try {
      let token = req.headers.authorization;
      let user = await redis_client.getAsync(token);
      if(user != null)
      {
        let data = {
          "payment_id" : req.query.payment_id,
          "user_name" : req.body.user_name,
          "card_name" : req.body.card_name,
          "card_number" : req.body.card_number,
          "price" :       req.body.price,
          "mobile_no" : req.body.mobile_no,
          "password" : req.body.password,
        }
      
      let body_data = JSON.stringify(data) 
      let querys = {
        'payment_id' : req.query.payment_id
      }
      let url_params = new URLSearchParams(Object.entries(querys));
      let booking_request_id = req.params.booking_request_id;
      let response = await fetch(`http://localhost:8001/status/payment-transaction/`+ booking_request_id +`?`+  url_params, 
      {
        method: 'POST', 
        body: body_data,
        headers: {'Content-Type': 'application/json'},
      });
      
      if(response.status == 400 || response.status == 404 || response.status == 500)
      {
        return res.status(400).send('Payment Session Expired');
      }
      else
      {
        return res.status(201).send('OK');
      }
      }
      else
      {
        return res.status(401).json("Authorization token Needed")
      }
      } catch (err) {
        return next(err);
      }
    },
);

router.get(
  '/payment/transaction-data/status',
  [
    query('payment_id'),
    apiErrorReporter,
  ],
  async (req, res, next) => {

    try {

      let token = req.headers.authorization;
      let user = await redis_client.getAsync(token);
      if(user != null)
      {
      let querys = {
        'payment_id' : req.query.payment_id
      }
      let url_params = new URLSearchParams(Object.entries(querys));
      let response = await fetch(`http://localhost:8001/status/payment-transaction?` + url_params);
      if(response.status == 400 || response.status == 404 || response.status == 500) 
      {
        return res.status(400).send("Invalid Payment Id");

      }
      else
      {
        let json_data = await response.json();
        return res.status(200).json(json_data);
      }
     }
     else
     {
      return res.status(401).json("Authorization token Needed");
     }
    } catch (error) {
      return next(error);
    }  
  }
);

kafka.on('ready', function() {
    console.log('Payment has connected.')
});
module.exports = router;
