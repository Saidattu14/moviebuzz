const { Client } = require('@elastic/elasticsearch')
const {createReadStream} = require('fs')
const split = require('split2')
const client = new Client({ node: 'http://localhost:9200' })
var obj = {
    "country": "afghanistan",
    "state":  "badakhshan",
    "movies": [
        {"id":17,"movie_name":"Forrest Gump","rating":0,"genre":"Family"},
        {"id":30,"movie_name":"Léon: The Professional","rating":0,"genre":"TV Movie"},
        {"id":86,"movie_name":"Singin' in the Rain","rating": 0,"genre":"Family"},
        {"id":93,"movie_name":"2001: A Space Odyssey","rating": 0,"genre":"Drama"},
        {"id":97,"movie_name":"The Apartment","rating": 0,"genre":"Adventure"}
    ]
  }

const add_index_value = async() => {
    client.index({
          index : obj.country,
          body : obj
        }).then(function(resp) {
              console.log("Index value added");
            }, function(err) {
              console.trace(err.message);
    });
}  

const map_indexing = async() => {
  client.indices.putMapping({
    index : obj.country,
    body : {
           
        "properties" : {
            "country" : {"type" : "keyword"},
            "state":  {"type" : "keyword"},
             "movies" : {
              "type" : "nested",
              "properties" : {
              "id" : {"type" : "double"},
              "movie_name":  {"type" : "keyword"},
              "rating" : {"type" : "integer"},
              "genre":  {"type" : "keyword"},
              }
            },
          
        }
    }
  }).then(function(resp) {
        console.log("Successful Mapping");
        add_index_value();
  }, function(err) {
        console.trace(err.message);
  });
}
const delete_index = async() => {

    client.indices.delete({
        index: obj.country,
      }).then(function(resp) {
        console.log("Successful query!");
        console.log(JSON.stringify(resp, null, 4));
      }, function(err) {
        console.trace(err.message);
    });
}

const search = async() => {
  let pr = new Promise((resolve,reject) => {
        client.search({
          index: obj.country,
          body: {
            "sort" : [
              {
                "movies.rating" : {
                  "order" : "asc",
                  "mode" : "min",
                  
                }
              }
            ],
            "query": {
              "nested" : {
                "path" : "movies",
                "query" : {
                  "match_all" : {}
                },
                "inner_hits" : {},
              }
              
           },
        },
      }).then(function(res) {
         console.log(res.body.hits.hits[0].inner_hits.movies.hits.hits)
         resolve(res)
      }, function(err) {
        reject(err.message)
        console.trace(err.message);
      });
  })

}

const create_index = async() => {
    
    // client.indices.create({
    //   index : obj.country
    // }).then(function(resp) {
    //       console.log("Index Created");
    //       map_indexing();
    //     }, function(err) {
    //       console.trace(err.message);
    // });
  
  search()
    
    
}
create_index()
