const movie_data = require('../JsonFiles/main_data.json');
const { v4: uuidv4 } = require('uuid');
const {Client} = require('pg');
const {checkTableExits,insertRow,createTable,dropTable,rowsData, createIndex,postgreSqlExplain} = require('./database_queries')
require('dotenv').config({ path: '../../.env' });
const seats = require('../JsonFiles/seats.json')
const theaters = require('../JsonFiles/theaters.json')

const client = new Client({
  user: 'user',
  host: '127.0.0.1',
  password: "123456".toString(),
  port: 5432,
  database : 'my_database'
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
} catch (error) {
  console.log("DataBase is not Connected")
}

const random = (min = 0, max = 50) => {
      let num = Math.random() * (max - min) + min;
      return Math.round(num);
    }
async function run()
{
      let res = await checkTableExits(client);
        if(res == 'Table doesnt Exits')
        {
          await createTable(client);
          let showsData = generateShowsData();
          for(let i =0; i<movie_data.length; i++)
          {
            for(let k=0; k<movie_data[i].Country.length;k++) {
              let src = movie_data[i].Country[k];
              for(let j=0; j<src.Cities.length; j++)
              {
                let theater_data = theaters[random(0,theaters.length-1)]
                await insertRow(client,src.Name,src.Cities[j].State,
                  theater_data.Theater_Name,theater_data.id,movie_data[i].imdbID,movie_data[i].Title,showsData,src.Cities[j].Name)
              } 
            }  
            console.log("Movie Booking Data Added " + i);
          }
          await createIndex(client)
          end(client);
        }
        else if(res == 'Table Already Exits')
        {
          // await rowsData(client);
          // await postgreSqlExplain(client)
          await dropTable(client)
          // await createIndex(client)
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