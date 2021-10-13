const bluebird = require('bluebird');
const Query = async function query_run_select(data,client)
{
    
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
          data.theater_id,
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
          'UNBOOKED'],
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
        return err;
    })
    return pr;
}

module.exports = Query;