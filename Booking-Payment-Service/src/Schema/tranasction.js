const bluebird = require('bluebird');
const Transaction = async function query_run_select(data,client)
{
    let pr = new Promise(async (resolve,reject) => {
    try {
        await client.query('BEGIN');
        let failure = false
        for(let i =0 ;i<data.length;i++)
        {
            let update_data = data[i];
            let query_update = {
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
                  values: [
                      update_data.theater_id,
                      'Date',
                      update_data.Date,
                      'shows_details',
                      'show_id',
                      update_data.show_id,
                      'Available_seats',
                      'row_id',
                      update_data.row_id,
                      'seats',
                      'seat_id',
                      update_data.seat_id,
                      'status',
                      'UNBOOKED'
                    ],
            }
            let result = await client.query(query_update)
            if(result.rowCount == 0)
            {
                failure = true
                break;
            }
        }
        if(failure == true)
        {
            await client.query('ROLLBACK');
            reject('Failure')
        }
        else
        {
            await client.query('COMMIT');
            resolve('Success')
        }
    } catch (error) {
        await client.query('ROLLBACK');
        reject('Failure')
    }
   }).catch((err)=> {
    return err;
   });
   return pr;
}

module.exports = Transaction;