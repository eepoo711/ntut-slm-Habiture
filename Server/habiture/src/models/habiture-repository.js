'use strict';
const dbClient = require('../utils/db-connection');

async function getHabitures(db, userId){
    let result;

    try{
        result = await dbClient.query(db, 'SELECT p.pid AS id, p.swear, p.punishment, po.weeks_frequency-po.weeks_done AS remain_frequency, 7-po.weeks_frequency-po.weeks_pass AS remain_pass,po.notice_enable FROM posts_owner AS po,posts AS p WHERE p.pid = po.pid AND po.uid = ? ORDER BY remain_frequency DESC',
            [userId]);

        return JSON.parse(JSON.stringify(result));
    }catch(err){
        console.log('getHabitures error=', err);
        throw(err);
    }
}

async function getHabitureInfo(db, pId) {
    let result;

    try {
        result = await dbClient.query(db, 'SELECT swear,punishment,goal,frequency,do_it_time,icon,post_date FROM posts WHERE pid = ?',
            [pId]);

        return JSON.parse(JSON.stringify(result[0]));
    } catch (err) {
        console.log('getHabitureInfo error=', err);
        throw(err);
    }
}

async function getHabitureFounders(db, pId) {
    let result;

    try {
        result = await dbClient.query(db, 'SELECT u.uid,u.name,o.goal-o.done AS remain,u.image_name FROM users AS u, posts_owner AS o WHERE u.uid = o.uid AND o.pid = ?',
            [pId]);

        return JSON.parse(JSON.stringify(result));
    } catch (err) {
        console.log('getHabitureFounder error=', err);
        throw(err);
    }
}

async function updateLastLoginTime(db, userId) {
    let result;
    let query;
    let value;

    try {
        query = 'UPDATE users SET last_login_date = now() WHERE uid = ?';
        value = [userId];

        result = await dbClient.query(db, query, value);
        return result.affectedRows;
    } catch (err) {
        console.log('updateLastLoginTime error=', err);
        throw (err);
    }
}

module.exports = {
    getHabitures,
    getHabitureInfo,
    getHabitureFounders,
    updateLastLoginTime,
}
