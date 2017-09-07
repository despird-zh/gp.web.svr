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
						 <jsp:param name="curr_page" value="ext"/> 
				  </jsp:include> 
				</section>
				<!-- /.sidebar -->
			</aside>

			  <!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
					<h1>
					外部账户
					<small>新建系统外部账户信息</small>
					</h1>
				</section>
				<!-- Main content -->
				<section class="content">
					  <!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">					
							<li class="active">
								<a gpid="ext-tab" data-toggle="tab" href="#tab_3">外部用户</a>
							</li>   					
						</ul>
						<div class="tab-content">							
							<div id="tab_3" class="tab-pane active">
								<div class="row m-l-none m-r-none">
									<div class="col-md-6">
										<div class="col-md-12  no-padding">
											<form class="form-inline">
												<label>Find : </label> <input gpid="ext-search-cond" type="text" placeholder="account/mail/mobile" class="form-control input-sm"> &nbsp;
												<a gpid="ext-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
												<a gpid="ext-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
											</form>	
											<hr class="m-t-sm m-b-sm">
										</div>	
										<div class="col-md-12 no-padding">
											<table gpid="ext-list-table" class="table table-bordered table-condensed table-hover">
												<thead>
													<tr>
														<th>Account</th>
														<th>Name</th>
														<th>Mobile</th>
														<th>Email</th>
														<th>Act</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
										</div><!-- /.box-body -->
									</div>
									<div class="col-md-6">
										<div class="col-md-12 no-padding">
										  <form class="form-inline">
											<label class="p-t-xxs  m-r-sm"> Entity Information </label>						
										  </form>
										  <hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">									 
												<div class="form-group">
												  <label class="col-sm-3 control-label">Entity / Node</label>
												  <div class="col-sm-9">
													<p class="form-control-static"><span gpid="ext-entity-p" class="label label-success">EN0001</span> - <span gpid="ext-node-p" class="label label-warning">ND0022</span></p>
													<p gpid="ext-entity-name-p" class="form-control-static">Hard disk store setting</p>
												  </div>
												</div>
												<div class="form-group">
												  <label class="col-sm-3 control-label" >Account</label>
												  <div class="col-sm-9">
													<p gpid="ext-account-p" class="form-control-static">user1</p>
													<input type="hidden" id="ext-global-account" >
												  </div>
												</div>
												<div class="form-group">
												  <label class="col-sm-3 control-label" for="ext-name">Name</label>
												  <div class="col-sm-6">
													<input type="text" placeholder="Account name" id="ext-name" class="form-control">
												  </div>
												</div>
												<div class="form-group">
												  <label class="col-sm-3 control-label" >Mobile</label>
												  <div class="col-sm-9">
													<p gpid="ext-mobile-p" class="form-control-static">1827374646</p>
												  </div>
												</div>
												<div class="form-group">
												  <label class="col-sm-3 control-label" >Email</label>
												  <div class="col-sm-9">
													<p gpid="ext-email-p" class="form-control-static">rtyyy@176.com</p>
												  </div>
												</div>									
											</form>
										</div>
									</div>
								</div><!-- /.row -->
							  <div class="box-footer">
								<button gpid="ext-close-btn" class="btn btn-default" type="button">Close</button>
								<button gpid="ext-save-btn" class="btn btn-warning pull-right" type="button">Create</button>
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
	<script src="${path_script}/ga/account-ext.js"></script>
  </body>
</html>
