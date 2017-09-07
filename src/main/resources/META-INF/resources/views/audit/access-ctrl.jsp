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
				 <jsp:param name="curr_page" value="access-ctrl"/> 
		  </jsp:include> 
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            访问控制
            <small>Here manage shared documents</small>
          </h1>
        </section>
        <!-- Main content -->
        <section class="content">
              <!-- Custom Tabs -->
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                  <li class="active"><a data-toggle="tab" href="#tab_1">用户访问</a></li>
                  <li ><a data-toggle="tab" href="#tab_2">实体访问</a></li>      				  
                </ul>
                <div class="tab-content">
               <div id="tab_1" class="tab-pane active">
				   <div class="row" style="margin-left:0px; margin-right:0px;">
						<div class="col-md-12">		
							<form class="form-inline" style="margin-bottom: 10px;">
								<label>Entity : </label> <input type="text" placeholder="entity name" class="form-control input-sm"> &nbsp;
								<label>Account : </label> <input type="text" placeholder="Doc name" class="form-control input-sm">
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
						  <th style="width: 100px">Account</th>
						  <th style="width: 100px">Mail</th>
						  <th style="width: 40px">Type</th>
						  <th style="width: 40px">State</th>
						  <th colspan="2">Task</th>
						  <th colspan="2">Post</th>
						  <th colspan="2">Disk</th>
						</tr>
						</thead>
						<tbody>
						<tr>
						  <td>1</td>
						  <td>dev001</td>
						  <td>dev02030@ssstttmm.com</td>
						  <td>内部</td>
						  <td>激活</td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
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
                  </div><!-- /.tab-pane -->
                  <div id="tab_2" class="tab-pane">
					<div class="row no-margin" >
						<div class="col-md-12">		
							<form class="form-inline" style="margin-bottom: 10px;">
								<label>Entity : </label> <input type="text" placeholder="entity name" class="form-control input-sm"> 
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
								<a type="submit" class="btn btn-default btn-sm"><i class="fa fa-times"></i></a>
							</form>
							<hr style="margin-top: 10px; margin-bottom: 10px;">	
						</div><!-- /.col-md-12 -->
					</div><!-- /.row -->
					<div class="row no-margin" >
						<div class="col-md-10">	
					<div class="box-body no-padding">
					  <table class="table table-bordered">
						<thead>
						<tr>
						  <th style="width: 10px">Id</th>
						  <th style="width: 140px">Entity</th>
						  <th style="width: 200px">Name</th>
						  <th colspan="2">Task</th>
						  <th colspan="2">Post</th>
						  <th colspan="2">Disk</th>
						</tr>
						</thead>
						<tbody>
						<tr>
						  <td>1</td>
						  <td>E00001-N00001</td>
						  <td>测试科技股份有限公司</td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">读取
							  </label>
							</div>
						  </td>
						  <td>
							<div class="checkbox no-margin">
							  <label>
								<input type="checkbox">写入
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
						</div><!-- /.col-md-10 -->
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
