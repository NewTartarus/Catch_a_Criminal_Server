const userList = [];

function addUser(userid, username, socketRoom)
{
    const user = {
        id:userid,
        name:username,
        room:socketRoom,
        color:"#ffffff",
        role:0,
        state:0,
        position:0,
        tickets:[
            {type:0,count:0},
            {type:1,count:0},
            {type:2,count:0},
            {type:3,count:0},
            {type:4,count:0}
        ],
        gender:0,
        look:[
            {position:0,id:0,color:"#ffffff"},
            {position:1,id:0,color:"#ffffff"},
            {position:2,id:0,color:"#ffffff"},
            {position:3,id:0,color:"#ffffff"}
        ]
    };
    userList.push(user);
    return user;
}

function getUser(userid)
{
    return userList.find(user => user.id === userid);
}

function removeUser(userid)
{
    const index = userList.findIndex(user => user.id === userid);

    if(index !== -1)
    {
        return userList.splice(index, 1)[0];
    }
}

function getRoomUsers(roomName)
{
    return userList.filter(user => user.room === roomName);
}

function setUserSetting(userid, color, role, state)
{
    const user = getUser(userid);
    user.color = color;
    user.role = role;
    user.state = state;
}

function changeRoom(userid, roomName)
{
    const user = getUser(userid);
    user.room = roomName;
}

module.exports = {
    addUser, 
    getUser, 
    removeUser,
    getRoomUsers
};