const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/booking-payment-producer');


router.post(
    '/payment-transction-details',
    [

      query('transaction_id'),
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

kafka.on('ready', function() {
    console.log('The producer has connected.')
});
module.exports = router;
