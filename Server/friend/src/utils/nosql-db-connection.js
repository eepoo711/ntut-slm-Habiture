'use strict';
const MongoClient = require('mongodb').MongoClient;

//force set pg timezone= UTC
process.env.TZ = 0;

async function create(dbInfo){
    let client;
    let db;

    try{
        client = new MongoClient(dbInfo.host);
        await client.connect();

        db = client.db(dbInfo.database);
        console.log('Connect to nosql db OK');
        return db;
    } catch(err){
        console.log('Connect to nosql fail =', err);
        throw (err);
    }
}

module.exports = {
    create,
}
