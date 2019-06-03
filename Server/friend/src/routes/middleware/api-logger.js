'use strict';
async function apiLog(ctx, next){
    console.log('The request body = ', ctx.request.body);
    await next();
    console.log('The response body = ', ctx.response.body);
}

module.exports = {
    apiLog,
}
