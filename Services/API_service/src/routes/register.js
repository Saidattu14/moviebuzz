const router = require('express').Router();
const { body} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/redis_client')
const jwt = require('jsonwebtoken');
const {v4 : uuidv4} = require('uuid');
const SECRET = 'shhhhhsjjs';
router.post(
    '/register_user',
    [
        body('username').isEmail(),
        body('password').notEmpty(),
        body('confirmPassword').notEmpty(),
        apiErrorReporter,
    ],
    async (request, response) => {
      if(request.body.password != request.body.confirmPassword)
      {
        return response.status(400).json("The password you entered wasn't matching.");
      }
      try {
        let userEncrypt = jwt.sign(request.body.username, SECRET);
        let userData = await redis_client.hgetAsync(userEncrypt,'username');
        if(userData == null)
        {
            let body = {
                'username' : request.body.username,
                'password' : request.body.password,
            }
            let tokenData = 'Bearer '+ jwt.sign(body, SECRET);
            let passwordEncrypt = jwt.sign(request.body.password, SECRET);
            let userId = uuidv4();
            await redis_client.setAsync(tokenData,userEncrypt);
            await redis_client.hsetAsync(userEncrypt,'username',userEncrypt);
            await redis_client.hsetAsync(userEncrypt,'password',passwordEncrypt);
            await redis_client.hsetAsync(userEncrypt,'token',tokenData);
            await redis_client.hsetAsync(userEncrypt,'userId',userId);
            let responseBody = {
              'jwtToken' : tokenData,
              'userEmail' : request.body.username,
              'userId' :  uuidv4()
            }
            return response.status(200).json(responseBody);
        }
        else
        {
          return response.status(400).send("The username you entered belong to an account. Please try another username and try again.");
        }
      } catch (err) {
        //console.log(err)
        return response.status(400).send("Please try again later.");
      }
    },
);
module.exports = router;