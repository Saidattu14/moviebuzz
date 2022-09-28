const { Client } = require('@elastic/elasticsearch');
const { error } = require('winston');
const testJson = require('./test.json').movies;
const client = new Client({ node: 'http://localhost:9200' })
const index = 'india'


const add_index_value = async(val,val1) => {
    client.index({
          index : index,
          body : val,
          id : val1,
          
        }).then(function(resp) {
              console.log("Index value added");
            }, function(err) {
              console.trace(err.message);
    });
}  

const search_movies = async() => {
  let pr = new Promise((resolve,reject) => {
    client.search({
      index: index,
      body: {
        "query": {
            "nested" : {
                "path" : "Cities",
                "query" : {
                  "dis_max": {
                    "queries": [
                      { "term": { "Cities.CityId":4} },
                      { "term": { "Cities.CityId":2} },
                      
                    ],
                  }
              },
            },
      },
    },
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res.body.hits.hits)
      }).catch((err) => {
        reject(err.message)
        console.trace(err.message);
      });
  })
  return pr;
}

const map_indexing = async(obj) => {
   return await client.indices.putMapping({
    index : index,
    body : {
        "properties" : {
            "Title" : {"type" : "keyword"},
          }
        },
  })
}


const delete_index = async() => {
    client.indices.delete({
        index: index,
      }).then(function(resp) {
        console.log("Successful query!");
        console.log(JSON.stringify(resp, null, 4));
      }, function(err) {
        console.trace(err.message);
    });
}

const search = async() => {
  let pr = new Promise(async(resolve,reject) => {
        await client.search({
          index: index,
          body: {
            "query": {
              "match_all" : {}
           },
        },
      }).then(function(res) {
         console.log(res.body.hits.hits)
         resolve(res)
      }).catch((error) => {
        reject(error)
        console.log(error);
      });
  });
  
}


const create_index = async() => {

    await client.indices.delete({
        index : '_all'
    }).then((resp) => {
        console.log(resp)
    });


    await client.cat.indices({
        index : '_all'
    }).then((resp) => {
        console.log(resp)
    });


  await client.cluster.putSettings({
    body : {
        "persistent" : {
            "cluster.max_shards_per_node": 30,
            "cluster.routing.allocation.enable": "all",
            "cluster.routing.allocation.total_shards_per_node" : -1,
          }
    }
   });

  //  await client.indices.putTemplate({
  //   "name" : "temp",
  //   body : {
  //     "index_patterns": ["india*"],
  //   "template": {
  //     "aliases": {
  //       "mydata": { 
  //         "actions": [
  //           {
  //             "add": {
  //               "search_routing": 2,
  //               "index_routing": 2
  //             }
  //           }
  //         ]
  //       }
  //     }
  //   },
  //  }})

  await client.indices.create({
      index : index,
      body : 
      {
       
        "settings" : {
          "number_of_shards" : 2,
          "number_of_replicas" : 0,
      },
      "mappings" : {
        
          "properties" : {
            "Title" : {"type" : "keyword"},
          }
      }
      }
    }).then(function async(resp) {
          
        }, function(err) {
          console.trace(err.message);
    });

          for(let i=0; i<5;i++)
          {
           
           let obj = {
            "Title" : i.toString()
           }
           await add_index_value(obj,i,"hello1")
          }

    
  //await search();
   
  // map_indexing()
  
}
create_index()
