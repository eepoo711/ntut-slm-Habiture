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

async function addHabiture(db, uId, frequency, swear, punishment, goal, doItTime) {
	let result;
	let pId;

	try {
		result = await dbClient.query(db, 'INSERT INTO posts (frequency, swear ,punishment, goal,do_it_time, post_date,update_date) VALUES (?, ?, ?, ?, ?, now(),now());',
			[frequency, swear, punishment, goal, doItTime]);

		if(result.affectedRows === 1){
			result = await dbClient.query(db, 'SELECT LAST_INSERT_ID();');
			pId = result[0]['LAST_INSERT_ID()'];

			result = await dbClient.query(db, 'INSERT INTO groups(pid, uid, founder) VALUES(?, ?, 1)',
				[pId, uId]);
			result = await dbClient.query(db, 'INSERT INTO posts_owner (pid, uid, update_time, goal, weeks_frequency) VALUES (?, ?, now(), ?, ?)',
				[pId, uId, goal, frequency]);
		}

		return (result.affectedRows);
	} catch (err) {
		console.log('addHabitureFounder error=', err);
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
	addHabiture,
    updateLastLoginTime,
}
