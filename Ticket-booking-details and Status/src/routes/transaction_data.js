const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/client')



router.get(
    '/transaction-status',
    [
      query('transaction_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {
        
        let result = await redis_client.hgetAsync(
          "Transaction_data",
          req.query.transaction_id,
        );
        
        if(result == null)
        {
          return res.status(400).send('Invalid request ID');
        }
        else
        {
          result = JSON.parse(result);
          return res.status(200).json(result);
        }
      
      } catch (err) {
        return next(err);
      }
    },
);
module.exports = router;