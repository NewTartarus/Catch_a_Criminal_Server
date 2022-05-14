const moment = require("moment");


function createMessage(name, message)
{
    return {
        userName: name,
        time: moment().format('HH:mm'),
        text: message
    };
}


module.exports = createMessage;