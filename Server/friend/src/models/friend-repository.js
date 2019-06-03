'use strict';
const dbClient = require('../utils/db-connection');

async function getFriendList(db, uid){
    let result;

    try{
        result = await dbClient.query(db, 'SELECT uid AS id, name, image_name FROM users WHERE uid IN (SELECT fid FROM friends WHERE uid = ?)',
            [uid]);

        return JSON.parse(JSON.stringify(result));

    }catch(err){
        console.log('getFriendList error=', err);
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

module.exports = {
    getFriendList,
    updateLoginInfo,
}
