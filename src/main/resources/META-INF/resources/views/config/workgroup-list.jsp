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
        				 <jsp:param name="curr_module" value="workgroup"/> 
        				 <jsp:param name="curr_page" value="list"/> 
        		  </jsp:include> 
                </section>
                <!-- /.sidebar -->
            </aside>

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                    工作组列表
                    <small>查询系统中的工作组信息</small>
                    </h1>
                </section>
                <!-- Main content -->
                <section class="content">
                    <!-- Custom Tabs -->
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#tab_1">内部工作组</a></li>
                            <li ><a data-toggle="tab" href="#tab_2">外部工作组</a></li>      				  
                        </ul>
                        <div class="tab-content">
                            <div id="tab_1" class="tab-pane active">
            					<div class="col-md-12">		
            						<form class="form-inline" style="margin-bottom: 10px;">
            							<label>workgroup : </label> <input gpid="search-local-wname" type="text" placeholder="entity name" class="form-control input-sm"> 
            							<a gpid="search-local-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
            							<a gpid="search-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-times"></i></a>
            						</form>		
            						<hr style="margin-top: 10px; margin-bottom: 10px;">							
            					</div>
            				    <div class="box-body">
                                    <table gpid="wgroup-local-table" class="table table-bordered table-condensed table-hover">
                    				    <thead>
                        					<tr>
                                                <th>Source</th>
                                                <th>Workgroup</th>
                                                <th>State</th>
                                                <th>Admin</th>
                                                <th>Description</th>
                                                <th>Task</th>
                                                <th>Create</th>
                                                <th>Action</th>
                                            </tr>
                        				</thead>
                                        <tbody>
                                        </tbody>
            				        </table>
                                </div>
                            </div><!-- /.tab-pane -->
                            <div id="tab_2" class="tab-pane">
        						<div class="col-md-12">		
        							<form class="form-inline" style="margin-bottom: 10px;">
        								<label>Workgroup : </label> <input gpid="search-mirror-wname" type="text" placeholder="entity name" class="form-control input-sm"> 
        								<a gpid="search-mirror-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
        								<a gpid="search-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-times"></i></a>
        							</form>
        							<hr style="margin-top: 10px; margin-bottom: 10px;">								
        						</div>
            					<div class="box-body">
            					  <table gpid="wgroup-mirror-table" class="table table-bordered table-condensed table-hover">
            						<thead>
            							<tr>
            								<th>#</th>
            								<th>Source</th>
            								<th>Workgroup</th>
            								<th>State</th>
            								<th>Admin</th>
            								<th>Description</th>
            								<th>Task</th>
            								<th>Create</th>
            								<th>Action</th>
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
	<script src="${path_script}/ga/workgroup-list.js"></script>
  </body>
</html>
