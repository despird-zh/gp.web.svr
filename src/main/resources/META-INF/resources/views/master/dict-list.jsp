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
						 <jsp:param name="curr_module" value="master"/> 
						 <jsp:param name="curr_page" value="dictionary"/> 
				  </jsp:include> 
				</section>
			<!-- /.sidebar -->
			</aside>
			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
				  <h1>
					字典配置
					<small>管理、维护系统的字典设置</small>
				  </h1>
				</section>
				<!-- Main content -->
				<section class="content">
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
						  <li class="active"><a data-toggle="tab" href="#tab_1">字典查询</a></li>               
						</ul>
						<div class="tab-content">
							<div id="tab_1" class="tab-pane active">	
								<div class="row m-l-none m-r-none">		
									<div class="col-md-8">	
										<div class="col-md-12 no-padding">		
											<form class="form-inline">
												<label>Group : </label>
												<select gpid="search-group" class="form-control select2">								
													<option value="png">PNG</option>
													<option value="jpg">JPG</option>
													<option value="gif">GIF</option>
												</select> &nbsp;
												<label>Language : </label>
												<select gpid="search-lang" class="form-control select2">								
													<option value="zh_CN">CN</option>
													<option value="en_US">EN</option>
													<option value="fr_FR">FR</option>
												</select> &nbsp;
												<a gpid="dict-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
												<a gpid="dict-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
											</form>
											<hr class="m-t-sm m-b-sm">							
										</div>
										<div class="col-md-12 no-padding">
											<table gpid="dict-table" class="table table-bordered table-condensed table-hover">
												<thead>
													<tr>
														<th>Group key</th>
														<th>Dict key</th>
														<th>Value</th>
														<th>Language</th>
														<th>Label</th>
														<th>Act</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
										</div>
									</div><!-- /.col-md-8 -->
									<div class="col-md-4">
										<div class="col-md-12 no-padding">											
											<label class="p-t-xxs  m-r-sm"> 字典详细 </label>											
											<a gpid="entry-save-btn" class="btn btn-primary btn-xs" 
												data-placement="top" 
												data-toggle="tooltip" 
												title="Save the dict entry"><i class="fa fa-fw fa-save"></i></a>
											<hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-sm-4 control-label" >Group</label>
													<div class="col-sm-6">
														<p class="form-control-static" gpid="entry-group"></p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Entry Key</label>
													<div class="col-sm-6">
														<input type="hidden" id="entry-id">
														<p class="form-control-static" gpid="entry-key"></p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" for="entry-value">Entry Value</label>
													<div class="col-sm-6">
														<input type="text" placeholder="entry value" value="" id="entry-value" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Language</label>
													<div class="col-sm-6">
														<p class="form-control-static" gpid="entry-language"></p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Label</label>
													<div class="col-sm-6">
														<input type="text" placeholder="entry label" value="" id="entry-label" class="form-control">
													</div>
												</div>
											</form><!-- /.form-horizontal -->
										</div>
									</div>
								</div><!-- /.row -->
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
	
	<script src="${path_script}/ga/dict-list.js"></script>
  </body>
</html>
