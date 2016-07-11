<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>Testing websockets</title>
</head>
<body>
<div>
    <input type="submit" value="Start" onclick="start()" />
</div>
<div id="messages"></div>


<script src="/static/js/reconnecting-websocket.js" ></script>

<script type="text/javascript">
    function cutOffStr(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var windowHref = decodeURI(window.location.search);
        var r = windowHref.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
</script>

<script type="text/javascript">

    var webSocket = new ReconnectingWebSocket('ws://kkw.kk.cn/ws');
	//var webSocket = new WebSocket('ws://localhost:8080/websocket1');

    webSocket.onerror = function(event) {
        onError(event)
    };

    webSocket.onopen = function(event) {
        onOpen(event)
    };

    webSocket.onmessage = function(event) {
        onMessage(event)
    };

    function onMessage(event) {
        document.getElementById('messages').innerHTML
                += '<br />' + event.data;
    }

    function onOpen(event) {
        document.getElementById('messages').innerHTML
                = 'Connection established';
    }

    function onError(event) {
        document.getElementById('messages').innerHTML
                += '<br />error:' + event.data;
    }

    function start() {
        webSocket.send('hello');
        return false;
    }
</script>

</body>
</html>
