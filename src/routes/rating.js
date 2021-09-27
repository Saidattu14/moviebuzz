const router = require('express').Router();
const { query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/review-rating-producer')
router.put(
  '/update_rating',
  [
   
    apiErrorReporter,
  ],
  async (req, res, next) => {
    console.log(req.body)
    try {
      const data = req.body;
      kafka.update_review(data,(err) => {
        console.log(err)
      });
      return res.status(201).send('OK');
    } catch (err) {
      return next(err);
    }
  },
);
router.post(
  '/add_rating',
  [
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
     
      const data = req.body;
      kafka.add_rating(data,(err) => {
        console.log(err)
      });
      
      return res.status(201).send('OK');
    } catch (err) {
      return next(err);
    }
  },
);
router.delete(
  '/delete_rating',
  [
   
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
      const data = req.body;
      kafka.delete_rating(data,(err) => {
        console.log(err)
      });
      
      return res.status(201).send('OK');
    } catch (err) {
      return next(err);
    }
  },
);
module.exports = router;
