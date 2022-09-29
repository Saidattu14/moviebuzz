var crypto = require('crypto');
const client = require('../Elastic_Search/elastic_client');
const searchMoviesByGenre = async(genre_type,size,from) => {  
let pr = new Promise(async(resolve,reject) => {
       await client.search({
          "index": "_all",
          from: from,
          size : size,
          body: {
            "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
            "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
            "query": {
              "bool" : {
                "must" : [
                   { 
                    "range" : {
                       "Year" : { "gte" : 2010, "lte" : 2021 }
                      }
                   },
                   {
                    "nested" : {
                      "path" : "Genre",
                      "query" : {
                          "match" : {"Genre.Type" : genre_type}
                      },
                    },
                   }
                ]
              },
            },
            "sort" : [
              {
              "Year" : {
                "order" : "desc"
              },
             },
            ],
          }
      }).then(function(resp) {
        resolve(resp.body.hits.hits)
      });
  }).catch((error) => {
    console.log(error)
    reject("Error");
  });
  return pr;
}

const searchMoviesByRating = async(size,from) => {
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: "_all",
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
        "query": {
         "match_all" : {}
      },
      "sort" : [
        {
          "imdbRating" : {
              "order" : "desc"
          },
       },
      ],
    },
    }).then(function(resp) {
      resolve(resp.body.hits.hits)
    }).catch((err) => {
      console.log(err);
      reject("Error");
    });
  });
  return pr;
}

const searchMovieByCountry = async(country_name,from,size) => {
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: "_all",
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
        "query" : {
            "nested" : {
              "path" : "Country",
              "query" : {
                "dis_max": {
                  "queries": [
                    { "term": { "Country.Name":country_name}},
                  ],
                }
            },
          },
        },
      },
    }).then(function(resp) {
      resolve(resp.body.hits.hits)
    }).catch((err) => {
      console.log(err);
      reject("Error");
    });
  });
  return pr;
}

const searchMostPopularMovies = async(size,from) => {
  console.log(size,from)
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: "_all",
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
        "query": {
         "match_all" : {}    
         },
         "sort" : [
          {
          "imdbVotes" : {
            "order" : "desc"
          },
         },
        ],
      },
    }).then(function(resp) {
      resolve(resp.body.hits.hits)
    }).catch((err) => {
      console.log(err)
      reject("Error");
    });
  });
  return pr;
}


const searchMovieByCountryCity = async(country_name,city_name,req) => {
  country_name = country_name.toLowerCase();
  let index = crypto.createHash('md5').update(country_name.toString()).digest("hex");
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: index,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
        "query": {
          "nested" : {
            "path" : "Cities",
            "query" : {
                "match" : {"Cities.Name" : city_name}
            },
            "inner_hits": {}
          },
         },
        },
    }).then(function(resp) {
      let arr = []
      let inner_hits_data = resp.body.hits.hits[0].inner_hits.Cities.hits.hits[0]._source;
      for(let i in resp.body.hits.hits)
      {
          let data = resp.body.hits.hits[i];
          data._source.Cities = inner_hits_data;
          delete data.inner_hits;
          arr.push(data)
      }
      let ob1 = {
        "response" : arr,
        "req" : req
      }
      
      resolve(ob1)
    }).catch((err) => {
      console.log(err)
      reject("Error");
    });
  });
  return pr;
}

const searchMovieByCountryLocation = async(country_name,lat,lon,distance,from,size) => {
  country_name = country_name.toLowerCase();
  //console.log(lat,lon,country_name)
  let index = crypto.createHash('md5').update(country_name.toString()).digest("hex");
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: index,
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
        "query": {
          "nested": {
            "path" : "Cities",
            "query" : {
              "bool": {
                "must": {
                  "match_all": {},
                },
                "filter": {
                  "geo_distance": {
                    "distance": String(distance) + "km",
                    "Cities.Location": {
                      "lat": lat,
                      "lon": lon
                    }
                  }
                },
              }
             },
             "inner_hits": {},
          }
        }  
      },
    }).then(function(resp) {
      let arr = []
      try {
        if(resp.body.hits.hits[0] != undefined)
        {
          let inner_hits_data = resp.body.hits.hits[0].inner_hits.Cities.hits.hits[0]._source;
          for(let i in resp.body.hits.hits)
          {
            let data = resp.body.hits.hits[i];
            //console.log(data)
            data._source.Cities = [inner_hits_data];
            delete data.inner_hits;
            arr.push(data)
          }
        }
      } catch (error) {
        console.log(error)
      }
      resolve(arr);
    }).catch((err) => {
      console.log(err)
      reject("Error");
    });
  });
  return pr;
}


const wildCardSearchOnCastDirectorsWriters = async(name,from,size) => {
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: '_all',
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
          "query" : {
            "bool" : {
                "should" : [
                 {
                  "nested" : {
                    "path" : "Actors",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Actors.Name":name}},
                        ],
                      }
                  },
                  },
                },
                {
                  "nested" : {
                    "path" : "Writer",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Writer.Name":name}},
                        ],
                      }
                  },
                  },
                },
                {
                  "nested" : {
                    "path" : "Director",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Director.Name":name}},
                        ],
                      }
                  },
                  },
                },
              ]
            },
           
        },
         
      },
    }).then(function(resp) {
      resolve(resp.body.hits.hits)
    }).catch((err) => {
      console.log(err)
      reject("Error");
    });
  });
  return pr;
}

const wildCardSearchOnAllFields = async(name,from,size) => {
  console.log(from,size)
  let pr = new Promise(async(resolve,reject) => {
    await client.search({
      index: '_all',
      from: from,
      size : size,
      body: {
        "_source": ["Title","Released","Poster","Runtime","Genre","Actors","Writer",
        "Director","Plot","Language","imdbRating","imdbVotes","imdbID","Cities","Country","Reviews"],
          "query" : {
            "bool" : {
                "should" : [
                 {
                  "nested" : {
                    "path" : "Actors",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Actors.Name":name}},
                        ],
                      }
                  },
                  },
                },
                {
                  "nested" : {
                    "path" : "Writer",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Writer.Name":name}},
                        ],
                      }
                  },
                  },
                },
                {
                  "nested" : {
                    "path" : "Director",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": { "Director.Name":name}},
                        ],
                      }
                    },
                  },
                },
                {
                  "nested" : {
                      "path" : "Cities",
                      "query" : {
                        "dis_max": {
                          "queries": [
                            { "term": {"Cities.Name":name}},
                          ],
                        }
                      },
                  },
                },
                {
                  "nested" : {
                    "path" : "Country",
                    "query" : {
                      "dis_max": {
                        "queries": [
                          { "term": {"Country.Name":name}},
                        ],
                      }
                    },
                  },
                },
                {
                  "dis_max": {
                    "queries": [
                      { "term": {"Title":name}},
                    ],
                  }
                }
              ]
            },
        },
      },
    }).then(function(resp) {
      resolve(resp.body.hits.hits)
    }).catch((err) => {
      reject("Error");
    });
  });
  return pr;
}


module.exports = {
  searchMoviesByGenre,
  searchMoviesByRating,
  searchMovieByCountry,
  searchMovieByCountryCity,
  searchMovieByCountryLocation,
  wildCardSearchOnCastDirectorsWriters,
  searchMostPopularMovies,
  wildCardSearchOnAllFields
};
