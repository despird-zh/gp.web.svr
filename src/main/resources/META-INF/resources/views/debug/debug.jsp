<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>AdminLTE 2 | Dashboard</title>
	<!-- Tell the browser to be responsive to screen width -->
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<%@include file="../common/include.jsp" %>
	<link rel="stylesheet" href="${path_plugins}/datatables/css/dataTables.bootstrap.css" />
	<title>Spring Boot</title>
</head>
<body>
  <h1>Spring Boot - MVC web application debug page</h1>
  <hr>

  <div class="form">
  	<div style="margin-bottom:5px">
		<table>
			<tr>
				<th> user</th> <td> <input id="user" value="dev1"/></td>
				<th> pass</th> <td> <input id="pass" value="1"/></td>
				<th> <button id="login">Login</button></th>
			</tr>
		</table>
		<div id="api-result">
		</div>
	</div>
	<div style="margin-bottom:5px">
	   	<button id="sync-push"> test sync push</button>
	   	<div id="push-result">
		</div>
   	</div>
  </div>
  <script src="jquery.min.js"></script>
  <script>
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
					$('#api-result').html(data.data);
				}
			});
		});
	  $('#sync-push').bind('click', function(){
		  $.ajax({
				url: 'gpapi/sync-debug',
				data: JSON.stringify({xxx:'xx'}),
				headers:{
					'Authorization': $('#api-result').html()
				},
				type: 'post',
				contentType: "application/json; charset=utf-8",
				dataType: 'json',
				success:function(data) {  
					$('#push-result').html(JSON.stringify(data));
				}
			});
	  });
  });
  </script>
</body>
</html>