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
						<jsp:param name="curr_module" value="storage" />
						<jsp:param name="curr_page" value="list" /></jsp:include>
				</section>
				<!-- /.sidebar -->
			</aside>
			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
					 <h1>
						存储设置
						<small>查看、维护系统使用的存储信息</small>
					  </h1>
				</section>
				<!-- Main content -->
				<section class="content">
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom no-radius">
						<ul class="nav nav-tabs no-radius">
							<li <c:if test="${viewtab == 'list'}">class="active"</c:if>>
								<a gpid="list-tab" data-toggle="tab" href="#tab_1">存储列表</a>
							</li>
							<li <c:if test="${viewtab == 'modify'}">class="active"</c:if>>	
								<a gpid="edit-tab" data-toggle="tab" href="#tab_2">存储信息</a>
							</li>
						</ul>
						<div class="tab-content no-radius">
							<div id="tab_1" class="tab-pane <c:if test="${viewtab == 'list'}">active</c:if> ">
								<div class="col-md-12">
									<form class="form-inline" style="margin-bottom: 10px;">
										<label>存储 :</label>
										<input gpid="list-search-sname" type="text" placeholder="storage name" class="form-control input-md">&nbsp;
										<label>类型 : </label> 
										<select gpid="list-search-type" class="form-control select2">
											<option value="ALL" selected>All</option>	
											<option value="DISK">Disk Store</option>
											<option value="HDFS">HDFS Store</option>								
										</select>&nbsp;
										<label>状态 : </label> 
										<select gpid="list-search-state" class="form-control select2">		
											<option value="ALL" selected>All</option>	
											<option value="OPEN">Open Store</option>
											<option value="CLOSE">Close Store</option>
											<option value="FULL">Full Store</option>	
										</select>&nbsp;
										<a gpid="list-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
										<a gpid="list-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
									</form>
									<hr class="m-t-sm m-b-sm">
								</div>
								<div class="box-body">
									<table gpid="list-table" class="table table-bordered table-condensed table-hover">
										<thead>
											<tr>
												<th>#</th>
												<th>name</th>
												<th>Type</th>
												<th>Cap.</th>
												<th>Used</th>
												<th>Progress</th>
												<th>State</th>
												<th>Description</th>
												<th>Act</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>
								</div>
							</div>
							<!-- /.tab-pane -->
							<div id="tab_2" class="tab-pane <c:if test="${viewtab=='modify'}">active</c:if> ">
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
													<label class="col-sm-3 control-label" for="edit-storage-name">Storage Name</label>
													<div class="col-sm-7">
														<input type="hidden" id="edit-storage-id">
														<input type="text" placeholder="storage name" id="edit-storage-name" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-storage-type">Storage Type</label>
													<div class="col-sm-5">
														<select class="form-control select2" id="edit-storage-type">
															<option value="DISK" selected>Disk Store</option>
															<option value="HDFS">HDFS Store</option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-capacity">Capacity</label>
													<div class="col-sm-4">
														<div class="input-group">
															<input type="text" placeholder="Capacity(mega bytes)" id="edit-capacity" class="form-control">	<span class="input-group-addon">M</span>
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-used">Used</label>
													<div class="col-sm-4">
														<input type="text" placeholder="Capacity(mega bytes)" disabled="" id="edit-used" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-state">State</label>
													<div class="col-sm-5">
														<select class="form-control select2" id="edit-state">
															<option value="OPEN" selected>Open Store</option>
															<option value="CLOSE">Close Store</option>
															<option value="FULL">Full Store</option>
														</select>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-description">Description</label>
													<div class="col-sm-8">
														<textarea class="form-control" id="edit-description" rows="2" placeholder="Enter ..."></textarea>
													</div>
												</div>											
											</form>
										</div><!-- /.box-body -->
									</div>
									<!-- /.col-md-6 -->
									<div class="col-md-6">
										<div class="col-md-12 no-padding">
										  <form class="form-inline">
											<label class="p-t-xxs  m-r-sm"> Hard disk store setting </label>						
										  </form>
										  <hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">																														
												<div class="form-group">
													<label class="col-sm-3 control-label" for="edit-storepath">Store Path</label>
													<div class="col-sm-7">
														<input type="text" placeholder="Path" id="edit-storepath" class="form-control">
													</div>
												</div>
												<div gpid="hdfs-marker" class="form-group hidden">
													<label class="col-sm-3 control-label" for="edit-hdfs-host">HDFS Host</label>
													<div class="col-sm-4">
														<input type="text" placeholder="Host" id="edit-hdfs-host" class="form-control">
													</div>
												</div>
												<div gpid="hdfs-marker" class="form-group hidden">
													<label class="col-sm-3 control-label" for="edit-hdfs-port">HDFS port</label>
													<div class="col-sm-2">
														<input type="text" placeholder="Port" id="edit-hdfs-port" class="form-control">
													</div>
												</div>										
											</form><!-- /.form-horizontal -->
										</div>
									</div>
									<!-- /.col-md-6 -->
								</div>
								<!-- /.row -->
								<div class="box-footer">
									<button type="button" gpid="edit-save-btn" class="btn btn-info pull-right">Update</button>
								</div>
								<!-- /.box-footer -->
							</div>
							<!-- /.tab-pane -->							
						</div>
						<!-- /.tab-content -->
					</div>
					<!-- nav-tabs-custom -->
				</section>
				<!-- /.content -->
			</div>
			<!-- /.content-wrapper -->
			<footer class="main-footer">
				<div class="pull-right hidden-xs"> <b>Version</b> 2.3.0</div> <strong>Copyright &copy; 2014-2015 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights reserved.</footer>
		</div>
		<!-- ./wrapper -->

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
		<!-- AdminLTE for demo purposes -->
		<script src="${path_script}/ga/storage-list.js"></script>
	</body>

</html>