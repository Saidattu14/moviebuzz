const kafka = require('./Kafka_consumer/reviews_consumer')
const kafka_producer = require('./Kafka_producer/produce-reviews-data')
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
  await movies.updateOne({movie_id: data.movieId},
    {
       $set : {"reviews.$[elem].review" : data.review}
    },
    {
      arrayFilters : [{"elem.review_id" : data.reviewId}]
    }).then(() => console.log("Review Added")).catch(err => console.error(err))
}

async function add_review(data)
{
  try {
    await movies.findOneAndUpdate({movie_id : data.movieId},
      {
        $push: {
          reviews : {
            review_id : uuidv4(), 
            review : data.review,
            username : data.username,
            rating :  data.rating
          },
        },
    });
    console.log("Review Added");
  } catch (error) {
    console.log("Error")
  }
}
async function delete_review(data)
{
  await movies.updateOne({movie_id : data.movieId},
    {
      $pull: {
        reviews : {
          review_id : data.reviewId,
        }
      }
    }).then(() => console.log("Review Deleted")).catch(err => console.error(err))
}


async function get_reviews(data)
{
  try {
    const movies_data = await movies.find({movie_id : data.movieId});
    //console.log(movies_data)
    let result = {
      "reviews" : movies_data[0].reviews,
      "requestId" : data.requestId,
      "movieId" : data.movieId
    }
    kafka_producer.sendReviewsResponseEvent(result,(err) => {
      console.log(err)
    });
  } catch (error) {
    console.log(error)
  }
}

kafka.consumer.connect();
kafka.consumer.on('ready', () => {
  console.log('Review and Rating consumer ready!')
  kafka.consumer.subscribe(['RequestReviewsTopic'])
  kafka.consumer.consume();
}).on('data', (data) => {
  
   try {
        let message = JSON.parse(data.value.toString())
        let event_type = message.eventType
        console.log(message)
        if (event_type == "updateReview")
        {
          
          update_review(message.payload);
          kafka.consumer.commitMessage(data)
        }
        else if(event_type == "addReview")
        {
          
          add_review(message.payload);
          kafka.consumer.commitMessage(data);
        }
        else if(event_type == "deleteReview")
        {
          delete_review(message.payload);
          kafka.consumer.commitMessage(data);
        }
        else if(event_type == 'getReviewsForMovie')
        {
          
          get_reviews(message.payload)
          kafka.consumer.commitMessage(data);
        }
        else
        {
          kafka.consumer.commitMessage(data);
        }
    }
   catch (err) {
        console.error(err);
    }
});
run();