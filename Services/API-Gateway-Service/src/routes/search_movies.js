const router = require('express').Router();
const { query,param} = require('express-validator');
const apiErrorReporter = require('../utils/api_error_report');
const redis_client = require('../Redis/redis_client')
const EventEmitter = require('events');
const 
{
  searchMoviesByRating,
  searchMoviesByGenre,
  searchMovieByCountry,
  searchMovieByCountryLocation,
  wildCardSearchOnCastDirectorsWriters,
  searchMostPopularMovies,
  wildCardSearchOnAllFields
} = require('../Elastic_Search/search');


const emitter = new EventEmitter()
emitter.setMaxListeners(200);


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


router.get(
  '/search_movies/country',
  [
    query('name').exists(true),
    query('Size').exists(true),
    query('From').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request);
      if(result)
      {
        let resp = await searchMovieByCountry(request.query.name,request.query.From,request.query.Size);
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).send("Authorization Needed");
      }
    } catch (err) {
      return response.status(500).send("Sorry Try Again Later");
    }
  },
);

router.get(
  '/search_movies/wildcardonAll/:name',
  [
    query('name').exists(true),
    query('Size').exists(true),
    query('From').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await wildCardSearchOnAllFields(request.params.name,request.query.From,request.query.Size); 
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).json("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);

router.get(
  '/search_movies/wildcard/:name',
  [
    param('name').exists(true),
    query('Size').exists(true),
    query('From').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await wildCardSearchOnCastDirectorsWriters(request.params.name,request.query.From,request.query.Size);
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).send("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);



router.get(
  '/search_movies/:country_name/:lat/:lon',
  [
    param('country_name').exists(true),
    param('lat').exists(true),
    param('lon').exists(true),
    query('Size').exists(true),
    query('From').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await searchMovieByCountryLocation(request.params.country_name,
          request.params.lat,request.params.lon,20,request.query.From,request.query.Size);
          return response.status(200).json(resp);
      }
      else
      {
        
        return response.status(400).send("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);


router.get(
  '/search_movies_genre/',
  [
    query('type').exists(true),
    query('Size').exists(true),
    query('From').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await searchMoviesByGenre(request.query.type,request.query.Size,request.query.From);
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).send("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);

router.get(
  '/search_most_popular_movies/',
  [
    query('From').exists(true),
    query('Size').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await searchMostPopularMovies(request.query.Size,request.query.From);
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).send("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);

router.get(
  '/search_top_rated_movies/',
  [
    query('From').exists(true),
    query('Size').exists(true),
    apiErrorReporter,
  ],
  async (request, response) => {
    try {
      let result = await checkJwtToken(request)
      if(result)
      {
        let resp = await searchMoviesByRating(request.query.Size,request.query.From);
        return response.status(200).json(resp);
      }
      else
      {
        return response.status(400).send("Sorry Authorization Needed");
      }
    } catch (err) {
      return response.status(500).json("Try again Later");
    }
  },
);
module.exports = router;
