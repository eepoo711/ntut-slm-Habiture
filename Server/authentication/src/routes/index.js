'use strict';
const Route = require('koa-router');
const route = new Route();
const apiLogger = require('./middleware/api-logger').apiLog;
const users = require('./user');

route.get('/v1/users/login', users.login);// don't write log for protect pwd
route.use(apiLogger);
route.get('/v1/tools/users/sync', users.sync);

module.exports = route;
