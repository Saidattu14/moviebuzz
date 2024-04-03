const router = require('express').Router();
const { body} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const reviewsKafka = require('../kafka_producer/review-rating-producer')
const reviewLikedDisLikedKafka = require('../kafka_producer/reviews-Liked-DisLiked-producer');
const redis_client = require('../Redis/redis_client');
const {v4 : uuidv4} = require('uuid')


const checkJwtToken = async(request) => {
  try {
    let token = request.headers.authorization;
    let user = await redis_client.getAsync(String(token));
    if(user != null) {
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
        if(result) {
          reviewsKafka.updateReviewEvent(data,(err) => {
            console.log(err)
          });
          return response.status(201).json('OK');
        }
        else {
          return response.status(400).json("Authorization Needed");
        }
      } catch (err) {
        return response.status(500).json("Try again Later");
      }
  },
);


router.patch(
  '/like_dislike_review',
  [
    body('movieId').isString(),
    body('isLiked').isBoolean(),
    body('reviewId').isString(),
    body('isDisLiked').isBoolean(),
    body('userId').isString(),
    apiErrorReporter,
  ],
  async (request, response) => {
      try {
        let result = await checkJwtToken(request);
        if(result) {
          console.log(request.body)
          // kafka.updateReviewEvent(data,(err) => {
          //   console.log(err)
          // });
          return response.status(201).json('OK');
        }
        else {
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
      if(result) {
        request.body.reviewId = uuidv4();
        reviewLikedDisLikedKafka.addReviewEvent(request.body,(err) => {
          console.log(err)
        })
        return response.status(201).json("OK");
      }
      else {
        return response.status(400).json("Authorization Needed");
      }
    } 
    catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);


router.patch(
  '/delete_review',
  [
    body().isObject(),
    body('movieId').isString(),
    body('reviewId').isString(),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request);
      if(result) {
        console.log(request.body)
        reviewsKafka.deleteReviewEvent(request.body,(err) => {
          console.log(err)
        });
        return response.status(201).send('OK');
      }
      else {
        return response.status(400).send("Authorization Needed");
      }
    } 
    catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);
module.exports = router;
