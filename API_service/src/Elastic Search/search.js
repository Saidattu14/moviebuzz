const search_moviebygenre = async(client,obj) => {
  
  let pr = new Promise((resolve,reject) => {
        client.search({
          index: obj.state,
          body: {
            query: {
              "nested" : {
                  "path" : "movies",
                  "query" : {
                      "match" : {"movies.genre" : obj.genre}
                    },
                  },
              },
          }
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res.body.hits.hits)
      }, function(err) {
        reject(err.message)
        console.trace(err.message);
      });
  })
  return pr
}
const search_moviebyname = async(client,obj) => {
  console.log(obj)
  let pr = new Promise((resolve,reject) => {
        client.search({
          index: obj.state,
          body: {
            query: {
              "nested" : {
                  "path" : "movies",
                  "query" : {
                      "match" : {"movies.movie_name" : obj.movie_name}
                    },
                  },
              },
          }
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res.body.hits.hits)
      }, function(err) {
        reject(err.message)
        console.trace(err.message);
      });
  })
  return pr
}
const search_movies = async(client,obj) => {
  let pr = new Promise((resolve,reject) => {
    client.search({
      index: obj.state.toLowerCase(),
      body: {
        "query": {
          "nested" : {
            "path" : "movies",
            "query" : {
                "match_all" : {}
              },
            
            },
      },
    },
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res.body.hits.hits)
      }, function(err) {
        reject(err.message)
        console.trace(err.message);
      });
  })
  return pr
}

const search_moviebyrating = async(client,obj) => {
  let pr = new Promise((resolve,reject) => {
    client.search({
      index: obj.state.toLowerCase(),
      body: {
        "query": {
          "nested" : {
            "path" : "movies",
            "query" : {
                "match_all" : {}
              },
            
            },
      },
      "sort" : [
        {
        "movies.id" : {
          "order" : "desc",
          "nested" : {
           "path" : "movies",
           "filter" : {
             "match_all" : {}
           },
           
          }
        },
       
       },
       
      ],
    },
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res.body.hits.hits)
      }, function(err) {
        reject(err.message)
        console.trace(err.message);
      });
  })
  return pr;
}
module.exports = {search_moviebygenre,search_moviebyname,search_moviebyrating,search_movies};
