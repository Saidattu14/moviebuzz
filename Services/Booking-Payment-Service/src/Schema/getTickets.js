

const getTheatersTickets = async(data,client) => {
    const query5 = {
        name :'fetch-user',
        text: `SELECT * from movie_information where City = $1 AND Movie_name = $2`,
        values: [data.cityName,data.movieName],
      }
    let pr = new Promise((resolve,reject) => {
        client.query(query5,function(err,res){
            if(err)
            {
             console.log(err)   
             reject(err)
            }
            else
            {
              //console.log(res)  
              resolve(res.rows);  
              
            }
        })
    }).catch((err)=> {
        return err;
    })
    return pr;
}
module.exports = {getTheatersTickets};
//getTheatersTickets(null,client);