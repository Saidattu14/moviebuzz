const router = require('express').Router();
const { query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const { Client } = require('@elastic/elasticsearch')
const client = new Client({ node: 'http://localhost:9200' })
const {search_moviebygenre,search_moviebyname, search_moviebyrating,search_movies} = require('../Elastic Search/search');

router.get(
  '/search_movies/:country_name&sort',
  [
    param('country_name'),
    query('order'),
    apiErrorReporter,
  ],
  async (req, res, next) => {

    try {
      
      return res.status(200).json("hello");
    } catch (err) {
      return next(err);
    }
  },
);
router.get(
  '/search_movies/:country_name/:state',
  [
    param('state').optional(),
    param('country_name'),
    query('movie_name').optional(),
    query('genre').optional(),
    query('order').optional(),
    apiErrorReporter,
  ],
  async (req, res, next) => {
    try {
      
      let obj = {
        "country": req.params.country_name,
        "state" : req.params.state,
        "movie_name" : req.query.movie_name,  
        "genre" : req.query.genre,
        "order" : req.query.order
      }
      if(obj.movie_name != undefined)
      {
        var result = await search_moviebyname(client,obj)
        return res.status(200).json(result);
      }
      else if(obj.genre != undefined)
      {
        var result = await search_moviebygenre(client,obj)
        return res.status(200).json(result);
      }
      else if(obj.order != undefined)
      {
        var result = await search_moviebyrating(client,obj)
        return res.status(200).json(result);
      }
      else
      {
        var result = await search_movies(client,obj)
        return res.status(200).json(result);
      }
     
    } catch (err) {
      return next(err);
    }
  },
);
module.exports = router;
