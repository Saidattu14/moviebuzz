const transactionQuery = async function query_run_select(data,client)
{
    let pr = new Promise(async (resolve,reject) => {
    try {
        await client.query('BEGIN');
        let failure = false
        for(let i =0 ;i<data.seating.length && failure == false;i++)
        {
        for(let j = 0;j<data.seating[i].seat_numbers.length;j++)
        {
            let row_id = data.seating[i].row_id;
            let seat_number = data.seating[i].seat_numbers[j];
            let query_update = {
                name :'up-user',
                text: `WITH available_booking_data as (
                  SELECT booking_avalability,id1
                  from movie_information,
                  JSONB_ARRAY_ELEMENTS(available_booking_data)
                  WITH ORDINALITY arr(booking_avalability,id1)
                  WHERE City = $1 AND Movie_name  = $15 AND booking_avalability->>$2=$3
                  ), show_details as (
                  SELECT show_details_selection,id1,id2
                  from available_booking_data,
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
                  SET available_booking_data = jsonb_set(available_booking_data,path,'"BOOKED"',false)
                  from seats_booking
                  where City = $1 AND Movie_name  = $15 AND theater_id = $16`
                  ,
                  values: [
                      data.cityName,
                      'Date',
                      data.date,
                      'shows_details',
                      'show_id',
                      data.show_id,
                      'Available_seats',
                      'row_id',
                      row_id,
                      'seats',
                      'seat_id',
                      seat_number,
                      'status',
                      'UNBOOKED',
                      data.movieName,
                      data.theater_id
                    ],
            }
            let result;
            try {
                result = await client.query(query_update);
            } catch (error) {
                console.log(error)
            }
            
            if(result.rowCount == 0)
            {
                failure = true
                break;
            }
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

module.exports = {
    transactionQuery
};