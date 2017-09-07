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
				 <jsp:param name="curr_page" value="basic"/> 
			</jsp:include> 
		</section>
		<!-- /.sidebar -->
		</aside>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<section class="content-header">
			  <h1>
				基本信息
				<small>基本系统信息</small>
			  </h1>
			</section>
			<!-- Main content -->
			<section class="content">
				<div class="row"> 
					<div class="col-md-12"> 
						<!-- Custom Tabs -->
						<div class="nav-tabs-custom">
							<ul class="nav nav-tabs">
								<li class="active"><a data-toggle="tab" href="#tab_0">实体信息</a></li>
								<li><a data-toggle="tab" href="#tab_1">外部实体</a></li>
								<li><a data-toggle="tab" href="#tab_2">添加外部实体</a></li>                                   
							</ul>
							<div class="tab-content">
								<div id="tab_0" class="tab-pane active">
									<form class="form-horizontal">
										<div class="box-body p-none">
											<div class="form-group">
												<label class="col-sm-2 control-label" for="source-name">Instance Name</label>
												<div class="col-sm-4">
													<input type="text" placeholder="instance name" id="source-name" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="global-id">Global Id</label>
												<div class="col-sm-3">
													<input type="text" placeholder="Global Id" disabled="" id="global-id" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="entity-code">Entity Code</label>
												<div class="col-sm-3">
													<input type="text" placeholder="entity code" disabled="" id="entity-code" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="node-code">Node Code</label>
												<div class="col-sm-3">
													<input type="text" placeholder="node code" disabled="" id="node-code" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="source-abbr">Abbr</label>
												<div class="col-sm-2">
													<input type="text" placeholder="abbr" id="source-abbr" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="short-name">Short Name</label>
												<div class="col-sm-4">
													<input type="text" placeholder="short name" id="short-name" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="short-name">Admininistrator</label>
												<div class="col-sm-2">
													<input type="text" placeholder="administrator" id="sys-admin" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="short-name">Email</label>
												<div class="col-sm-2">
													<input type="text" placeholder="admin@123.com" id="sys-email" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="binary-url">Binary Url</label>
												<div class="col-sm-4">
													<input type="text" placeholder="binary url" id="binary-url" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="service-url">Service Url</label>
												<div class="col-sm-4">
													<input type="text" placeholder="service url" id="service-url" class="form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label" for="description">Description</label>
												<div class="col-sm-4">
													<textarea placeholder="Enter ..." rows="2" id="description" class="form-control"></textarea>
												</div>
											</div>
										</div><!-- /.box-body -->
										<div class="box-footer">
											<a gpid="refresh-source-info" class="btn btn-warning" >Refresh</a>
											<a gpid="save-source-btn" class="btn btn-info pull-right" >Save</a>
										</div><!-- /.box-footer -->				  
									</form>
								</div><!-- /.tab-pane -->
								<div id="tab_1" class="tab-pane">
									
									<div class="col-md-12">		
										<form class="form-inline">
											<label>Entity Name : </label> <input gpid="list_search_ename" type="text" placeholder="entity name" class="form-control input-md"></input> &nbsp;							
											<a gpid="list_search_btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
											<a gpid="list_clear_btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
										</form>
										<hr class="m-t-sm m-b-sm">						
									</div>
									
									<div class="box-body" >
										<table gpid="list_table" class="table table-striped table-bordered table-condensed table-hover table-checkable" >
											<thead>
												<tr>
													<th> Entity </th>
													<th> Node </th>
													<th> Name </th>
													<th> State </th>
													<th> Abbr </th>
													<th> ShortName </th>
													<th> Description </th>
													<th> Oper </th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div><!-- /.tab-pane -->
								<div id="tab_2" class="tab-pane">									
									<div class="col-md-12">
										<form class="form-inline">
											<label>Entity Name : </label> <input gpid="extern_search_ename" type="text" placeholder="entity name" class="form-control input-md"></input> &nbsp;
											<a gpid="extern_search_btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
											<a gpid="extern_clear_btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
										</form>
										<hr class="m-t-sm m-b-sm">	
									</div>									
									<div class="box-body no-padding">
										<div class="col-md-6">
											<div class="box-body no-padding" style="min-height: 350px;">
												<table gpid="extern_list_table" class="table table-bordered table-condensed table-hover">
													<thead>
														<tr>
															<th>#</th>
															<th>Entity</th>
															<th>Node</th>
															<th>Contact</th>
															<th>EntityName</th>                      
															<th>Act</th>  
														</tr>
													</thead>
													<tbody>
													</tbody>
												</table>
											</div><!-- /.box-body -->
										</div>
										<div class="col-md-6">
											<form class="form-horizontal">
												<div class="box-body" style="min-height: 350px;">
													<div class="form-group" >
														<label class="col-sm-3 control-label" for="ext-global-id">Global Id</label>
														<div class="col-sm-4">
															<input type="text" placeholder="Entity Id" disabled="" id="ext-global-id" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-name">Name</label>
														<div class="col-sm-8">
															<input type="text" placeholder="Entity name" id="ext-name" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-entitycode">Entity</label>
														<div class="col-sm-4">
															<input type="text" placeholder="Entity Id" id="ext-entitycode" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-nodecode">Node</label>
														<div class="col-sm-4">
															<input type="text" placeholder="Node Id" id="ext-nodecode" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-abbr">Abbr</label>
														<div class="col-sm-3">
															<input type="text" placeholder="Node Id" id="ext-abbr" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-shortname">Short Name</label>
														<div class="col-sm-5">
															<input type="text" placeholder="Node Id" id="ext-shortname" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-email">Email</label>
														<div class="col-sm-5">
															<input type="text" placeholder="Node Id" id="ext-email" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-admin">Admin</label>
														<div class="col-sm-5">
															<input type="text" placeholder="Administrator" id="ext-admin" value="demoadmin" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-binurl">BinUrl</label>
														<div class="col-sm-9">
															<input type="text" placeholder="Binary Url" id="ext-binurl" value="http://172.0.0.1/binsvc" class="form-control">
														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label" for="ext-svcurl">ServiceUrl</label>
														<div class="col-sm-9">
															<input type="text" placeholder="Service Url" id="ext-svcurl" value="http://172.0.0.1/binsvc" class="form-control">
														</div>
													</div>
												</div><!-- /.box-body -->
												<div class="box-footer">
													<a gpid="extern-save-btn" href="javascript:;" class="btn btn-info pull-right">Save</a>
												</div><!-- /.box-footer -->
											</form>
										</div>
									</div>
								</div><!-- /.tab-pane -->
							</div><!-- /.tab-content -->
						</div><!-- nav-tabs-custom -->
					</div><!-- /.col-md-12 -->
				</div> <!-- /.row -->
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
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
    <!-- GPress basicinfo -->
    <script src="${path_script}/ga/basicinfo.js"></script>
</body>
</html>
