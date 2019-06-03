'use strict';
const compress = require('koa-compress');
const logger = require('koa-logger');
const serve = require('koa-static');
const bodyParser = require('koa-bodyparser');
const koa = require('koa');
const cors = require('koa2-cors');
const app = new koa();
const rootRoute = require('./src/routes');
// const node_env = process.env.NODE_ENV ? process.env.NODE_ENV : 'development';
const config = require('./config/config');
const dbConnection = require('./src/utils/db-connection');
const nosqlDbConnection = require('./src/utils/nosql-db-connection');

let serverPort = config.server.port;
let db;
let nosqlDb;

initDb();

app.use(cors({
    allowMethods: ['GET', 'POST', 'PUT', 'DELETE']
}));
// app.use(serve('./public'));

app.use(logger());
app.use(bodyParser());

app.use(rootRoute.routes());
app.use(rootRoute.allowedMethods());
app.use(compress());

async function initDb(){
    db = await dbConnection.create(config.db).catch((err) => {
        console.log('Connect to db fail =', err);
        process.exit(-1);
    });
    app.context.db = db;

    nosqlDb = await nosqlDbConnection.create(config.nosqlDb).catch((err) => {
        console.log('Connect to nosqldb fail =', err);
        process.exit(-1);
    });
    app.context.nosqlDb = nosqlDb;
}

module.exports = app;
