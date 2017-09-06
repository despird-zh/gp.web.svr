<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!-- Static content -->
<link rel="stylesheet" href="/css/index.css">
<script type="text/javascript" src="/js/index.js"></script>

<title>Spring Boot</title>
</head>
<body>
  <h1>Spring Boot - MVC web application home example</h1>
  <hr>

  <div class="form">
  	<span>Welcome, ${user} !</span>
    <form action="hello" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Enter Your name</td>
          <td><input id="name" name="name"></td>
          <td><input type="submit" value="Submit"></td>
        </tr>
        <tr>
        		<td>Jump to:</td>
        		<td><a href="/login">Login Page</a></td>
        </tr>
         <tr>
        		<td>Jump to:</td>
        		<td><a href="/hello">Hello Page</a></td>
        </tr>
        <tr>
        		<td>Jump to:</td>
        		<td><a href="/wstest">WSTest Page</a></td>
        </tr>
      </table>
    </form>
  </div>

</body>
</html>