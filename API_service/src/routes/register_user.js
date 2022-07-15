const router = require('express').Router();
const { body, query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/client')
const jwt = require('jsonwebtoken');
const { v4: uuidv4, stringify } = require('uuid');

router.post(
    '/register_user',
    [
        body('username').isEmail(),
        body('password').notEmpty(),
        body('confirmPassword').notEmpty(),
        apiErrorReporter,
    ],
    async (req, res, next) => {
      if(req.body.password != req.body.confirmPassword)
      {
        return res.status(400).json("Password UnMatch");
      }
      try {
        let token = {
          'username' : req.body.username,
          'password' : req.body.password,
          'confirmPassword' : req.body.confirmPassword
        }
        var token_data = 'Bearer '+ jwt.sign(token, 'shhhhhsjjs');
        var user = jwt.sign(req.body.username, 'shhhhhsjjs');
        var result = await redis_client.hgetAsync(user,'username')
        if(result == null)
        {
        // redis_client.hsetAsync(user,'username',req.body.username);
        // redis_client.hsetAsync(user,'password',req.body.password);
        // redis_client.saddAsync(req.body.username,String(token_data));
        // redis_client.setAsync(token_data,user);
        console.log(token_data, user)
        return res.status(200).json(token_data);
        }
        else
        {
          return res.status(400).json("Username Already Exits");
        }
      } catch (err) {
        return next(err);
      }
    },
);

module.exports = router;