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
        const data = req.body;
        kafka.payment_data(data,(err) => {
          console.log(err)
        })
        return res.status(201).send('OK');
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
