<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
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
				 <jsp:param name="curr_page" value="orghier"/> 
		  </jsp:include> 
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            组织架构信息
            <small>Here manage shared documents</small>
          </h1>
        </section>
        <!-- Main content -->
        <section class="content">

              <!-- Custom Tabs -->
              <div id="page-tabs" class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                  <li class="active"><a data-toggle="tab" href="#tab_1">组织架构</a></li>
                  <li gpid="org-member-tab"><a data-toggle="tab" href="#tab_2">组织成员</a></li>	  
                </ul>
                <div class="tab-content">
					<div id="tab_1" class="tab-pane active">
					<div class="row no-margin">
						<div class="col-md-6">
						  <div class="col-md-12 no-padding">
							<label class="m-b-none  p-t-xxs"> Node Hierarchy </label>
							<hr class="m-t-sm m-b-sm">
						  </div>
							<div class="col-md-12 no-padding">
								<div class="box-body p-xxs box-border">
									<div gpid="org-hier-tree" style="">
									</div>  
								</div><!-- /.box-body -->
							</div>
						</div><!-- /.col-md-6 -->
						<div class="col-md-6">
						  <div class="col-md-12 no-padding">
							<label class="m-b-none  p-t-xxs"> Node Setting </label>
							<a gpid="orghier-new-btn" class="btn btn-warning btn-xs"
								data-placement="top" 
								data-toggle="tooltip" 
								title="Create new Hierarchy node"><i class="fa fa-fw fa-plus"></i> </a>
							<a gpid="orghier-save-btn" class="btn btn-primary btn-xs"
								data-placement="top" 
								data-toggle="tooltip" 
								title="save Hierarchy node"><i class="fa fa-fw fa-save"></i> </a>
							<hr class="m-t-sm m-b-sm">
						  </div>
						  <div class="col-md-12 no-padding">
							<form class="form-horizontal">
							  <div class="box-body p-none">
								<div id="node-type-selector" class="form-group hidden">
								  <label class="col-sm-3 control-label" for="node-type">Node Binding</label>
								  <div class="col-sm-3">
										<select class="form-control select2" id="node-type">
											<option value="SIBLING">Sibling Node</option>
											<option value="CHILDREN">Child Node</option>
										</select>		
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" >Current Node</label>
								  <div class="col-sm-3">
									<input type="text" id="node-parent-id" class="form-control hidden">
									<input type="text" id="node-id" class="form-control hidden">
									<p id="current-node-name" class="form-control-static">Current Node</p>
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="node-name">Org Name</label>
								  <div class="col-sm-7">
									<input type="text" placeholder="orgnization name" id="node-name" value="org name x" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="node-admin">Admin</label>
								  <div class="col-sm-4">
									  <div class="input-group">
										<input type="text" class="form-control" placeholder="admin" value="admin" id="node-admin" disabled>
										<span class="input-group-btn">
										  <a class="btn btn-info btn-sm" gpid="admin-sel-btn"><i class="fa fa-search"></i></a>
										</span>
									  </div>
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="node-manager">Manager</label>
								  <div class="col-sm-4">
									  <div class="input-group">
										<input type="text" class="form-control" placeholder="manager" value="manager" id="node-manager" disabled>
										<span class="input-group-btn">
										  <a class="btn btn-info btn-sm" gpid="mgmr-sel-btn"><i class="fa fa-search"></i></a>
										</span>
									  </div>
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="node-email">Email</label>
								  <div class="col-sm-4">
									<input type="text" placeholder="contact email" id="node-email" value="tst@163.com" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="node-description">Description</label>
								  <div class="col-sm-7">
									<textarea placeholder="Enter ..." rows="2" id="node-description" value="this is the dmeo description xxx" class="form-control"></textarea>
								  </div>
								</div>
							  </div><!-- /.box-body -->
							</form>
							</div><!-- /.col-md-12 -->
						</div><!-- /.col-md-6 -->
					</div><!-- /.row -->
					</div><!-- /.tab-pane -->
                  <div id="tab_2" class="tab-pane">
					<div class="row no-margin">
						<div class="col-md-8">		
							<div class="col-md-12 no-padding">
								<form class="form-inline" style="margin-bottom: 10px;">
									<label class="m-r-xs">Org Name </label> 
									<input type="text" class="form-control hidden" id="orgmember-node-id">
									<input type="text" class="form-control" placeholder="org name" id="orgmember-node-name" disabled>
									<a gpid="orgmember-node-refresh-btn" class="btn btn-default btn-sm"><i class="fa fa-refresh"></i></a>
									<a gpid="orgmember-add-btn" class="btn btn-primary btn-sm"><i class="fa fa-plus"></i></a>
								</form>
								<hr class="m-t-sm m-b-sm">	
							</div>
							<div class="col-md-12 no-padding">
								<table gpid="orgmember-list" class="table table-bordered table-condensed">
									<thead>
										<tr>
										  <th >Account</th>
										  <th >Name</th>								  
										  <th >Email</th>					 								  
										  <th>Type</th>
										  <th >State</th>
										  <th >Act</th>
										</tr>
									</thead>
									<tbody>								
									</tbody>
								</table>
							</div>
						</div><!-- /.col-md-8 -->
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
    <script src="${path_plugins}/select2/select2.full.min.js"></script>
	<!-- JStree -->
	<script src="${path_plugins}/jstree/dist/jstree.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>	
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
	<%@include file="../../dialog/select-user.jsp" %>
    <!-- AdminLTE for demo purposes -->
    <script src="${path_script}/ga/orghier.js"></script>
  </body>
</html>
