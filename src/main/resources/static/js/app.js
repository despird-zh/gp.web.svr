var stompClient = null;
var sockJS = null;
function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}

var wsendpoint = "gpcenter";
var wsapp = "gpapp";
function connect() {
	console.log('connecting... ');
	//sockJS = new SockJS('http://localhost:8080/hello');
    var login = document.getElementById('user').value;
    var pass = document.getElementById('pass').value;
    stompClient = Stomp.client('ws://localhost:8080/'+wsendpoint+'?login='+login+'&passcode='+pass);
    //stompClient.debug = null;
    stompClient.connect({'passcode':pass,'login':login}, function(frame) {
    	
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
        
        stompClient.subscribe('/user/queue/notifications', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
        
        stompClient.subscribe('/user/queue/chat', function(greeting){
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
	console.log('closing... ');
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = document.getElementById('name').value;
    stompClient.send('/'+wsapp+'/hello', {}, JSON.stringify({ 'name': name }));
}

function showGreeting(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}

$(document).ready(function(){
	
	$('#login').bind('click', function(){
		var data = {
			principal: $('#user').val(),
			credential:$('#pass').val(),
			audience:'sync001'
		}
		$.ajax({
			url: 'gpapi/authenticate',
			data: JSON.stringify(data),
			type: 'post',
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			success:function(data) {  
				$('#api-result').html(JSON.stringify(data));
			}
		});
	});
	
	$('#test').bind('click', function(){
		stompClient.send("/"+wsapp+"/test", {},JSON.stringify({ 'tkey': 'hello blabal...' }));
	});
	
	$('#spittle').bind('click', function(){
		stompClient.send("/"+wsapp+"/spittle", {},JSON.stringify({ 'name': 'spittle message...' }));
	});
	
	$('#sayhi').bind('click', function(){
		stompClient.send("/"+wsapp+"/test.sayhi", {},JSON.stringify({ 'name': 'sayhi message...' }));
	});
	$('#sayhi2').bind('click', function(){
		stompClient.send("/"+wsapp+"/test.sayhi.dev1", {},JSON.stringify({ 'name': 'sayhi dev1 message...' }));
	});
	
	$('#to-user').bind('click', function(){
		
		stompClient.send("/"+wsapp+"/chat", {},JSON.stringify({ 'target': $('#target-user').val(), 'message': $('#message').val()}));
	});
	
	$('#all-users').bind('click', function(){
		$.ajax({
			url: 'gpapi/all-users',
			type: 'post',
			contentType: "application/json; charset=utf-8",
			dataType: 'json',
			success:function(data) {  
				$('#users-span').html(JSON.stringify(data));
			}
		});
	});
}) 