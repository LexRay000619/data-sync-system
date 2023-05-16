import {loadExcelData,editBtn} from "./app.js"
let socket;
let ipAddr = window.location.hostname;
let id = 0;
axios.get(`http://${ipAddr}:8080/websocket/getId`).then(result => {
    id = result.data;
    createWebSocketConnection();
});

function createWebSocketConnection() {
    if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
        socket = new WebSocket(`ws://${ipAddr}:8080/websocket/${id}`);
        socket.onopen = function () {
            console.log("WebSocket连接建立成功,id为:" + id);
            socket.send("新的连接已建立,id为:" + id);
        };
        socket.onmessage = function (msg) {
            console.log(msg.data);
            // 若当前页面已加载excel文件，则可以实时接收同步
            if(!editBtn.disabled){
                loadExcelData();
            }
        };
        socket.onclose = function () {
            console.log("WebSocket连接已关闭");
        };
        socket.onerror = function () {
            alert("WebSocket连接发生了错误");
        };
    }
}

document.getElementById('send-btn').onclick=()=>{
    let inputText = document.getElementById("textInput").value;
    socket.send(inputText);
}

