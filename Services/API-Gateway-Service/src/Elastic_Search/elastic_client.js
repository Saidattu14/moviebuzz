const { Client,errors } = require('@elastic/elasticsearch');
const client = new Client({ 
        node: 'http://localhost:9200',
        auth: {
                username: 'elastic',
                password: 'changeme'
        }
});


const check = async() => {
        let pr = new Promise(async(resolve,reject) => {
                await client.search({
                   index: '_all'
                }).then(function(resp) {
                        console.log("ElasticSearch Connected")
                  }).catch((err) => {
                   console.log("ElasticSearch Not Connected")
                });
        });
        return pr;
}
check();
module.exports = client;