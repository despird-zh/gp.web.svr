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
  <h1>Spring Boot - MVC web application login page</h1>
  <hr>

  <div class="form">
    <form action="/authen_form" method="post">
      <table>
        <tr>
          <td>Enter Your name</td>
          <td><input id="username" name="username" value="dev1"></td>
        </tr>
        <tr>
          <td>Enter Your password</td>
          <td><input type="password" id="password" name="password" value="1"></td>
        </tr>
        <tr>
          <td></td>
          <td><input type="submit" value="Submit"></td>
        </tr>
      </table>
    </form>
  </div>

</body>
</html>