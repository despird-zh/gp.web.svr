<%@ page language="java" errorPage="/WEB-INF/view/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../../common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh_CN">
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
						 <jsp:param name="curr_module" value=""/> 
						 <jsp:param name="curr_page" value="sys-option"/> 
				  </jsp:include> 
				</section>
			<!-- /.sidebar -->
			</aside>
			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
				  <h1>
					系统配置
					<small>管理、维护系统的各项设置</small>
				  </h1>
				</section>
				<!-- Main content -->
				<section class="content">
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
						  <li class="active"><a data-toggle="tab" href="#tab_1">基本设置</a></li>               
						</ul>
						<div class="tab-content">
							<div id="tab_1" class="tab-pane active">								
								<div class="col-md-12">		
									<form class="form-inline" style="margin-bottom: 10px;">
										<label>Group : </label> 								
										<select gpid="basic-setting-group" class="form-control select2" id="type_input" style="min-width: 170px;">								
										</select> &nbsp;
										<a gpid="basic-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
										<a gpid="basic-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
									</form>
									<hr class="m-t-sm m-b-sm">							
								</div>												   
								<div class="box-body">
									<table gpid="basic-table" class="table table-bordered table-condensed table-hover">
										<thead>
											<tr>
												<th >Group key</th>
												<th >Setting key</th>
												<th style="padding: 5px;">Value</th>
												<th>Description</th>
												<th >Act</th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
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
    <script src="${path_plugins}/select2/select2.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
	
	<script src="${path_script}/ga/sys-option.js"></script>
  </body>
</html>
