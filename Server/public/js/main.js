const socket = io();

socket.on('chat-message', (message) => {
    console.log(message);
});  