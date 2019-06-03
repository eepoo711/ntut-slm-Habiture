'use strict';

async function getFriendList(db, uid){
    let result;

    try{
        result = await db.collection('users').aggregate([
            { '$match': { 'uid': uid }},
            { '$lookup': { 'from': 'users', 'localField': 'friends', 'foreignField': 'uid', 'as': 'friends' }}
        ]).toArray();

        return result[0].friends;

    }catch(err){
        console.log('getFriendList error=', err);
        throw(err);
    }
}

module.exports = {
    getFriendList,
}
