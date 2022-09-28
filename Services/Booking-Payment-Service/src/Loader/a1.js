const movieData = require('../Json Files/movies_city - Formatted.json').Movies;
const CountryStatesCity_data = require('../Json Files/countries+states+cities.json');
const theaters = require('../Json Files/theaters.json').theaters
const fs = require('fs')
var crypto = require('crypto');

const random = (min = 0, max = 50) => {
    let num = Math.random() * (max - min) + min;
    return Math.round(num);
  }

let cityTheatersDict = {}
for(let i=0; i<CountryStatesCity_data.length; i++)
{
    for(let j=0; j<CountryStatesCity_data[i].states.length;j++)
    {
        for(let k=0; k<CountryStatesCity_data[i].states[j].cities.length; k++)
        {

            let r1 = random(1,3);
            let arr = []
            let dict = {}
            let CountryName = CountryStatesCity_data[i].name;
            let CityName = CountryStatesCity_data[i].states[j].cities[k].name;
            let State = CountryStatesCity_data[i].states[j].name
            for(let k1=0; k1<theaters.length;k1++)
            {
                dict[k1] = 1;
            }
            for(let k1=0; k1<r1;k1++)
            {
             let r2 = random(0,theaters.length-1);
             if(dict[r2] == 1)
             {
                dict[r2] = 0;
                let obj = {
                    "TheaterName" : CityName + " " + theaters[r2].name,
                    "TheaterId" : crypto.createHash('md5').update(CityName + " " + theaters[r2].name.toString()).digest("hex")
                }
                arr.push(obj);
             }
             else
             {
                k1--;
             }
            }
            cityTheatersDict[[CityName,CountryName,State]] = arr;

        }
    }
}

let arr = []
for(let i in cityTheatersDict)
{
    let a = i.split(',')
    arr.push({
        "CityName" : a[0],
        "Theaters" : cityTheatersDict[i],
        "CountryName" : a[1],
        "State" : a[2]
    });
}

  let obj = {
    "TheatersCity" : arr
    }
    let json = JSON.stringify(obj);
    fs.writeFile('theaters_city.json',json, 'utf-8',(err) => {
    if(err)
    {
    console.log(err)
    }
})
