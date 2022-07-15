const fetch = (...args) => import('node-fetch').then(({default: fetch}) => fetch(...args));
const router = require('express').Router();
const URLSearchParams = require('@ungap/url-search-params')
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');


router.get(
    '/transaction-data',
    [
      query('transaction_id'),
      apiErrorReporter,
    ],
    async (req, res, next) => {
      try {

        let token = req.headers.authorization;
        let user = await redis_client.getAsync(token);
        if(user != null)
        {
        let querys = {
          'transaction_id' : req.query.transaction_id
        }
        let url_params = new URLSearchParams(Object.entries(querys));
        const response = await fetch(`http://localhost:8001/status/transaction-status?` + url_params);
        if(response.status == 500 || response.status == 400 || response.status == 404)
        {
          
          return res.status(400).send("BAD request");
        }
        else
        {
  
        const json_data = await response.json();
        return res.status(200).json(json_data);
        }
       }
       else
       {
        return res.status(401).json("Authorization token Needed");
       }
      } catch (error) {
        return next(error);
      }  
    }
  );

module.exports = router;
