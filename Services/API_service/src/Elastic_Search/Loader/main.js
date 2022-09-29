const { Client } = require('@elastic/elasticsearch');
const client = new Client({ node: 'http://localhost:9200' })
const movies = require('../JsonFiles/Movies_main.json').Movies;
const country_and_states_cities = require('../JsonFiles/countries+states+cities.json');
const movies_data = require('../JsonFiles/movies_city.json').Movies;
const positive_reviews = require('../JsonFiles/postiveReviews - Formatted.json').positive_reviews;
const negative_reviews = require('../JsonFiles/negativeReviews - Formatted.json').negative_reviews;
var crypto = require('crypto');
const {v4 : uuidv4} = require('uuid');

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
                },
                "Reviews" : {
                    "type" : "nested",
                    "properties" : {
                        "reviewId" :  {"type" :"keyword"},
                        "username" : {"type" : "keyword"},
                        "review" :     {"type" : "keyword"},
                        "rating" :    {"type" : "keyword"}
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
const random = (min, max) => {
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

const add_positive_reviews = async(rating,num) => {
    try {
      let positive_count = num;
      let positive_dict = {};
      let arr = []
      for(let j=0; j<positive_count; j++)
      {
         let random_number = random(0,positive_count);
         if(positive_dict[random_number] == undefined)
         {
           let obj = {
                 "reviewId" :  uuidv4(),
                 "username" : "",
                 "review" : positive_reviews[random_number],
                 "rating" : random(parseInt(rating),10)
           }
           positive_dict[random_number] = 0;
           arr.push(obj);
         }
         else
         {
          j = j - 1;
         }
       }
       return arr;
    } catch (error) {
      console.log(error);
    }
      return []
}


const add_negative_reviews = async(rating,num) => {
     
      try {
        let negative_count = num;
        let negative_dict = {}
        let arr = []
        for(let j=0; j<negative_count; j++)
        {
          let random_number = random(0,negative_reviews.length);
          if(negative_dict[random_number] == undefined)
          {
            let obj = {
                  "reviewId" :   uuidv4(),
                  "username" : "",
                  "review" : negative_reviews[random_number],
                  "rating" : random(0,parseInt(rating))
            }
            negative_dict[random_number] = 0;
            arr.push(obj)
          }
          else
          {
            j = j - 1;
          }
        }
        return arr;
      } catch (error) {
        console.log(error); 
      }   
      return []
}

const generateReviews = async(source) => {
    let num = parseInt(source.imdbRating)
    if(num !=0 && source.Response != 'False')
    {
        let arr1 = await add_positive_reviews(source.imdbRating,3);
        let arr2 =  await add_negative_reviews(source.imdbRating,2);
        let arr3 = arr1.concat(arr2);
        return arr3;
    }
    return []
}

const getAll = async() => {
   const a =  await client.search({
        index: '_all',
        body: {
            "query" : {
                "match_all" : {}
            }
        }
    })
    // let list = a.body.hits.hits;
    // for(let i=0; i<list.length; i++)
    // {
    //     console.log(list[i]._source.Reviews)
    // }

}

const indexing1 = async() => {
    try {
        await delete_all_indexs();
        await clusterSettings();
    } catch (error) {
        console.log(error)
    }
    //await getAll()
    for(let i=0 ; i<movies_data.length; i++)
    {
        let source = movies_data[i]._source;
        if(source.Response != 'False') {
        let count = 0;
        for(let j in movies[i].Country)
        {
            count = count + 1;
        }
        let reviews = await generateReviews(source);
        source.Reviews = reviews;
       
        try {
            let index = await getIndex(count,source)
            let response = await check_index_exits(index);
            if(response.statusCode == 404)
            {
                await create_index(index);
                await putMapping(index);
            }
            await add_index_value(index,source)
            //console.log(index)
            console.log('Document ' +i+ ' Added')
        } catch (error) {
            console.log(error)
        }   
       }
    }
}
console.log('Data Loading')
indexing1();