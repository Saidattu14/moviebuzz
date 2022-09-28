const mongoose = require('mongoose')
const movies = require('../model/schema.js');
const {v4 : uuidv4} = require('uuid');
const { count } = require('../model/schema.js');
const movies_data = require('../JsonFiles/Movies_main.json').Movies;
const positive_reviews = require('../JsonFiles/postiveReviews - Formatted.json').positive_reviews;
const negative_reviews = require('../JsonFiles/negativeReviews - Formatted.json').negative_reviews;
//const movies_data = require('../model/movies.json');

async function run() {

  try {
    let url = 'mongodb://localhost:27017';
     await mongoose.connect(url, {
        useNewUrlParser : true,
        useUnifiedTopology: true, 
        "user" : "mongo-user",
        "pass" : "mongo-pw",   
      });
      console.log("connected");
      await add_movies();
  } catch (error) {
    console.log(error)
  }
  
}
async function add_review(data)
{

  try {
    await movies.findOneAndUpdate({movie_id : data.movie_id},
      {
        $push: {
          reviews : {
            review_id :  data.review_id,
            username :   data.username,
            review :     data.review,
            rating :     data.rating
          },
        },
       
      })
  } catch (error) {
    console.log(error)
  }
}
async function delete_review(data)
{
  try {
    await movies.updateOne({movie_id : data.movie_id},
      {
        $pull : {
          reviews : {
            review_id : data.text
          },
        },
      })
  } catch (error) {
  console.log(error)
}
    
}


const random = (min = 0, max = 50) => {
  let num = Math.random() * (max - min) + min;
  return Math.round(num);
}

async function drop_collection()
{
  await movies.collection.drop();
}


const add_positive_reviews = async(index,num) => {

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
                 "movie_id" : movies_data[index].imdbID,
                 "review_id" :  uuidv4(),
                 "username" : '',
                 "review" : positive_reviews[random_number],
                 "rating" : random(parseInt(movies_data[index].imdbRating),10)
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


const add_negative_reviews = async(index,num) => {

      try {
        let negative_count = 30 - num;
        let negative_dict = {}
        let arr = []
        for(let j=0; j<negative_count; j++)
        {
          let random_number = random(0,negative_reviews.length);
          if(negative_dict[random_number] == undefined)
          {
            let obj = {
                  "movie_id" : movies_data[index].imdbID,
                  "review_id" :   uuidv4(),
                  "username" : '',
                  "review" : negative_reviews[random_number],
                  "rating" : random(0,parseInt(movies_data[index].imdbRating))
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



async function add_movies()
{
  await drop_collection();
  var starttime=(new Date()).getTime();
  for(let i =0; i<movies_data.length; i++)
  {
      
     try {
      let num = parseInt((movies_data[i].imdbRating*100)/30);
      if(num !=0 && movies_data[i].Response != 'False')
      {
           let arr1 = await add_positive_reviews(i,num);
           let arr2 =  await add_negative_reviews(i,num);
           let arr3 = arr1.concat(arr2);
           let data = new movies({
            movie_name : movies_data[i].Title,
            movie_id : movies_data[i].imdbID,
            reviews : arr3,
           });
           await data.save();
          console.log("Document Added "+i);
      }
     } catch (error) {
      console.log(error)
      console.log(movies_data[i])
     }
    }
    var endtime = (new Date()).getTime();
    var time_complexity = (endtime-starttime)/1000;
    console.log(time_complexity)

    await mongoose.connection.close();
  
// for(let i =0; i<10;i++)
// {
//   console.log( await movies.find({movie_id : movies_data[i].imdbID}))
// }

}
run()
