<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Hello WebSocket</title>
    </head>
    <body onload="disconnect()">
        <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable
            Javascript and reload this page!</h2></noscript>
        <div>
        		<div style="margin-bottom:5px">
        			<table>
        				<tr>
        					<th> user</th> <td> <input id="user" value="dev1"/></td>
        					<th> pass</th> <td> <input id="pass" value="1"/></td>
        					<th> <button id="login">Login</button> <a href="/"> Home</a></th>
        				</tr>
        			</table>
        			<div id="api-result">
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="test">send</button> <span>Test json string to /app/test and forward to /topic/greetings</span>
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="spittle">send</button> <span>Test spittle to /app/spittle and forward to  @SendToUser("/queue/notifications")</span>
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="sayhi">send</button> <span>Test sayhi , /app/test.sayhi and forward to /topic/greetings</span>
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="sayhi2">send</button> <span>Test sayhi with parameter, /app/test.sayhi.dev1 and forward to /topic/greetings</span>
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="all-users">all user</button> <span id="users-span"></span>
        			</div>
        		</div>
        		<div style="margin-bottom:5px">
        			<div>
        				<button id="to-user">to user</button> <label>user</label> <input type="text" id="target-user" /> / <label>message</label><input type="text" value="chat message" id="message" />
        			</div>
        		</div>
            <div>
            		<button id="connect" onclick="connect();">Connect</button>
                <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
            </div>
            <div id="conversationDiv">
                <label>What is your name?</label><input type="text" id="name" />
                <button id="sendName" onclick="sendName();">Send</button>
                <p id="response"></p>
            </div>
        </div>
        <script src="jquery.min.js"></script>
        <script src="stomp.js"></script>
        <script src="js/app.js"></script>
    </body>
</html>