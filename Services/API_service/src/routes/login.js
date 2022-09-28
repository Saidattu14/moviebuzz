const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/redis_client')
const jwt = require('jsonwebtoken');
const SECRET = 'shhhhhsjjs';
router.post(
    '/login_user',
    [
      body('username').isEmail(),
      body('password').notEmpty(),
      apiErrorReporter,
    ],
    async (request, response) => {
      try {
        //console.log(request.body)
        let userEncrypt = jwt.sign(request.body.username, SECRET);
        let userData = await redis_client.hgetAsync(userEncrypt,'username');
        //console.log(userData)
        if(userData != null)
        {
            let getPasswordEncrypt = await redis_client.hgetAsync(userEncrypt,'password');
            let passwordEncrypt = jwt.sign(request.body.password, SECRET);
            if(getPasswordEncrypt == passwordEncrypt)
            {
              let body = {
                'username' : request.body.username,
                'password' : request.body.password,
              }
              let tokenData = await redis_client.hgetAsync(userEncrypt,'token');
              let userId = await redis_client.hgetAsync(userEncrypt,'userId');
              let responseBody = {
                'jwtToken' : tokenData,
                'userEmail' : request.body.username,
                'userId' : userId
              }
              return response.status(200).json(responseBody);
            }
            else
            {
              return response.status(400).send("The password you entered doesn't belong to this account. Please check your password and try again.");
            }
        }
        else
        { 
          return response.status(400).send("The username you entered doesn't belong to an account. Please check your username and try again.");
        }
      } catch (err) {
        //console.log(err)
        return response.status(400).send("Please try again later.");
      }
    },
);
module.exports = router;
