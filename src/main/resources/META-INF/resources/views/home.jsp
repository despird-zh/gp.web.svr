<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="${path_bmd}/dist/css/bootstrap-material-design.min.css">

<title>Spring Boot</title>
</head>
<body>
  <h1>Spring Boot - MVC wgeb application home example</h1>
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
  <script src="${path_vendor}/jquery-slim.min.js" type="text/javascript"></script>
  <script src="${path_vendor}/popper.min.js" type="text/javascript"></script>
  <script src="${path_bmd}/dist/js/bootstrap-material-design.js" type="text/javascript"></script>
</body>
</html>