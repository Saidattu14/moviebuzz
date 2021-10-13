const fetch = (...args) => import('node-fetch').then(({default: fetch}) => fetch(...args));
const router = require('express').Router();
const URLSearchParams = require('@ungap/url-search-params')
const { v4: uuidv4 } = require('uuid');
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/booking-payment-producer');



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
        });
      let body_data = JSON.stringify(data) 
      let querys = {
        'booking_request_id' : request_id
      }
      let url_params = new URLSearchParams(Object.entries(querys))
      try {
        
        const response = await fetch(`http://localhost:8001/status/booking-status?` + url_params, 
        {
          method: 'POST',
          body: body_data,
          headers: {'Content-Type': 'application/json'},
        });
        console.log(response)
      } catch (error) {
        return next(err);
      }
        return res.status(201).send({"booking_request_id" : request_id,"message": "Processing"});
      } catch (err) {
        return next(err);
      }
    },
);

router.get(
  '/request/Booking-data/status',
  [
    query('booking_request_id'),
    apiErrorReporter,
  ],
  async (req, res, next) => {

    let querys = {
      'booking_request_id' : req.query.booking_request_id
    }
    let url_params = new URLSearchParams(Object.entries(querys))
    try {
      
      const response = await fetch(`http://localhost:8001/status/booking-status?` + url_params);
      if(response.status == 500 || response.status == 400 || response.status == 404)
      {
        con
        return res.status(400).send("BAD request");
      }
      else
      {

      const json_data = await response.json();
      return res.status(200).json(json_data);
      }
    } catch (error) {
      return next(error);
    }  
  }
);

kafka.on('ready', function() {
    console.log('Booking-Payment-Producer has connected.')
  });
module.exports = router;
