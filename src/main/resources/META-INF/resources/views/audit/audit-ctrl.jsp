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
				 <jsp:param name="curr_page" value="audit-ctrl"/> 
		  </jsp:include> 
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            审计控制
            <small>Here manage shared documents</small>
          </h1>
        </section>
        <!-- Main content -->
        <section class="content">
			<!-- Custom Tabs -->
			<div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#tab_1">审计信息</a></li>
					<li ><a data-toggle="tab" href="#tab_2">审计控制</a></li>      
					<li ><a data-toggle="tab" href="#tab_3">工作组审计</a></li>       				  
                </ul>
			<div class="tab-content">
               <div id="tab_1" class="tab-pane active">
				   <div class="row no-margin">
						<div class="col-lg-12">		
							<form class="form-inline" style="margin-bottom: 10px;">
								<label>Subject : </label> <input type="text" placeholder="entity name" class="form-control input-sm"> &nbsp;
								<label>Operation : </label> <input type="text" placeholder="Doc name" class="form-control input-sm"> &nbsp;
								<label>Object : </label> <input type="text" placeholder="Doc name" class="form-control input-sm">
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-times"></i></a>
							</form>
							<hr style="margin-top: 10px; margin-bottom: 10px;">	
						</div>
					</div>
					<div class="box-body no-padding">
					  <table class="table table-bordered">
						<thead>
						<tr>
						  <th style="width: 60px">Id</th>
						  <th style="width: 100px">Subject</th>
						  <th style="width: 100px">Operation</th>
						  <th style="width: 80px">Action</th>
						  <th style="width: 80px">Object</th>
						  <th style="width: 80px">Type</th>
						  <th>Predicates</th>
						  <th style="width: 130px">Time</th>
						</tr>
						</thead>
						<tbody>
						<tr>
						  <td>1</td>
						  <td>dev001</td>
						  <td>new task</td>
						  <td>insert task</td>
						  <td>T00001</td>
						  <td>Task</td>
						  <td>{"a":"b","a":"b","a":"b","a":"b","a":"b","a":"b"}</td>
						  <td>2015-5-6 12:09:08</td>
						</tr>
						<tr>
						  <td>2</td>
						  <td>dev001</td>
						  <td>new task</td>
						  <td>insert task</td>
						  <td>T00001</td>
						  <td>Task</td>
						  <td>{"a":"b","a":"b","a":"b","a":"b","a":"b","a":"b"}</td>
						  <td>2015-5-6 12:09:08</td>
						</tr>
						<tr>
						  <td>3</td>
						  <td>dev001</td>
						  <td>new task</td>
						  <td>insert task</td>
						  <td>T00001</td>
						  <td>Task</td>
						  <td>{"a":"b","a":"b","a":"b","a":"b","a":"b","a":"b"}</td>
						  <td>2015-5-6 12:09:08</td>
						</tr>
						<tr>
						  <td>4</td>
						  <td>dev001</td>
						  <td>new task</td>
						  <td>insert task</td>
						  <td>T00001</td>
						  <td>Task</td>
						  <td>{"a":"b","a":"b","a":"b","a":"b","a":"b","a":"b"}</td>
						  <td>2015-5-6 12:09:08</td>
						</tr>
						<tr>
						  <td>5</td>
						  <td>dev001</td>
						  <td>new task</td>
						  <td>insert task</td>
						  <td>T00001</td>
						  <td>Task</td>
						  <td>{"a":"b","a":"b","a":"b","a":"b","a":"b","a":"b"}</td>
						  <td>2015-5-6 12:09:08</td>
						</tr>
					  </tbody>
					  </table>
					</div>
					<div class="box-footer clearfix">
					  <ul class="pagination pagination-sm no-margin pull-right">
						<li><a href="#">«</a></li>
						<li><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">»</a></li>
					  </ul>
					</div>
                  </div><!-- /.tab-pane -->
                  <div id="tab_2" class="tab-pane">
					  <div class="row" style="margin-left:0px; margin-right:0px;">
						  <div class="col-md-12">
							<form class="form-inline">
								<label>Share : </label> <input type="text" placeholder="Share name" class="form-control input-sm"> &nbsp;
								<label>Doc : </label> <input type="text" placeholder="Doc name" class="form-control input-sm">
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
							</form>	
							<hr style="margin-top: 10px; margin-bottom: 10px;">
						  </div>				  
						</div>
						<div class="row no-margin" >
							<div class="col-md-9">
								<div class="box-body no-padding" >
								  <table class="table table-bordered">
									<tbody>
										<tr>
										  <th style="width: 10px">#</th>
										  <th style="width: 100px">Operation</th>
										  <th style="width: 100px">Object Type</th>
										  <th>Description</th>
										  <th style="width: 30px">Oper</th>
										</tr>
										<tr>
										  <td>1.</td>
										  <td>New</td>
										  <td>
											Task
										  </td>
										  <td>
											Create new task object information
										  </td>
										  <td>
											<button type="button" class="btn btn-default btn-xs"><i class="fa fa-edit"></i></button>
										  </td>
										</tr>
										<tr>
										  <td>2.</td>
										  <td>New</td>
										  <td>
											Task
										  </td>
										  <td>
											Create new task object information
										  </td>
										  <td>
											<button type="button" class="btn btn-default btn-xs"><i class="fa fa-edit"></i></button>
										  </td>
										</tr>
										<tr>
										  <td>3.</td>
										  <td>New</td>
										  <td>
											Task
										  </td>
										  <td>
											Create new task object information
										  </td>
										  <td>
											<button type="button" class="btn btn-default btn-xs"><i class="fa fa-edit"></i></button>
										  </td>
										</tr>
									</tbody>
								 </table>
								</div><!-- /.box-body -->
								<div class="box-footer clearfix">
								  <ul class="pagination pagination-sm no-margin pull-right">
									<li><a href="#">«</a></li>
									<li><a href="#">1</a></li>
									<li><a href="#">2</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">»</a></li>
								  </ul>
								</div><!-- /.box-footer -->
							</div><!-- /.col-md-9 -->					
						</div><!-- /.row -->
					</div><!-- /.tab-pane -->
                  <div id="tab_3" class="tab-pane">
					  <div class="row" style="margin-left:0px; margin-right:0px;">
						  <div class="col-md-12">
							<form class="form-inline">
								<label>Share : </label> <input type="text" placeholder="Share name" class="form-control input-sm"> &nbsp;
								<label>Doc : </label> <input type="text" placeholder="Doc name" class="form-control input-sm">
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
							</form>	
							<hr style="margin-top: 10px; margin-bottom: 10px;">
						  </div>				  
						</div>
						<div class="row no-margin" >
							<div class="col-md-10">
								<div class="box-body no-padding" >
								  <table class="table table-bordered">
									<tbody>
										<tr>
										  <th style="width: 10px">Id</th>
										  <th style="width: 200px">Workgroup</th>										  
										  <th>Description</th>				
										  <th style="width: 240px" colspan="3">Object Type</th>										  
										</tr>
										<tr>
										  <td>w00001</td>
										  <td>Demo Wgrp</td>
										  <td>
											Create new task object information
										  </td>									  
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">任务
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">网盘
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">Post
											  </label>
											</div>
										  </td>
										</tr>
										<tr>
										  <td>w00002</td>
										  <td>Demo Wgrp</td>
										  <td>
											Create new task object information
										  </td>									  
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">任务
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">网盘
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">Post
											  </label>
											</div>
										  </td>
										</tr>
										<tr>
										  <td>w00003</td>
										  <td>Demo Wgrp</td>
										  <td>
											Create new task object information
										  </td>								  
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">任务
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">网盘
											  </label>
											</div>
										  </td>
										  <td>
											<div class="checkbox no-margin">
											  <label>
												<input type="checkbox">Post
											  </label>
											</div>
										  </td>
										</tr>
									</tbody>
								 </table>
								</div><!-- /.box-body -->
								<div class="box-footer clearfix">
								  <ul class="pagination pagination-sm no-margin pull-right">
									<li><a href="#">«</a></li>
									<li><a href="#">1</a></li>
									<li><a href="#">2</a></li>
									<li><a href="#">3</a></li>
									<li><a href="#">»</a></li>
								  </ul>
								</div><!-- /.box-footer -->
							</div><!-- /.col-md-9 -->					
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
    <!-- AdminLTE App -->
    <script src="${path_script}/app.min.js"></script>
    <!-- Sparkline -->
    <script src="${path_plugins}/sparkline/jquery.sparkline.min.js"></script>
    <!-- SlimScroll 1.3.0 -->
    <script src="${path_plugins}/slimScroll/jquery.slimscroll.min.js"></script>
	<script src="${path_script}/message.js"></script>
    <script src="${path_script}/pages/dashboard2.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="${path_script}/demo.js"></script>
  </body>
</html>
