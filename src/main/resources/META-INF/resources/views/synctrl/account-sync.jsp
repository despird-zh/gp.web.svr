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
	<link rel="stylesheet" href="${path_plugins}/datatables/datatables.min.css" />
	<link rel="stylesheet" href="${path_plugins}/datatables/plugins/bootstrap/datatables.bootstrap.css"/>
	<link rel="stylesheet" href="${path_plugins}/uniform/themes/default/css/uniform.default.min.css" />
	
	<%@include file="../common/include.jsp" %>
	<link rel="stylesheet" href="${path_plugins}/daterangepicker/daterangepicker-bs3.css">
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
				 <jsp:param name="curr_page" value="account-sync"/> 
		  </jsp:include> 
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            同步记录
            <small>查看同步记录、配置同步信息</small>
          </h1>
        </section>
        <!-- Main content -->
        <section class="content">
              <!-- Custom Tabs -->
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                  <li class="active"><a data-toggle="tab" href="#tab_1">同步记录</a></li>
                  <li ><a data-toggle="tab" href="#tab_2">同步设置</a></li>  
                </ul>
                <div class="tab-content">
               <div id="tab_1" class="tab-pane active">
				   <div class="row" style="margin-left:0px; margin-right:0px;">
						<div class="col-md-12">		
							<form class="form-inline" style="margin-bottom: 10px;">
								<label>Range : </label>
								<div class="input-group">
								  <div class="input-group-addon">
									<i class="fa fa-calendar"></i>
								  </div>
								  <input type="text" gpid="search-dt-range" class="form-control pull-right">
								</div><!-- /.input group -->&nbsp;
								<a class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
								<a class="btn btn-default btn-sm"><i class="fa fa-times"></i></a>
							</form>
							<hr style="margin-top: 10px; margin-bottom: 10px;">	
						</div><!--/.col-md-12 -->
					</div><!--/.row -->
					<div class="row" style="margin-left:0px; margin-right:0px;">
						<div class="col-md-8">
						  <table gpid="sync-list" class="table table-bordered table-condensed table-hover">
							<thead>
							<tr>
							  <th>Remote</th>
							  <th>Type</th>
							  <th>State</th>
							  <th>Remark</th>
							  <th>Time</th>
							  <th>Act</th>
							</tr>
							</thead>
							<tbody>
							</tbody>
						  </table>
						</div><!--/.col-md-8 -->
						<div class="col-md-4">
							同步统计信息
						</div><!--/.col-md-4 -->
					</div><!--/.row -->
                  </div><!-- /.tab-pane -->
					<div id="tab_2" class="tab-pane">
						<div class="row m-l-none m-r-none">
							<div class="col-md-6">
								<form class="form-horizontal">
									<div class="form-group">
										<label class="col-sm-3 control-label" for="ldap-host">ldap Host</label>
										<div class="col-sm-4">
											<input type="text" placeholder="Ip address" id="ldap-host" class="form-control">
										</div>
										<label class="col-sm-1 control-label" for="ldap-port">Port</label>
										<div class="col-sm-2">
											<input type="text" placeholder="Port" id="ldap-port" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label" for="ldap-base-dn">Base DN</label>
										<div class="col-sm-9">
											<input type="text" placeholder="Node Id" id="ldap-base-dn" class="form-control">
										</div>  
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label" for="ldap-filter">Filter</label>
										<div class="col-sm-6">
											<input type="text" placeholder="Node Id" id="ldap-filter" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label" for="ldap-admin">Principal</label>
										<div class="col-sm-3">
											<input type="text" placeholder="Node Id" id="ldap-admin" class="form-control">
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-3 control-label" for="ldap-password">Password</label>
										<div class="col-sm-3">
											<input type="text" placeholder="Entity name" id="ldap-password" class="form-control">
										</div>
									</div>
								</form>
							</div>
							<div class="col-md-6">
								<div class="col-md-12">
									<form class="form-inline">
										<label >Attr  </label> <input type="text" placeholder="Share name" class="form-control input-sm"> &nbsp;
										<label >Mapto  </label> <input type="text" placeholder="Doc name" class="form-control input-sm">
										<a class="btn btn-default btn-sm"><i class="fa fa-plus"></i></a>
									</form>	
									<hr class="m-t-sm m-b-sm">
								</div>
								<div class="col-md-12">
									<table gpid="mapping-table" class="table table-bordered table-condensed">
									<thead>
										<tr>
											<th >Attribute</th>
											<th >Mapping </th>
											<th >Action</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									</table>
								</div>
							</div>
						</div><!-- /.row -->
						<div class="box-footer">
							<a class="btn btn-default" >Cancel</a>
							<a class="btn btn-info pull-right" >Save</a>
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
    <script src="${path_plugins}/datatables/datatables.min.js" type="text/javascript"></script>
    <script src="${path_plugins}/datatables/plugins/bootstrap/datatables.bootstrap.js" type="text/javascript"></script>
	<script src="${path_plugins}/uniform/jquery.uniform.min.js" type="text/javascript"></script>
    <!-- Select2 -->
    <script src="${path_plugins}/select2/select2.js"></script>
	<script src="${path_plugins}/moment/moment.min.js"></script>
	<script src="${path_plugins}/daterangepicker/daterangepicker.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
	
    <!-- AdminLTE for demo purposes -->
    <script src="${path_script}/ga/account-sync.js"></script>
  </body>
</html>
