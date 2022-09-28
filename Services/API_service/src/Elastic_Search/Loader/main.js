const { Client } = require('@elastic/elasticsearch');
const client = new Client({ node: 'http://localhost:9200' })
const movies = require('../JsonFiles/Movies_main.json').Movies;
const country_and_states_cities = require('../JsonFiles/countries+states+cities.json');
const EventEmitter = require('events');
var crypto = require('crypto');
const emitter = new EventEmitter()
emitter.setMaxListeners(500);

const add_index_value = async(index,body) => {
    let pr = new Promise(async(resolve,reject) => {
        await client.index({
            index : index,
            body : body,
            }).then(function(resp) {
                resolve(resp);
                //console.log("Index value added");
            }).catch((err) => {
                reject(err)
                console.log(err)
        });
    });
    return pr;
}  

const putMapping = async(index) => {
    let pr = new Promise(async(resolve,reject) => {
      await client.indices.putMapping({
        index : index,
        body : {
            "properties" : {
                "Title" : {
                    "type" : "keyword"
                },
                "Year": {
                    "type" : "keyword"
                },
                "Rated": {
                    "type" : "keyword"
                },
                "Released": {
                    "type" : "keyword"
                },
                "Runtime": {
                    "type" : "keyword"
                },
                "Genre": {
                    "type" : "nested",
                    "properties" : {
                        "Id" : {
                            "type" : "integer"
                        },
                        "Type":  {
                            "type" : "keyword"
                        },
                }},
                "Director": {
                    "type" : "nested",
                    "properties" : {
                        "Id" : {
                            "type" : "integer"
                        },
                        "Name":  {
                            "type" : "keyword"
                        },
                }},
                "Writer": {
                    "type" : "nested",
                    "properties" : {
                        "Id" : {
                            "type" : "integer"
                        },
                        "Name":  {
                            "type" : "keyword"
                        },
                }},
                "Actors": {
                    "type" : "nested",
                    "properties" : {
                        "Id" : {
                            "type" : "integer"
                        },
                        "Name":  {
                            "type" : "keyword"
                        },
                }},
                "Plot": {
                    "type" : "keyword"
                },
                "Language": {
                    "type" : "keyword"
                },
                "Country": {
                    "type" : "nested",
                    "properties" : {
                        "Id" : {
                            "type" : "integer"
                        },
                        "Name" : {
                            "type" : "keyword"
                        },
                    }
                },
                "Awards": {
                    "type" : "keyword"
                },
                "Poster": {
                    "type" : "keyword"
                },
                "Ratings" : {
                    "type" : "nested",
                    "properties" : {
                        "Source" : {
                            "type" : "keyword"
                        },
                        "Value":  {
                            "type" : "keyword"
                        },
                    }
                },
                "Metascore": {
                    "type" : "integer"
                },
                "imdbRating": {
                    "type" : "scaled_float",
                    "scaling_factor": 100
                },
                "imdbVotes": {
                    "type" : "integer"
                },
                "imdbID": {
                    "type" : "keyword"
                },
                "Type": {
                    "type" : "keyword"
                },
                "DVD": {
                    "type" : "keyword"
                },
                "BoxOffice": {
                    "type" : "keyword"
                },
                "Production": {
                    "type" : "keyword"
                },
                "Website": {
                    "type" : "keyword"
                },
                "Response": {
                    "type" : "keyword"
                },
                "Cities" : {
                    "type" : "nested",
                    "properties" : {
                        "CityId" : {
                            "type" : "integer"
                        },
                        "Name" : {
                            "type" : "keyword"
                        },
                        "Location":  {
                              "type" : "geo_point",
                         },
                    }
                }
            }, 
          }
      }).then(function(resp) {
            resolve(resp);
      }).catch((err) => {
        reject(err)
        console.log(err)
      });
    });
    return pr;
}

const check_index_exits = async(index) => {

    let pr = new Promise(async(resolve,reject) => {
        await client.indices.exists({
            "index" : index
          }).then(function(resp) {
                resolve(resp);
              }).catch((err) => {
                reject(err);
                console.log(err);
         });
    });
    return pr;
}

const setting_index = async(index) => {

    return new Promise(async(resolve,reject) => {
        await client.indices.putSettings({
            index : index,
            body : {
                "index" : {
                    "number_of_replicas" : 1,
                    "number_of_shards" : 2, 
                }
            }
        }).then(function async(resp) {
           resolve(resp)
        }).catch((err) => {
            reject(err)
        });
    })
}

const create_index = async(index) => {
    return new Promise(async(resolve,reject) => {
        await client.indices.create({
            index : index,
            body : {
                "settings" : {
                    "number_of_shards" : 1,
                    "number_of_replicas" : 0,
                    "index.mapping.nested_objects.limit" : 50000
                },
            }
        }).then(function async(resp) {
           resolve(resp);
        }).catch((err) => {
            console.log(err)
            reject(err)
        });
    })
}


const delete_all_indexs = async() => {
    return await client.indices.delete({
        index : '_all'
    }).then((resp) => {
        return resp;
    }).catch((err) => {
        console.log(err)
        return err;
    });
}



const delete_index = async(index) => {
    return await client.indices.delete({
        index: index,
      }).then(function(resp) {
        return resp;
      }).catch((err) => {
        return err;
    });
}
const random = (min = 0, max = 50) => {
    let num = Math.random() * (max - min) + min;
    return Math.round(num);
}

const percentage  = (list) => {
    let length = list.length;
    let rd = random(80,100);
    let dt = parseInt((length/100)*rd);
    for(let i=0; i<length-dt;i++)
    {
      let new_random = random(0,length);
      list[new_random] = 0;
    }
    let new_arr = [];
    for(let i=0; i<list.length;i++)
    {
      if(list[i] !=0)
      {
        
        let new_obj = {
            "CityId" : list[i].id,
            "Location" : {
                "lat": parseFloat(list[i].latitude),
              "lon": parseFloat(list[i].longitude)
            },
            "Name" : list[i].name
        }
        new_arr.push(new_obj)
      }
    }
    return new_arr;
}

const checkCountryIndex = (country_dict,name) => {
    try {
        if(country_dict[name] != undefined && country_dict[name] != 'N/A')
        {
        let list = country_dict[name];
        list = percentage(list);
        return [true,list];
        }
    } catch (error) {
        console.log(error);
        return [false,[]];
    }
    return[false,[]];
}

const clusterSettings = async() => {
    await client.cluster.putSettings({
        body : {
            "persistent" : {
                "cluster.max_shards_per_node": 1000,
                "cluster.routing.allocation.enable": "all",
                "cluster.routing.allocation.total_shards_per_node" : -1,
              }
        }
       });
}

const getIndex = async(count,data) => {

    return new Promise(async(resolve,reject) => {
        let index = '';
        if(count == 0)
        {
          index = 'N/A';
        }
        else if(count == 1)
        {
         index = data.Country[0].Name.toLowerCase()
        }
        else
        {
         index = String(data.Year);
        }
        index = crypto.createHash('md5').update(index.toString()).digest("hex");
        resolve(index);
    }).catch((err) => {
        return err;
    });
    
}


const indexing = async() => {

    let country_dict = [];
    for(let i in country_and_states_cities)
    {
        let arr = [];
        for(let j in country_and_states_cities[i].states)
        {
            for(let k in country_and_states_cities[i].states[j].cities)
            {
                arr.push(country_and_states_cities[i].states[j].cities[k])
            }
        }
        let list = country_and_states_cities[i].name.split(",");
        for(let j = 0; j<list.length;j++)
        {
            country_dict[list[j]] = arr;
        }
    }
   
    try {
        await delete_all_indexs();
        await clusterSettings();
    } catch (error) {
        console.log(error)
    }
   
    for(let i=0; i<movies.length;i++)
    {
        let total_list = [];
        let count = 0;
        for(let j in movies[i].Country)
        {
            let result = checkCountryIndex(country_dict,movies[i].Country[j].Name);
            if(result[0] == true)
            {
                total_list = total_list.concat(result[1]);
                count = count + 1;
            }
            movies[i].Cities = total_list;
           
           // console.log(movies[i],total_list)
        }
        movies[i].Cities = total_list;
        try {
            let index = await getIndex(count,movies[i])
            let response = await check_index_exits(index);
            if(response.statusCode == 404)
            {
                await create_index(index);
                await putMapping(index);
            }
            await add_index_value(index,movies[i])
            //console.log(index)
            console.log('Document ' +i+ ' Added')
        } catch (error) {
            console.log(error)
        }
    }
}

console.log('Data Loading')
indexing();