const seats = require('../Json Files/seats.json')
const { v4: uuidv4 } = require('uuid');
const PostgresExtras = require("postgres-extras").PostgresExtras;
require('dotenv').config({ path: '../../.env' });

const checkTableExits = async(client) => 
{
    const check = `SELECT *
    FROM pg_catalog.pg_tables
    WHERE schemaname != 'pg_catalog' AND 
        schemaname != 'information_schema'`;

    let pr = new Promise(async(resolve,reject) => {
       await client.query(check,function(err,res){
            var table_boolean = false;
            if(err)
            {
              reject(err);
              console.log(err);
            }
            else
            {
              var arr = res.rows;
              for(let i= 0; i<arr.length;i++)
              {
                if(arr[i].tablename == 'movie_information')
                {
                  table_boolean = true;
                  break;
                }
              }
              if(table_boolean == true)
              {
                resolve('Table Already Exits');
              }
              else
              {
                resolve('Table doesnt Exits')
              }
            }
          })
    }).catch((err)=> {
        return err;
    })
    return pr;
}


const dropTable = async(client) => {
  await client.query("DROP TABLE movie_information",(err,res) => {
      console.log(res);
   })
}

const rowsData = async(client) => {
  const query5 = {
    // give the query a unique name
    name :'fetch-user',
    text: `SELECT * from movie_information where City = $1`,
    values: ['India'],
  }
 await client.query(query5,(err,res) => {
    console.log(res,err)
  })
}

const createTable = async(client) => {
  const movie_table = `CREATE TABLE Movie_Information
  (
    id varchar(255) UNIQUE PRIMARY KEY,
    Country varchar(255) NOT NULL,
    State varchar(255) NOT NULL,
    City varchar(255) NOT NULL,
    Theater_id varchar(255) NOT NULL,
    Theater_name varchar(255) NOT NULL,
    Movie_id varchar(255) NOT NULL,
    Movie_name varchar(255) NOT NULL,
    Available_Booking_data jsonb NOT NULL
  )`;

  await client.query(movie_table,(err,res) => {
    if(err)
    {
      console.log(err)
    }
    else
    {
      console.log("Table Created")
    }
  });
}

const InsertRow = async(client,obj,showsData,theatersCity) => {

  const random = (min = 0, max = 50) => {
    let num = Math.random() * (max - min) + min;
    return Math.round(num);
  }
     if(theatersCity[obj.City] != undefined)
     {
     let dict = theatersCity;
     let state =  dict[obj.City].State;
     let Country = dict[obj.City].CountryName;
     let listTheaters = dict[obj.City].Theaters;
     let rd = random(1,listTheaters.length-1);
     let dict2 = {}
     for(let i=0; i<rd; i++)
     {
        let r1 = random(0,listTheaters.length-1);
        if(dict2[r1] == undefined)
        {
          // console.log(Country,state,listTheaters[r1].TheaterName,
          //   listTheaters[r1].TheaterId,obj.Movie_id,obj.Movie_name,arr,obj.City)
         await insert(client,Country,state,listTheaters[r1].TheaterName,
            listTheaters[r1].TheaterId,obj.Movie_id,obj.Movie_name,showsData,obj.City);
          dict2[r1] = 0;
        }
        else
        {
          i--;
        }
       //await insert("India","A.p",lst[i],"123","ABCD",arr1,"Kurnool");
     }   
     return 1;
    }
    return 0;
    
}


const insert = async(client,country,state,theater_name,theater_id,movie_id,movie_name,arr,city) => {

      let pr = new Promise(async(resolve,reject) => {
        const query_insert = {
          name :'insert-user',
          text: `INSERT INTO movie_information(
          id,
          Country,
          State,
          Theater_name,
          Movie_id,
          Movie_name,
          Available_Booking_data,
          City,
          Theater_id
        ) 
        VALUES($1,$2,$3,$4,$5,$6,$7,$8,$9)`,
        values: [uuidv4(),country,state,theater_name,movie_id,movie_name,arr,city,theater_id],
        }
        await client.query(query_insert,function(err,res) {
          if(err)
          {
            console.log(err);
            reject('Row Failed to Added');
          }
          else
          {
            resolve('Row Added SuccessFully')
          }
        })
        }).catch((err)=> {
            console.log(err);
            return err;
        })
        return pr;
}

const createIndex = async(client) => {
  
  let pr = new Promise(async(resolve,reject) => {
    const query_index =  `CREATE INDEX City
      ON movie_information(city) WITH (deduplicate_items = off)`;
    await client.query(query_index,function(err,res) {
      if(err)
      {
        console.log(err);
        reject('Row Failed to Added');
      }
      else
      {
        console.log(res)
        //resolve('Row Added SuccessFully')
      }
    })
    }).catch((err)=> {
        return err;
    })
    return pr;

}

const postgreSqlExplain = async(client) => {

  const database_url = process.env.DATABASE_URL
   console.log(await PostgresExtras.null_indexes())
} 

module.exports = {checkTableExits,InsertRow,createTable,dropTable,rowsData,createIndex,postgreSqlExplain};