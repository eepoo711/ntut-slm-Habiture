'use strict';
const userRepository = require('../models/user-repository');
const resMessage = require('../utils/response-message-definition');
const config = require('../../config/config');

async function login(ctx, next){
    let account = ctx.request.query.account;
    let pwd = ctx.request.query.password;
    let regId = ctx.request.query.regId;
    let userInfo;

    if(!account || !pwd){
        return ctx.body = {
            Code: 400,
            Message: resMessage.getMessage(400)
        };
    }

    try{
        userInfo = await userRepository.login(ctx.db, account, pwd);
        if (userInfo){
            await userRepository.updateLoginInfo(ctx.db, account, regId);
            ctx.body = {
                Code: 200,
                Message: resMessage.getMessage(200),
                id: userInfo.uid,
                url: config.profileUrl + userInfo.image_name,
                name: userInfo.name
            };
        } else{
            ctx.body = {
                Code: 403,
                Message: resMessage.getMessage(403)
            };
        }
    }catch(error){
        console.log('Get non except error ', error);
        ctx.status = 500;
    }
}

async function sync(ctx, next) {
    let users;

    try {
        users = await userRepository.getUserList(ctx.db);

        await userRepository.addUsers(ctx.nosqlDb, users);
        ctx.body = {
            Code: 200,
            Message: resMessage.getMessage(200),
        };
    } catch (error) {
        console.log('Sync-user except error ', error);
        ctx.status = 500;
    }
}

module.exports = {
    login,
    sync,
}
