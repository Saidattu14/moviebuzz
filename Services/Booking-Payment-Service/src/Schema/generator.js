const {Client} = require('pg');
const { v4: uuidv4 } = require('uuid');
const json_data = require('./data.json').data
const theater_names = require('./theaters.json').theaters
const client = new Client({
    user: 'user',
    host: 'localhost',
    password: '123456',
    port: 5431,
    database : 'default_database'
  })
  const movie_table = `CREATE TABLE Movie_Information(
    id varchar(255) PRIMARY KEY,
    Country varchar(255),
    State varchar(255),
    Theater_name varchar(255) ,
    Theater_id int ,
    Movie_id int,
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


run1(client)

} catch (error) {
  console.log("DataBase is not Connected")
}
async function run()
{
  const check = `SELECT *
  FROM pg_catalog.pg_tables
  WHERE schemaname != 'pg_catalog' AND 
      schemaname != 'information_schema'`;
  client.query(check,function(err,res){
    if(err)
    {
      console.log(err);
    }
    else
    {
      var arr = res.rows;
      for(let i= 0; i<arr.length;i++)
      {
        if(arr[i].tablename == 'movie_information')
        {
          console.log("Table Already created")
        }
      }
    }
  })
}
run()


async function run1(client)
{
  const random = (min = 0, max = 50) => {
    let num = Math.random() * (max - min) + min;
    return Math.round(num);
  }
  let Show_timings = ["10:00 AM", "01:00 PM", "3:30 PM", "6:00 PM"]
  let dates = ["10/2/2020", "11/2/2020", "12/2/2020", "13/2/20"]
  let Available_seats = [
    {
      "row_id" : 'A',
      "seats" : [
        {
          "seat_id" : 'A1',
          "status" :  'UNBOOKED' 
        },
        {
          "seat_id" : 'A2',
          "status" :  'UNBOOKED'
        },
        {
          "seat_id" : 'A3',
          "status" :  'UNBOOKED'
        },
        {
          "seat_id" : 'A4',
          "status" :  'UNBOOKED'
        },
      ]
    },
    {
      "row_id" : 'B',
      "seats" : [
        {
          "seat_id" : 'B1',
          "status" :  'UNBOOKED' 
        },
        {
          "seat_id" : 'B2',
          "status" :  'UNBOOKED'
        },
        {
          "seat_id" : 'B3',
          "status" :  'UNBOOKED'
        },
        {
          "seat_id" : 'B4',
          "status" :  'UNBOOKED'
        },
      ]
    },
  ]

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
  for(let i = 0; i<=3;i++)
  {
    let obj = {
      "Date" : dates[i],
      "shows_details" : arr
    }
    arr1.push(obj);
}

arr1 = JSON.stringify(arr1)


var theater_id = 0;
for(let i = 0; i<json_data.length;i++)
{
  let country = json_data[i].country
  let state = json_data[i].state
  let mv = json_data[i].movies
  for(let j = 0;j<json_data[i].movies.length;j++)
  {
    theater_id = theater_id + 1;
    let movie_name = mv[j].movie_name
    let movie_id = mv[j].id
    const query_insert = {
      name :'insert-user',
      text: `INSERT INTO movie_information(
      id,
      Country,
      State,
      Theater_name,
      Theater_id,
      Movie_id,
      Movie_name,
      Available_Bookings
    ) 
    VALUES($1,$2,$3,$4,$5,$6,$7,$8)`,
    values: [uuidv4(),country,state,theater_names[random(0,18)].name,theater_id,movie_id,movie_name,arr1],
    }
    // client.query(query_insert,function(err,res) {
    //   if(err)
    //   {
    //     console.log(err)
    //   }
    //   else
    //   {
    //   console.log("Added")
    //   }
    // })
   
  }
}
const query5 = {
  // give the query a unique name
  name :'fetch-user',
  text: `SELECT (js3) as dt from movie_information,
  LATERAL JSONB_ARRAY_ELEMENTS(available_bookings) obj(js),
  LATERAL JSONB_ARRAY_ELEMENTS(js->$4) js1,
  LATERAL JSONB_ARRAY_ELEMENTS(js1->$7) js2,
  LATERAL JSONB_ARRAY_ELEMENTS(js2->$10) js3
  where theater_id = $1 AND js->>$2=$3 AND js1->>$5=$6 AND js2->>$8=$9`,
  values: [13000,'Date','11/2/2020','shows_details','show_id',1,'Available_seats','row_id','A','seats'],
}
const query51 = {
  // give the query a unique name
  name :'fetch-user_select',
  text: `SELECT (seat_status) as dt from movie_information,
  LATERAL JSONB_ARRAY_ELEMENTS(available_bookings) show_details_data,
  LATERAL JSONB_ARRAY_ELEMENTS(show_details_data->$4) available_seats_data,
  LATERAL JSONB_ARRAY_ELEMENTS(available_seats_data->$7) rows_seats,
  LATERAL JSONB_ARRAY_ELEMENTS(rows_seats->$10) seat_status
  where theater_id = $1 
  AND show_details_data->>$2=$3 
  AND available_seats_data->>$5=$6 
  AND rows_seats->>$8=$9 AND seat_status->>$11=$12 AND seat_status->>$13=$14`,
  values: [
    13000,
    'Date',
    '11/2/2020',
    'shows_details',
    'show_id',
     1,
    'Available_seats',
    'row_id',
    'A',
    'seats',
    'seat_id',
    'A2',
    'status',
    'UNBOOKED'],
  }
let query_select = {
  name :'up-user',
  text: `WITH available_bookings as (
    SELECT booking_avalability,id1
    from movie_information,
    JSONB_ARRAY_ELEMENTS(available_bookings)
    WITH ORDINALITY arr(booking_avalability,id1)
    WHERE theater_id = $1 AND booking_avalability->>$2=$3
    ), show_details as (
    SELECT show_details_selection,id1,id2
    from available_bookings,
    JSONB_ARRAY_ELEMENTS((booking_avalability->$4)::jsonb)
    WITH ORDINALITY arr(show_details_selection,id2)
    where show_details_selection->>$5=$6
    ), seats_selection as (
      SELECT row_details,id1,id2,id3
      from show_details,
      JSONB_ARRAY_ELEMENTS((show_details_selection->$7)::jsonb)
      WITH ORDINALITY arr(row_details,id3)
      where row_details->>$8=$9
    ), seats_booking as (
      SELECT ('{'||id1-1||',shows_details,'||id2-1||',Available_seats,'||id3-1||',seats,'||id4-1||',status}')::text[] as path
      from seats_selection,
      JSONB_ARRAY_ELEMENTS((row_details->$10)::jsonb)
      WITH ORDINALITY arr(seat_selection,id4)
      where seat_selection->>$11=$12 AND seat_selection->>$13=$14
    )
    UPDATE movie_information
    SET available_bookings = jsonb_set(available_bookings,path,'"BOOKED"',false)
    from seats_booking
    where theater_id = $1`
    ,
    values: [13000,'Date','11/2/2020','shows_details','show_id',1,'Available_seats','row_id','A','seats','seat_id',"A2",'status','BOOKED'],
}

// var starttime=(new Date()).getTime();
// client.query(query_select,function(err,res) {
//     if(err)
//     {
//      console.log(err)
//     }
//     else
//     {
//       console.log(res)
//       var endtime = (new Date()).getTime();
//           var time_complexity = (endtime-starttime)/1000;
//           console.log(time_complexity)

//     }
// })
      client.query(query5,function(err,res) {
        if(err)
        {
         console.log(err)
        }
        else
        {
          console.log(res.rows)
          client.query(query51,function(err,res) {
            if(err)
            {
             console.log(err)
            }
            else
            {
              if(res.resCount == 0)
              {
                console.log("No Tickets Available");
              }

            }
        })
          
        }
    })
}