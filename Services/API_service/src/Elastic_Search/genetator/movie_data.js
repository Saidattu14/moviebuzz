const fs = require('fs')
const country_states = require('./gistfile1.json').countries
const genres = require('./genres.json').genres
const movies = require('./movies.json')
var states = []
const random = (min = 0, max = 50) => {
    let num = Math.random() * (max - min) + min;
    return Math.round(num);
}
for(let i = 0;i<country_states.length;i++)
{
    for(let j = 0; j < country_states[i].states.length;j++)
    {
        let obj = {
            country : country_states[i].country,
            state : country_states[i].states[j],
        }
        states.push(obj)
    }
}
var genres_list = []
for(let i = 0;i<genres.length;i++)
{
    genres_list.push(genres[i].name);
}

var movies_names_list = []
for(let i = 0;i<movies.length;i++)
{
    let obj = {
        id : i,
        movie_name : movies[i].title,
        rating : 0,
        genre : genres_list[random(0,genres_list.length - 1)]

    }
    movies_names_list.push(obj);
}
var data = []
var ct = 0;
for(let i = 0;i<states.length;i++)
{
    var mySet = new Set();
    var rt = random(1,7)
    while (rt != mySet.size)
    {
        ct = ct + 1;
        let value = random(0,movies_names_list.length -1);
        mySet.add(movies_names_list[value])
    }
    var arr = Array.from(mySet);
    let obj = {
        country : states[i].country,
        state : states[i].state,
        movies : arr
    }
    data.push(obj)
}
let obj = {
    data : data
}
let json = JSON.stringify(obj)
fs.writeFile('data.json',json, 'utf-8',(err) => {
    if(err)
    {
 console.log(err)
    }
})
console.log(ct)