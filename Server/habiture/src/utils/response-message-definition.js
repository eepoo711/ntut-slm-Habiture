'use strict';
const resMessage = {
    200: 'Successful operation',
    400: 'Bad request',
    401: 'Access token is missing or invalid',
    403: 'The resource is not available for various reasons',
    404: 'The requested resource could not be found',
};

function getMessage(code){
    return resMessage[code];
}

module.exports = {
    getMessage,
}
