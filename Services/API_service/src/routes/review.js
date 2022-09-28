const router = require('express').Router();
const { body} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const kafka = require('../kafka_producer/review-rating-producer')
const redis_client = require('../Redis/redis_client');
const {v4 : uuidv4} = require('uuid')
const { promisifyAll } = require('bluebird');
promisifyAll(redis_client); 



const checkJwtToken = async(request) => {
  try {
    let token = request.headers.authorization;
    let user = await redis_client.getAsync(String(token));
    if(user != null) 
    {
      return true;
    }
    return false;
  } catch (error) {
    console.log(error)
    return false;
  } 
}


router.patch(
  '/update_review',
  [
    body('movieId').isString(),
    body('review').isString(),
    body('reviewId').isString(),
    body('rating').isInt(),
    apiErrorReporter,
  ],
  async (request, response) => {
      try {
        let result = await checkJwtToken(request);
        if(result)
        {
          kafka.updateReviewEvent(data,(err) => {
            console.log(err)
          });
          return response.status(201).json('OK');
        }
        else
        {
          return response.status(400).json("Authorization Needed");
        }
      } catch (err) {
        return response.status(500).json("Try again Later");
      }
  },
);

router.post(
  '/add_review',
  [
    body().isObject(),
    body('movieId').isString(),
    body('review').isString(),
    body('rating').isFloat(),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request);
      console.log(result)
      if(result)
      {
        kafka.addReviewEvent(data.body,(err) => {
          console.log(err)
        })
        return response.status(201).json("OK");
      }
      else
      {
        return response.status(400).json("Authorization Needed");
      }
    } 
    catch (err) {
      return response.status(500).json("Try again Later");
      }
  },
);


router.delete(
  '/delete_review',
  [
    body('movieId').isString(),
    body('reviewId').isString(),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request);
      if(result)
      {
        kafka.deleteReviewEvent(data.body,(err) => {
          console.log(err)
        })
        return response.status(201).send('OK');
      }
      else
      {
        return response.status(400).send("Authorization Needed");
      }
    } 
    catch (err) {
      return response.status(500).json("Try again Later");
      }
  },
);


module.exports = router;
