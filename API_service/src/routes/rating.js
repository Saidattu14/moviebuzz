const router = require('express').Router();
const { query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/review-rating-producer')
router.patch(
  '/update_rating',
  [
   
    apiErrorReporter,
  ],
  async (req, res, next) => {
    console.log(req.body)
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
  '/add_rating',
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
        kafka.add_rating(data,(err) => {
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
router.delete(
  '/delete_rating',
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
        kafka.delete_rating(data,(err) => {
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
module.exports = router;
