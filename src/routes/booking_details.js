const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/booking-payment-producer');

router.get(
    '/:country/:state-and-Where-therters-available-tickets',
    [
        param('state'),
        param('country_name'),
        query('movie_name'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        const data = req.body;
        kafka.movie_data(data,(err) => {
          console.log(err)
        })
        return res.status(201).send('OK');
      } catch (err) {
        return next(err);
      }
    },
);

router.post(
    '/:country_name/:state-and-Where-movie=:movie_name-and-theater',
    [
        param('state'),
        param('movie_name'),
        param('country_name'),
        query('theater_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        const data = req.body;
        kafka.booking_information(data,(err) => {
          console.log(err)
        })
        return res.status(201).send('OK');
      } catch (err) {
        return next(err);
      }
    },
);


kafka.on('ready', function() {
    console.log('Booking-Payment-Producer has connected.')
  });
module.exports = router;
