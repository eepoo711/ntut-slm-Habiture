'use strict';
const habitureRepository = require('../models/habiture-repository');
const resMessage = require('../utils/response-message-definition');
const config = require('../../config/config');

async function getList(ctx, next) {
    let userId = ctx.query.uid;
    let habitures;

    try {
        if (userId === null) {
            return ctx.body = {
                Code: 400,
                Message: resMessage.getMessage(400)
            };
        }
        
        await habitureRepository.updateLastLoginTime(ctx.db, userId);
        habitures = await habitureRepository.getHabitures(ctx.db, userId);

        ctx.body = {
            home: habitures,
            // Code: 200,
            // Message: resMessage.getMessage(200),
        };
    } catch (error) {
        console.log('getHabitures error=', error);
        // if (error.message === 'USER_NOT_FOUND') {
            // return ctx.body = {
                // Code: 404,
                // Message: resMessage.getMessage(404)
            // }
        // }
        ctx.status = 500;
    }
}

async function get(ctx, next) {
    let pId = ctx.params.pid;
    let habiture;

    try {
        if (pId === null) {
            return ctx.body = {
                Code: 400,
                Message: resMessage.getMessage(400)
            };
        }

        habiture = await habitureRepository.getHabitureInfo(ctx.db, pId);
        habiture.founder = await habitureRepository.getHabitureFounders(ctx.db, pId);

        habiture.founder.forEach((founder) => {
            founder.url = config.profileUrl + founder.image_name;
        });

        ctx.body = {
            posts_page: habiture,
        };
    } catch (error) {
        console.log('getHabiture error=', error);
        ctx.status = 500;
    }
}

async function add(ctx, next) {
    let uId = ctx.request.body.uid;
    let frequency = ctx.request.body.frequency;
    let swear = ctx.request.body.swear;
    let punishment = ctx.request.body.punishment;
    let goal = ctx.request.body.goal;
    let doItTime = ctx.request.body.do_it_time;
    let habiture;

    try {
        if (frequency === null || swear === null || punishment === null || goal === null || doItTime === null) {
            return ctx.body = {
                Code: 400,
                Message: resMessage.getMessage(400)
            };
        }

        habiture = await habitureRepository.addHabiture(ctx.db, uId, frequency, swear, punishment, goal, doItTime);

        ctx.body = {};
    } catch (error) {
        console.log('addHabiture error=', error);
        ctx.status = 500;
    }
}

module.exports = {
    getList,
    get,
    add,
}
