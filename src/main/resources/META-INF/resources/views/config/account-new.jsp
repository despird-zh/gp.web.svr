<%@ page language="java" errorPage="/WEB-INF/view/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../../common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en_US">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>AdminLTE 2 | Dashboard</title>
		<!-- Tell the browser to be responsive to screen width -->
		<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
		<%@include file="../common/include.jsp" %>
		<link rel="stylesheet" href="${path_plugins}/datatables/css/dataTables.bootstrap.css" />
	</head>
	<body class="hold-transition skin-blue sidebar-mini fixed">
		<div class="wrapper">
			<header class="main-header">
			<%@include file="../common/header.jsp" %>
			</header>
			<!-- Left side column. contains the logo and sidebar -->
			<aside class="main-sidebar">
				<!-- sidebar: style can be found in sidebar.less -->
				<section class="sidebar">
				  <jsp:include page="../common/sidebar.jsp" flush="true">     
						 <jsp:param name="curr_module" value="security"/> 
						 <jsp:param name="curr_page" value="new"/> 
				  </jsp:include> 
				</section>
				<!-- /.sidebar -->
			</aside>

			  <!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
					<h1>
					新建账户
					<small>新建系统内部账户信息</small>
					</h1>
				</section>
				<!-- Main content -->
				<section class="content">
					  <!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li class="active">
								<a gpid="new-tab" data-toggle="tab" href="#tab_4">新建用户</a>
							</li>   												
						</ul>
						<div class="tab-content">							
							<div id="tab_4" class="tab-pane active">
								<div class="row m-l-none m-r-none">
									<div class="col-md-6">
										<div class="col-md-12 no-padding">
										  <form class="form-inline">
											<label class="p-t-xxs  m-r-sm"> Basic Information </label>						
										  </form>
										  <hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-account">account</label>
													<div class="col-sm-4">
													<input type="text" placeholder="account" value="" id="new-account" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-name">name</label>
													<div class="col-sm-4">
													<input type="text" placeholder="name" value="" id="new-name" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-type">type</label>
													<div class="col-sm-4">
														<select class="form-control select2" id="new-type">
														<option value="LDAP"> LDAP</option>
														<option value="INLINE">IN-LINE</option>
														<option value="EXTERN">EXTERN</option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-password">password</label>
													<div class="col-sm-4">
													<input type="password" placeholder="set password" value="" id="new-password" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-password-confirm">confirm</label>
													<div class="col-sm-4">
													<input type="password" placeholder="confirm password" value="" id="new-password-confirm" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-email">email</label>
													<div class="col-sm-4">
													<input type="text" placeholder="email" id="new-email" value="" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-mobile">mobile</label>
													<div class="col-sm-4">
													<input type="text" placeholder="mobile" id="new-mobile" value="" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-phone">phone</label>
													<div class="col-sm-4">
													<input type="text" placeholder="phone" id="new-phone" value="" class="form-control">
													</div>
												</div>
											</form>
										</div>
									</div><!-- /.col-md-6 -->
									<div class="col-md-6">
										<div class="col-md-12 no-padding">
										  <form class="form-inline">
											<label class="p-t-xxs  m-r-sm"> User Store Setting </label>						
										  </form>
										  <hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-storage-sel">Storage</label>
													<div class="col-sm-4">
														<select class="form-control select2" id="new-storage-sel" style="width : 150px;">
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-pub-cap">public cabinet</label>
													<div class="col-sm-4">
														<div class="input-group">
															<input type="text" id="new-pub-cap" value="${item.pubcapacity}" class="form-control">
															<span class="input-group-addon">G</span>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-pri-cap">private cabinet</label>
													<div class="col-sm-4">
														<div class="input-group">
															<input type="text" id="new-pri-cap" value="${item.pricapacity}" class="form-control">
															<span class="input-group-addon">G</span>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-md-3 control-label">International</label>
													<div class="col-sm-7 ">								
														<p gpid="new-store-setting" class = "form-control-static">International support Setting</p>
													</div>
												</div>
												<hr class="m-t-sm m-b-sm">
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-language">language</label>
													<div class="col-sm-4">
														<select class="form-control select2" id="new-language">
															<option value="en_US">English</option>
															<option value="zh_CN">Chinese</option>
															<option value="fr_FR">France</option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="new-timezone">time zone</label>
													<div class="col-sm-4">
														<select class="form-control select2" id="new-timezone">
															<option value="GMT+08:00">GMT+08:00</option>
															<option value="GMT+09:00">GMT+09:00</option>
															<option value="GMT+10:00">GMT+10:00</option>
														</select>
													</div>
												</div>
											</form><!-- /.form-horizontal -->
										</div>
									</div><!-- /.col-md-6 -->
								</div><!-- /.row -->
								<div class="box-footer">
									<button type="button" gpid="new-save-btn" class="btn btn-warning pull-right">Create</button>
								</div><!-- /.box-footer -->
							</div><!-- /.tab-pane -->
						</div><!-- /.tab-content -->						
					</div><!-- nav-tabs-custom -->
				</section><!-- /.content -->
			</div><!-- /.content-wrapper -->
			<footer class="main-footer">
				<div class="pull-right hidden-xs">
				  <b>Version</b> 2.3.0
				</div>
				<strong>Copyright &copy; 2014-2015 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights reserved.
			</footer>
		</div><!-- ./wrapper -->
    <!-- FastClick -->
    <script src="${path_plugins}/fastclick/fastclick.min.js"></script>
    <!-- SlimScroll 1.3.0 -->
    <script src="${path_plugins}/slimScroll/jquery.slimscroll.min.js"></script>
    <!-- mustache -->
    <script src="${path_plugins}/mustache/mustache.min.js"></script>
    <script src="${path_plugins}/datatables/js/jquery.dataTables.js" type="text/javascript"></script>
    <script src="${path_plugins}/datatables/js/dataTables.bootstrap.js" type="text/javascript"></script>
    <script src="${path_plugins}/uniform/jquery.uniform.min.js" type="text/javascript"></script>
    <!-- Select2 -->
    <script src="${path_plugins}/select2/select2.full.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
	<script src="${path_script}/ga/account-new.js"></script>
  </body>
</html>
