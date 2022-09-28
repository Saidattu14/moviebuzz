const queryAvailability = async function query_run_select(data,client)
{
    const query51 = {
        name :'fetch-user_select',
        text: `SELECT (seat_status) as dt from movie_information,
        LATERAL JSONB_ARRAY_ELEMENTS(available_booking_data) show_details_data,
        LATERAL JSONB_ARRAY_ELEMENTS(show_details_data->$4) available_seats_data,
        LATERAL JSONB_ARRAY_ELEMENTS(available_seats_data->$7) rows_seats,
        LATERAL JSONB_ARRAY_ELEMENTS(rows_seats->$10) seat_status
        where City = $1 AND Movie_name  = $15 AND theater_id = $16 
        AND show_details_data->>$2=$3 
        AND available_seats_data->>$5=$6 
        AND rows_seats->>$8=$9 AND seat_status->>$11=$12 AND seat_status->>$13=$14`,
        values: [
          data.city,
          'Date',
          data.Date,
          'shows_details',
          'show_id',
           data.show_id,
          'Available_seats',
          'row_id',
          data.row_id,
          'seats',
          'seat_id',
          data.seat_id,
          'status',
          'UNBOOKED',
           data.movie_name,
           data.theater_id],
    }
    
    let pr = new Promise((resolve,reject) => {
        client.query(query51,function(err,res){
            if(err)
            {
            reject(err)
            }
            else
            {
            
                if(res.rowCount == 0)
                {
                    resolve("UNAVAILAVLE");
                }
                else
                {
                    resolve("AVAILABLE");
                }
            }
        })
    }).catch((err)=> {
        console.log(err)
    })
    return pr;
}

// let obj = {
//     "state" : 'Siliana Governorate',
//     "country_name" : 'Tunisia',
//     "movie_name" : "Noura's Dream",
//     "theater_id" : 'da7448b55f0507b6006f503e6a2348d8',
//     "date" : '16/8/2022',
//     "show_id" : '1',
//     "seat_id" : "A4",
//     "row_id" : "A",
//     "city" : 'Siliana'
//   }

module.exports = {
    queryAvailability
};