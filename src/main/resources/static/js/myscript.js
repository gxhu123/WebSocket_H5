var websocket=null;
$(function() {
    //$("#message").innerHTML="ready"+"<br>";
    //创建WebSocket
    //connectWebSocket();
    websocket = new WebSocket("ws://localhost:8080/websocket_h5/");
    alert(websocket.state());
});

//强制关闭浏览器  调用websocket.close（）,进行正常关闭
window.onunload = function() {
    //关闭连接
    closeWebSocket();
};

//建立WebSocket连接
function connectWebSocket(){
    //setMessageInnerHTML("开始...");

    //建立webSocket连接
    websocket = new WebSocket("ws://localhost:8080/");
    alert(websocket.state());

    //打开webSokcet连接时，回调该函数
    websocket.onopen = function () {
        setMessageInnerHTML("onpen");
    };

    //关闭webSocket连接时，回调该函数
    websocket.onclose = function () {
        //关闭连接
        //setMessageInnerHTML("onclose");
    };

    //接收信息
    websocket.onmessage = function (msg) {
        //setMessageInnerHTML(msg.data);
    };
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML){
    //$("#message").innerHTML+= innerHTML + '<br/>';
};

//发送消息
function send(){
    var postValue={};
    postValue.id=userID;
    postValue.message=$("#text").val();
    websocket.send(JSON.stringify(postValue));
};
//关闭连接
function closeWebSocket(){
    if(websocket != null) {
        websocket.close();
    }
}