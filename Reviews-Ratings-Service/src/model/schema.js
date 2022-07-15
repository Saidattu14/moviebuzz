const mongoose = require('mongoose')
const Schema_movies = new mongoose.Schema({
    movie_name: {
        type: String,
        min :  8,
        max : 65,
        required: true,
       
    },
    movie_id : {
        type : String,
        required: true, 
    
    },
    reviews : [{
        review_id : {type : String, required : true},
        review : {type : String,required : true},
        },
    ],
    ratings : [{
        rating_data :  {
            type : String,
            required: true, 
            min : 1,
            max : 9
        },
        rating_id : {
            type : String,
            required: true,
        }   
    }],
});

module.exports =  mongoose.model('Movies',Schema_movies);