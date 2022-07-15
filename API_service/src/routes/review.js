const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/review-rating-producer')
const redis_client = require('../Redis/client')
router.patch(
  '/update_review',
  [
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
      let token = req.headers.authorization;
      let user = await redis_client.getAsync(token);
      if(user != null)
      {
        const data = req.body;
        kafka.update_review(data,(err) => {
          console.log(err)
        });
        return res.status(201).send('OK');
      }
      else
      {
        return res.status(401).json("Authorization token Needed")
      }
     
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
      let token = req.headers.authorization;
      let user = await redis_client.getAsync(token);
      if(user != null)
      {
        const data = req.body;
        kafka.add_review(data,(err) => {
          console.log(err)
        })
        return res.status(201).send('OK');
      }
      else
      {
        return res.status(401).json("Authorization token Needed")
      }
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
      let token = req.headers.authorization;
      let user = await redis_client.getAsync(token);
      if(user != null)
      {
        const data = req.body;
        kafka.delete_review(data,(err) => {
          console.log(err)
        })
        return res.status(201).send('OK');
      }
      else
      {
        return res.status(401).json("Authorization token Needed")
      }
    } catch (err) {
      return next(err);
    }
  },
);

kafka.on('ready', function() {
  console.log('Review-rating has connected.')
});
module.exports = router;
