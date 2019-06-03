'use strict';
const dbClient = require('../utils/db-connection');

async function login(db, account, pwd){
    let result;

    try{
        result = await dbClient.query(db, 'SELECT uid, image_name, name FROM users WHERE account = ? AND password = ?',
            [account, pwd], 0);
        if (result.length <= 0) {
            return null;
        } else{
            return result[0];
        }
    }catch(err){
        console.log('login error=', err);
        throw(err);
    }
}

async function updateLoginInfo(db, account, regId) {
    let result;
    let query;
    let value;

    try {
        if(regId){
            query = 'UPDATE users SET last_login_date = now() WHERE account = ?';
            value = [account];
        }else{
            query = 'UPDATE users SET last_login_date = now(), reg_id = ? WHERE account = ?';
            value = [regId, account];
        }

        result = await dbClient.query(db, query, value);
        return result.affectedRows;
    } catch (err) {
        console.log('updateLoginInfo error=', err);
        throw (err);
    }
}

async function getUserList(db) {
    let result;

    try {
        result = await dbClient.query(db, 'SELECT uid, image_name, name FROM users');
        return JSON.parse(JSON.stringify(result));
    } catch (err) {
        console.log('getUserList error=', err);
        throw (err);
    }
}

async function addUsers(db, users) {
    let result;
// db.members.createIndex( { "user_id": 1 }, { unique: true } )
    try {
        result = await db.collection('users').insertMany(users);
        return (result.insertedCount);
    } catch (err) {
        console.log('addUsers error=', err);
        throw (err);
    }
}

module.exports = {
    login,
    updateLoginInfo,
    getUserList,
    addUsers,
}
