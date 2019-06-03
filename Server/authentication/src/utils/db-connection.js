'use strict';
const mysql = require('mysql');

//force set pg timezone= UTC
process.env.TZ = 0;

function create(dbInfo){
    return new Promise((resolve, reject) => {
        let pool;

        pool = mysql.createPool(dbInfo);
        pool.getConnection((error, dbClient) => {
            if(error){
                return reject(error);
            }

            dbClient.query('SELECT ? as message', ['Hello world!'], (error, results, fields) => {
                dbClient.release();

                if(error){
                    return reject(error);
                } else{
                    console.log('DB connection success');
                    return resolve(pool);
                }
            });
        });
    });
}

function query(db, query, values, logFlag = 1){
    return new Promise((resolve, reject) => {
        let error;

        if(logFlag){
            console.log(`query=${query}\nvalues=${values}\n`);
        }

        db.getConnection((error, dbClient) => {
            if (error) {
                return reject(error);
            }

            dbClient.query(query, values, (error, results, fields) => {
                dbClient.release();

                if (error) {
                    return reject(error);
                }

                return resolve(results);
            });
        });
    });
}

module.exports = {
    create,
    query,
}
