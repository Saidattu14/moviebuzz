const router = require('express').Router();
const { v4: uuidv4 } = require('uuid');
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/client')
const kafka = require('../kafka-producer/payment-producer');
router.get(
    '/payment-transaction',
    [
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        const result = await redis_client.hgetAsync(
          "Payment_Status",
          req.query.payment_id,
        ).then(() => console.log("Responsed")).catch(err => console.error(err))
        return res.status(201).send(result);
      } catch (err) {
        return next(err);
      }
    },
);

router.post(
    '/payment-transaction',
    [
      query('payment_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        if(req.query.payment_id != undefined)
        {
          redis_client.hsetAsync(
            "Payment_Status",
            req.query.payment_id,
            "Payment in Progess",
            'EX',
             180
          ).then(
        () => 
        {
            kafka.payment_data(data,(err) => {
                console.log(err)
              });
        }
        ).catch(err => console.error(err))
          return res.status(201).send('Payment in Progress');
        }
        else
        {
          return res.status(400).send('Invalid request ID');
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
