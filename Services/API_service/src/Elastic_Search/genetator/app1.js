const { Client } = require('@elastic/elasticsearch');
const client = new Client({ node: 'http://localhost:9200' })

async function run () {
   



  // for(let i =0; i<35; i++)
  // {
   

  //   await client.indices.create({
  //     index : 'game-of-thrones'+ i.toString(),
  //     body : 
  //     {
       
  //       "settings" : {
  //         "number_of_shards" : 2,
  //         "number_of_replicas" : 0,
  //     },
  //     "mappings" : {
        
  //         "properties" : {
  //           "Title" : {"type" : "keyword"},
  //         }
  //     }
  //     }
  //   });

  //   // console.log(await client.indices.shardStores({
  //   //   "index" : 'game-of-thrones'+ i.toString(),
  //   // }))

   

    
  // }
    

    // const result = await client.search({
    //     index: 'game-of-thrones',
    //     "body": {
    //     query: {
    //       match: {
    //         "quote" : 'winter'
    //       }
    //     }
    // }
    //   })
   
    //   console.log(result.body.hits.hits)
    
    const res =  await client.searchShards();
    console.log(res.body.shards)
    let a = await client.cat.shards()
    console.log(a)


  }
  
  run().catch(console.log)