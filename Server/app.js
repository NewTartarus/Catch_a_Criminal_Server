const crypto   = require('crypto');
const express  = require('express');
const fs       = require('fs');
const http     = require('http');
const path     = require('path');
const socketio = require('socket.io');

const {addUser, getUser, removeUser, getRoomUsers} = require('./utils/users.js');
const createMessage = require('./utils/messages.js');

const app       = express();
const server    = http.createServer(app);
const io        = socketio(server);
const sha256sum = crypto.createHash('sha256');
const CONFIG    = readJSONFile(path.join(__dirname,'server.properties'));

// hash the server password
if(CONFIG.password !== '')
{
    CONFIG.password = sha256sum.update(Buffer.from(CONFIG.password, 'utf16le').toString()).digest('hex');
}

app.use(express.static(path.join(__dirname, 'public')));

//run when client connects
io.on('connection', (socket) => {
    socket.on('login', ({username,password}) => {
        if((CONFIG.password === '') || (password === CONFIG.password))
        {
            const user = addUser(socket.id, username, 'lobby');
            socket.join(user.room);

            socket.emit('login-success', `${CONFIG.name}`);
            console.log(`${user.name} connected`);
            if(user.name !== 'Admin' || user.name !== 'admin')
            {
                socket.broadcast.to(user.room).emit('message', createMessage('Server', `${user.name} joined`));
            }
        }
        else
        {
            socket.emit('error', `wrong_password`);
        }
    });
    socket.on('message', (message) => {
        const user = getUser(socket.id);
        const msg = createMessage(user.name, message);
        console.log(`${user.name}: ${message}`);
        io.to(user.room).emit('message', msg);
    });

    // runs when client disconnects
    socket.on('disconnect', () => {
        const user = removeUser(socket.id);
        if(user)
        {
            console.log(`${user.name} disconnected`);
            io.to(user.room).emit('message', createMessage('Server', `${user.name} has disconnected.`));
        }
    });
});

server.listen(CONFIG.port, () => console.log(`server running on port ${CONFIG.port}`));

function readJSONFile(path)
{
    let rawdata = fs.readFileSync(path);
    return JSON.parse(rawdata);
}