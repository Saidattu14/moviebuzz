const router = require('express').Router();
// const { query,param} = require('express-validator');
// const apiErrorReporter = require('../utils/api_error_report');
// const { Client } = require('@elastic/elasticsearch')
// const client = new Client({ node: 'http://localhost:9200' })
// const {search_moviebygenre,search_moviebyname} = require('../Elastic Search/search');

// router.get(
//   '/search_movies/:country_name&sort',
//   [
//     param('country_name'),
//     query('order'),
//     apiErrorReporter,
//   ],
//   async (req, res, next) => {

//     try {
      
//       return res.status(200).json("hello");
//     } catch (err) {
//       return next(err);
//     }
//   },
// );
// router.get(
//   '/search_movies/:country_name/:state',
//   [
//     param('state').optional(),
//     param('country_name'),
//     query('movie_name').optional(),
//     query('genre').optional(),
//     apiErrorReporter,
//   ],
//   async (req, res, next) => {
//     try {
      
//       let obj = {
//         "country": req.params.country_name,
//         "state" : req.params.state,
//         "movie_name" : req.query.movie_name,  
//       }
      
      
//       var result = await search_moviebyname(client,obj)
//       return res.status(200).json(result);
//     } catch (err) {
//       return next(err);
//     }
//   },
// );
// // router.get(
// //   '/search_movies/:country_name/:state+and+/:movie_name',
// //   [
// //     param('state'),
// //     param('movie_name'),
// //     param('country_name'),
// //     query('order'),
// //     apiErrorReporter,
// //   ],
// //   async (req, res, next) => {
// //     try {
       
// //       let obj = {
// //         "country": req.params.country_name,
// //         "state" : req.params.state,
// //         "movie_name" : req.params.movie_name,
// //         "order" : req.params.order
// //       }
      
      
// //       const result = await search_moviebyrating(client,obj);
// //       return res.status(200).json(result);
// //     } catch (err) {
// //       return next(err);
// //     }
// //   },
// // );
module.exports = router;
