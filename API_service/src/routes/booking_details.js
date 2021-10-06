const router = require('express').Router();
const { v4: uuidv4 } = require('uuid');
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

/** Example body
 {
"Date" : "11/2/20",
"show_id" : "1",
"seating" : [
    {
        "row_id" : "A",
        "seat_number" : ["A1","A2"]
    },
    {
        "row_id" : "B",
        "seat_number" : ["B1","B2"]
    }
    ]
}
 **/

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
      let request_id = uuidv4();
      try {
        const data = {
          "request_id" : request_id,
          "state" : req.params.state,
          "country_name" : req.params.country_name,
          "movie_name" : req.params.movie_name,
          "theater_id" : req.query.theater_id,
          "Date" : req.body.Date,
          "show_id" : req.body.show_id,
          "seating" : req.body.seating
        }
        console.log(data)
        kafka.booking_information(data,(err) => {
          console.log(err)
        })
        return res.status(201).send({"request_id" : request_id,"message": "Processing"});
      } catch (err) {
        return next(err);
      }
    },
);
kafka.on('ready', function() {
    console.log('Booking-Payment-Producer has connected.')
  });
module.exports = router;
