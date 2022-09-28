const mongoose = require('mongoose')
const Schema_movies = new mongoose.Schema(
    {
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
        review_id :  {type :String,required : true},
        username :   {type : String},
        review :     {type : String},
        rating :     {type : Number}
        },
    ],
});

module.exports = mongoose.model('Movies',Schema_movies);