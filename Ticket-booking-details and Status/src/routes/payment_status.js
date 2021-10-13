const router = require('express').Router();
const { v4: uuidv4 } = require('uuid');
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/client')
const kafka = require('../kafka-producer/payment-producer');
const { json } = require('body-parser');
router.get(
    '/payment-transaction',
    [
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        console.log("Hello")
        const result = await redis_client.getAsync(
          req.query.payment_id,
        );
        console.log(result)
        if(result == "Payment Not Started" || result == null)
        {
          return res.status(400).send('Invalid request ID');
        }
        return res.status(201).send(result);
      } catch (err) {
        return next(err);
      }
    },
);
router.post(
    '/payment-transaction/:booking_request_id',
    [
      param('booking_request_id'),
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        
        let res1 = await redis_client.getAsync(req.query.payment_id)
        let res2 =  await redis_client.getAsync(req.params.booking_request_id)
        
        if(res2 == null)
        {
          return res.status(400).send('Invalid Payment Id');
        }
        else if(res1 == null)
        {
          return res.status(400).send('Booking Session Expired');
        }
        else 
        {
          let data = {
            "payment_data" : req.body,
            "booking_id" : req.params.booking_request_id,
            "booking_details" : JSON.parse(res2)
          }
          console.log(data)
          try {
            let update_payment_status = await redis_client.setAsync(req.query.payment_id,"Payment In Progress")

          } catch (error) {
            console.log(error)
          }
          kafka.payment_data(data,(err) => {
            console.log(err)
          });
          return res.status(200).send('Payment in Progress');
        }
        
      } catch (err) {
        return next(err);
      }
    },
);
kafka.on('ready', function() {
    console.log('The producer has connected.')
});

module.exports = router;
