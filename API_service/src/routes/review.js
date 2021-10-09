const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/review-rating-producer')
router.patch(
  '/update_review',
  [
    apiErrorReporter,
  ],
  async (req, res, next) => {
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
  '/add_review',
  [
  
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
      const data = req.body;
      kafka.add_review(data,(err) => {
        console.log(err)
      })
      return res.status(201).send('OK');
    } catch (err) {
      return next(err);
    }
  },
);
router.delete(
  '/delete_review',
  [
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
      const data = req.body;
      kafka.delete_review(data,(err) => {
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
