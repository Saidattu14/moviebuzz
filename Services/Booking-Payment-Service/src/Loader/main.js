const movie_data = require('../Json Files/movies_city.json').Movies;
const CountryStatesCity_data = require('../Json Files/countries+states+cities.json')
const { v4: uuidv4 } = require('uuid');
const {Client} = require('pg');
const {checkTableExits,InsertRow,createTable,dropTable,rowsData, createIndex,postgreSqlExplain} = require('./database_queries')
require('dotenv').config({ path: '../../.env' });
const seats = require('../Json Files/seats.json')
const theatersCity = require('../Json Files/theaters_city.json').TheatersCity;


const client = new Client({
    user: 'user',
    host: 'localhost',
    password: '123456'.toString(),
    port: '5432',
    database : 'default_database'
  })
  const movie_table = `CREATE TABLE Movie_Information(
    id varchar(255) PRIMARY KEY,
    Country varchar(255),
    State varchar(255),
    City varchar(255),
    Theater_id varchar(255) NOT NULL,
    Theater_name varchar(255),
    Movie_id varchar(255),
    Movie_name varchar(255),
    Available_Bookings jsonb
  )`;
  const movie = `CREATE TABLE survey(
    id int,
    questions jsonb NOT NULL
  )`;

try {
  client.connect();
  console.log("Database Connected")
} catch (error) {
  console.log("DataBase is not Connected")
}
async function run()
{
      let res = await checkTableExits(client);
      console.log(res)
        if(res == 'Table doesnt Exits')
        {
          await createTable(client);
          let a = 0;
          let showsData = generateShowsData();
          let theatersCity = getTheatersCityDict();
          for(let i =0; i<movie_data.length; i++)
          {
            let src = movie_data[i]._source;
            for(let j=0; j<src.Cities.length && src.Year >= 2017; j++)
            {
              
              let obj = {
                "Movie_name" : src.Title,
                "Movie_id" : src.imdbID,
                "City" : src.Cities[j].Name
              }
              a = a + await InsertRow(client,obj,showsData,theatersCity);
            } 
            console.log("Movie Booking Data Added " + i);
          }
          
          end(client);
          console.log(a)
        }
        else if(res == 'Table Already Exits')
        {
          //await rowsData(client);
          //await postgreSqlExplain(client)
          //await dropTable(client)
          await createIndex(client)
        }
        await client.end();
}
run();

const end = (client) => {
  client.end();
}


const generateShowsData = () => {
        let  d = new Date().toLocaleString().split('/'); 
        let dates = []
        for(let i =0 ; i<=3;i++)
        {
          let d1 = new Date(parseInt(d[2]),parseInt(d[1])-1,parseInt(d[0])+i)
          dates.push(d1.toLocaleDateString())
        }
        let Show_timings = ["10:00 A.M", "01:00 P.M", "03:30 P.M", "06:00 P.M"]
        let Available_seats = seats;
        let arr = []
        for(let j = 0; j<=3; j++)
        {
            let obj = {
              "show_id" : j,
              "timings" : Show_timings[j],
              "Available_seats" : Available_seats,
            }
            arr.push(obj);
        }
        let arr1 = []
        for(let i = 0; i<=dates.length;i++)
        {
          let obj = {
          "Date" : dates[i],
          "shows_details" : arr
        }
        arr1.push(obj);
        }
        arr1 = JSON.stringify(arr1);
        return arr1;
}

const getTheatersCityDict = () => {
  let dict = {}
  for(let i=0; i<theatersCity.length; i++)
  {
    dict[theatersCity[i].CityName] = theatersCity[i];
  }
  return dict;
}