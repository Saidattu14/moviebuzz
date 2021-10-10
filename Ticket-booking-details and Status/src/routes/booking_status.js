const router = require('express').Router();
const { v4: uuidv4 } = require('uuid');
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/client')
router.get(
    '/booking-status',
    [

      query('booking_request_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        let result = await redis_client.getAsync(
          req.query.booking_request_id,
        )
        result = JSON.parse(result)
        return res.status(200).json(result);
      } catch (err) {
        return next(err);
      }
    },
);

router.post(
    '/booking-status',
    [
      query('booking_request_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        
        if(req.query.booking_request_id != undefined)
        {
          redis_client.setAsync(
            req.query.booking_request_id,
            "Booking in Progess",
            'EX',
            300
          ).then(() => console.log("Request added")).catch(err => console.error(err))
          return res.status(201).send('Booking in Progress');
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
module.exports = router;
