<%@ page language="java" errorPage="/WEB-INF/view/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %> 
<!DOCTYPE html>
<html lang="zh_CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Log in</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${path_bootstrap}/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="${path_plugins}/font-awesome/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="${path_plugins}/ionicons/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${path_css}/AdminLTE.css">
    <!-- uniform -->
    <link rel="stylesheet" href="${path_plugins}/uniform/themes/default/css/uniform.default.min.css" />

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="hold-transition login-page">
    <div class="login-box">
      <div class="login-logo">
        <a href="../../index2.html"><b>Groupress</b> ECM</a>
      </div><!-- /.login-logo -->
      <div class="login-box-body">
        <p class="login-box-msg">Sign in to start your session</p>
        <form action="authenticate.do" method="post">
          <div class="form-group has-feedback">
            <input gpid="username" type="text" value="admin" class="form-control" placeholder="Email">
            <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
          </div>
          <div class="form-group has-feedback">
            <input gpid="password" type="password" value="1" class="form-control" placeholder="Password">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
          </div>
          <div class="row">
            <div class="col-xs-8">
                <label class="checkbox-inline">
                  <input type="checkbox"> Remember Me
                </label>
            </div><!-- /.col -->
            <div class="col-xs-4">
              <a href="javascript:;" gpid="sign-in" class="btn btn-primary btn-block btn-flat">Sign In</a>
            </div><!-- /.col -->
          </div>
        </form>

        <a href="#">I forgot my password</a><br>
        <a href="register.html" class="text-center">Register a new membership</a>

      </div><!-- /.login-box-body -->
    </div><!-- /.login-box -->

    <!-- jQuery 2.1.4 -->
    <script src="${path_plugins}/jQuery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.5 -->
    <script src="${path_bootstrap}/js/bootstrap.min.js"></script>
    <!-- iCheck -->
    <script src="${path_plugins}/uniform/jquery.uniform.min.js" type="text/javascript"></script>
    <script>
      $(function () {
        $('input[type=checkbox]').uniform();
		
		$('a[gpid=sign-in]').bind('click', function(){
			
			var username = $('input[gpid=username]').val();
			var password = $('input[gpid=password]').val();
			var $msg_box = $('p.login-box-msg');
			
			$.ajax({
				contentType: "application/json", 
				url: "authenticate.do", 
				data: {"account" : username, "password": password},      
				dataType: 'json', 				
				success: function(result) { 
					console.log(result);
					if(result.state == 'success'){						
						window.location.href = '../main/mainpage.do';
					}else{
						$msg_box.html(result.message);						
					}
				}
            });
		});
		
      });
    </script>
  </body>
</html>
