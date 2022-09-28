const fs = require('fs')
var readerStream = fs.createReadStream('../JsonFiles/reviews.csv');
readerStream.setEncoding('UTF8');
var dt;
readerStream.on('data', function(chunk) {
   try {
      dt = dt + chunk;
   } catch (error) { 
   }
   
 });
 
readerStream.on('end',function() {
      let list = dt.split('\n');
      let count = 0;
      let reviews = []
      let positive_reviews = []
      let negative_reviews = []
      for(let i=0; i<list.length;i++)
      {
         if(list[i].length <= 250)
         {
            reviews.push(list[i])
            count++;
         }
      }
      for(let i =0; i<reviews.length; i++)
      {
         let review_length = reviews[i].length;
         let sentiment = reviews[i].slice(review_length-8,review_length);
         
         if(sentiment == "positive")
         {
            positive_reviews.push(reviews[i].slice(0,review_length-9));
         }
         else
         {
           negative_reviews.push(reviews[i].slice(0,review_length-9));
         }
         
      }
      let obj = {
         "positive_reviews" : positive_reviews
     }
     let obj1 = {
      "negative_reviews" : negative_reviews
  }
     let json = JSON.stringify(obj)
     fs.writeFile('postiveReviews.json',json, 'utf-8',(err) => {
         if(err)
         {
           console.log(err)
         }
     })
     let json1 = JSON.stringify(obj1)
     fs.writeFile('negativeReviews.json',json1, 'utf-8',(err) => {
         if(err)
         {
           console.log(err)
         }
     })      
 });
 
 readerStream.on('error', function(err) {
    console.log(err.stack);
 });
