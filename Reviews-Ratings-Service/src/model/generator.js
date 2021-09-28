const mongoose = require('mongoose')
const movies = require('./schema.js');
const {v4 : uuidv4} = require('uuid')
const movies_data = require('./movies.json');
const { add } = require('winston');

async function run() {
    let url = 'mongodb://localhost:27017';
     mongoose.connect(url, {
        useNewUrlParser : true, 
        useUnifiedTopology: true, 
        "user" : "mongo-user",
        "pass" : "mongo-pw"   
      }).then(() => console.log("connected")).catch(err => console.error(err));
      add_movies();
}
async function add_review(data)
{
  console.log(data)
  movies.findOneAndUpdate({movie_id : data.movie_id},
    {
      $push: {
        reviews : {
          review_id :  data.text, 
          review : "hello"
        },
      },
     
    
    }).then(() => console.log("Comment Inserted")).catch(err => console.error(err))
    
}
async function del_review(data)
{

  movies.updateOne({movie_id : data.movie_id},
    {
      $pull : {
        reviews : {
          review_id : data.text
        },
      },
    
    
    }).then(() => console.log("Comment Inserted")).catch(err => console.error(err))
    
}
async function up_review(data)
{
  
  movies.updateOne({movie_id: data.movie_id},
    {
       $set : {"comments.$[elem].comment" : "I love alekhya and she love me too"}
    },
    {
      arrayFilters : [{"elem.comment_id" : data.text}]
    }).then(() => console.log("Comment Inserted")).catch(err => console.error(err))
    
}
async function add_movies()
{
    for(let i = 0;i<2;i++)
    {
        let obj = {
            id : i,
            movie_name : movies_data[i].title,
            rating : 0,
        }
        let data = new movies({
            movie_name : movies_data[i].title,
            movie_id : movies_data[i].id,
         });
        // let result = await data.save().then(() => console.log("Inserted")).catch(err => console.error(err))
       
        
    
    }
    // movies.collection.drop()
    let obj1 = {
        "movie_id" : "tt0068646",
        "text" : "h8xjsdl"


    }
    let obj2 = {
        "movie_id" : "tt0068646",
        "text" : "h8xjxlxlsdl"


    }
    // add_review(obj1)
    // add_review(obj2)
    
    let a = await movies.find({movie_id : "tt0068646"});
    console.log(a[0])
    
}
run()
