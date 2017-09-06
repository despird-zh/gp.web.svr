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
  <h1>Spring Boot - MVC web application hello page</h1>
  <hr>

  <div class="form">
    <form action="hello" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Congratulation! </td>
          <td>U are in the authenticated page now!!!</td>
        </tr>
        <tr>
        		<td>Jump to:</td>
        		<td><a href="/logout">Logout Page</a></td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>