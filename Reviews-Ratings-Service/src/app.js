const kafka = require('./Kafka_consumer/search_consumer')
const mongoose = require('mongoose');
const movies = require('./model/schema');
const {v4 : uuidv4} = require('uuid')
async function run() {
  let url = 'mongodb://localhost:27017';
   mongoose.connect(url, {
      useNewUrlParser : true, 
      useUnifiedTopology: true, 
      "user" : "mongo-user",
      "pass" : "mongo-pw"   
    }).then(() => console.log("connected")).catch(err => console.error(err));
}
async function update_review(data)
{
  movies.updateOne({movie_id: data.movie_id},
    {
       $set : {"reviews.$[elem].review" : data.text}
    },
    {
      arrayFilters : [{"elem.review_id" : data.review_id}]
    }).then(() => console.log("Review Added")).catch(err => console.error(err))
}
async function add_review(data)
{
    movies.findOneAndUpdate({movie_id : data.movie_id},
    {
      $push: {
        reviews : {
          review_id :  uuidv4(), 
          review : data.text
        },
      },
    }).then(() => console.log("Review Added Successfully")).catch(err => console.error(err))
}
async function delete_review(data)
{
  movies.updateOne({movie_id : data.movie_id},
    {
      $pull: {
        reviews : {
          review_id : data.review_id,
        }
      }
    }).then(() => console.log("Review Deleted")).catch(err => console.error(err))
}
async function update_rating(data)
{
  movies.updateOne({movie_id: data.movie_id},
    {
       $set : {"ratings.$[elem].rating" : data.text}
    },
    {
      arrayFilters : [{"elem.rating_id" : data.rating_id}]
    }).then(() => console.log("Rating Updated")).catch(err => console.error(err))
}
async function delete_rating(data)
{
  movies.updateOne({movie_id : data.movie_id},
    {
      $pull: {
        ratings : {
          rating_id : data.rating_id,
        }
      }
    }).then(() => console.log("Rating Deleted")).catch(err => console.error(err))
  
}
async function add_rating(data)
{
  movies.updateOne({movie_id : data.movie_id},
    {
      $push: {
        ratings : {
          rating_id : uuidv4(),
          rating :  data.rating
        }
      }
    }).then(() => console.log("Rating Added")).catch(err => console.error(err))
}
kafka.consumer.connect();
kafka.consumer.on('ready', () => {
  console.log('Review and Rating consumer ready!')
  kafka.consumer.subscribe(['Reviews_and_Rating'])
  kafka.consumer.consume();
}).on('data', (data) => {
  
   try {
        let message = JSON.parse(data.value.toString())
        console.log(message)
        let event_type = message.eventType
        if (event_type == "update_review")
        {
          update_review(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "add_review")
        {
          add_review(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "delete_review")
        {
          delete_review(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "add_rating")
        {
          add_rating(message.payload)
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "delete_rating")
        {
         delete_rating(message.payload)
         kafka.consumer.commitMessage(data)
        }
        else if(event_type == "update_rating")
        {
          update_rating(message.rating)
          kafka.consumer.commitMessage(data)
        }
        else
        {
          kafka.consumer.commitMessage(data)
        }
    }
   catch (err) {
        console.error(err)
        
    }
});
run()

