<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${userId}-codepile</title>

    <script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
    <style>
    .mgt20{margin-top:20px;}
    .mgl20{margin-left:20px;}
    </style>
</head>
<body>
<input type="hidden" value="${userId}" id="userId"/>
<div>
    <div  class="mgt20 mgl20">当前在线人数：<span id="online"></span></div>
    <textarea id="content" rows="25" cols="150" class="mgt20 mgl20">${content}</textarea>
</div>

<script src="/static/js/reconnecting-websocket.js" ></script>


<script type="text/javascript">

    var webSocket = new ReconnectingWebSocket('ws://${domain}/ws?userId=' + $("#userId").val());

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
        // console.log("receive:" + event.data);
        var msg = JSON.parse(event.data);
        var type = msg["type"];
        if("content" == type){
            var content = msg["content"];
            $("#content").val(content);
        }
        if("online" == type){
            var content = msg["content"];
            $("#online").html(content);
        }
    }

    function onOpen(event) {
        console.log('Connection established');
    }

    function onError(event) {
        if(typeof(event)!="undefined" && typeof(event.data)!="undefined" ){
            console.error('error:' + event.data);
        }
    }


    $('#content').bind('input propertychange', function(){
        var content = $("#content").val()
        var msg = {}
        msg["type"] = "content";
        msg["content"] = content;
        msg["userId"] = $("#userId").val();

        webSocket.send(JSON.stringify(msg));
    });

</script>

</body>
</html>
