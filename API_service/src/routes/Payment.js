const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/booking-payment-producer');


router.post(
    '/payment-transction-details/',
    [
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      
      try {
        const data = {
          "payment_id" : req.query.payment_id,
          "user_name" : req.body.user_name,
          "card_name" : req.body.card_name,
          "card_number" : req.body.card_number,
          "price" : req.body.price,
          "mobile_no" : req.body.mobile_no,
          "password" : req.body.password,
        }
      console.log(data)
      let body_data = JSON.stringify(data) 
      let querys = {
        'payment_id' : req.query.payment_id
      }
      let url_params = new URLSearchParams(Object.entries(querys))
      try {
        
        const response = await fetch(`http://localhost:8001/status/payment-transaction?` + url_params, 
        {
          method: 'POST', 
          body: body_data,
          headers: {'Content-Type': 'application/json'},
        });
        console.log(response)
      } catch (error) {
        return next(err);
      }
        return res.status(201).send({"payment_id" : req.query.payment_id,"message": "Payment in Processing"});
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

    let querys = {
      'payment_id' : req.query.payment
    }
    let url_params = new URLSearchParams(Object.entries(querys))
    try {
      
      const response = await fetch(`http://localhost:8001/status/booking-status?` + url_params);
      const json_data = response.json();
      console.log(json_data)
      return res.status(201).send("OK");
    } catch (error) {
      return next(error);
    }  
  }
);


kafka.on('ready', function() {
    console.log('The producer has connected.')
});
module.exports = router;
