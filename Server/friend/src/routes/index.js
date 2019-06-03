'use strict';
const Route = require('koa-router');
const route = new Route();
const apiLogger = require('./middleware/api-logger').apiLog;
const friends = require('./friend');

route.use(apiLogger);
route.get('/v1/friends', friends.getFriendList);


module.exports = route;
