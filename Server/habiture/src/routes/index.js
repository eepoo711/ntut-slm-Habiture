'use strict';
const Route = require('koa-router');
const route = new Route();
const apiLogger = require('./middleware/api-logger').apiLog;
const habitures = require('./habiture');

route.use(apiLogger);

route.get('/v1/habitures/', habitures.getList);
route.post('/v1/habitures/', habitures.add);
route.get('/v1/habitures/:pid', habitures.get);

module.exports = route;
