'use strict';
const friendRepository = require('../models/friend-repository');
const resMessage = require('../utils/response-message-definition');
const config = require('../../config/config');

async function getFriendList(ctx, next){
    let uId = ctx.request.query.uid;
    let friends;

    if(!uId){
        return ctx.body = {
            Code: 400,
            Message: resMessage.getMessage(400)
        };
    }

    try{
        friends = await friendRepository.getFriendList(ctx.db, uId);
        friends.forEach((friend) => {
            friend.url = config.profileUrl + friend.image_name;
            delete friend.image_name;
        });

        ctx.body = {
            // Code: 200,
            // Message: resMessage.getMessage(200),
            friends: friends
        };
    }catch(error){
        console.log('Get non except error ', error);
        ctx.status = 500;
    }
}

module.exports = {
    getFriendList,
}
