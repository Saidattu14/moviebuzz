const mongoose = require('mongoose')
const Schema_movies = new mongoose.Schema(
    {
    movieName: {
        type: String,
        min :  8,
        max : 65,
        required: true,
    },
    movieId : {
        type : String,
        required: true, 
    },
    reviews : [{
        reviewId :  {type :String,required : true},
        username :   {type : String},
        review :     {type : String},
        rating :     {type : Number}
        },
    ],
});

module.exports = mongoose.model('Movies',Schema_movies);