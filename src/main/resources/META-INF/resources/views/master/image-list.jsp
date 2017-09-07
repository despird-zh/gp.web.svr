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

	<style type="text/css">
		.table-condensed > tbody > tr > td.avatar-cell{
			padding-top : 0;
			padding-bottom : 0;
			padding-left : 13px;
		}
		.table-condensed > tbody > tr > td.avatar-cell > div{
			border: 2px solid #fff;
			border-radius: 2px;
			box-shadow: 0 0 5px rgba(0, 0, 0, 0.15);
			display: block;
			height: 30px;
			overflow: hidden;
			width: 30px;
		}
		.avatar-cell > div > img {
			width: 100%;
		}
	</style>
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
						 <jsp:param name="curr_page" value="images"/> 
				  </jsp:include> 
				</section>
			<!-- /.sidebar -->
			</aside>
			<!-- Content Wrapper. Contains page content -->
			<div class="content-wrapper">
				<!-- Content Header (Page header) -->
				<section class="content-header">
				  <h1>
					图片信息
					<small>管理、维护系统的图片资源</small>
				  </h1>
				</section>
				<!-- Main content -->
				<section class="content">
					<!-- Custom Tabs -->
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li gpid="list-tab" class="active"><a data-toggle="tab" href="#tab_1">图片查询</a></li>
							<li gpid="new-tab" class="hidden" ><a data-toggle="tab" href="#tab_2">新建图片</a></li>
							<li class="pull-right">
								<a gpid="new-image-btn" class="text-primary" role="button" href="javascript:void(0)"><i class="fa fa-fw fa-plus"></i>新建图片</a>
							</li>							
						</ul>
						<div class="tab-content">
							<div id="tab_1" class="tab-pane active">	
								<div class="row m-l-none m-r-none">
									<div class="col-md-8">							
										<div class="col-md-12 no-padding">		
											<form class="form-inline">
												<label>Format : </label> 								
												<select gpid="image-format" class="form-control select2" id="format_input">								
													<option value="png">PNG</option>
													<option value="jpg">JPG</option>
													<option value="gif">GIF</option>
												</select> &nbsp;
												<a gpid="image-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
												<a gpid="image-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
											</form>
											<hr class="m-t-sm m-b-sm">							
										</div>
										<div class="col-md-12 no-padding">
											<table gpid="image-table" class="table table-bordered table-condensed table-hover">
												<thead>
													<tr>
														<th>#</th>
														<th>Image</th>
														<th>Name</th>
														<th>Format</th>
														<th>Category</th>
														<th>Modifier</th>
														<th>Last Modify</th>
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
											<label class="p-t-xxs  m-r-sm"> Image Detail </label>											
											<a gpid="image-save-btn" class="btn btn-primary btn-xs" 
												data-placement="top" 
												data-toggle="tooltip" 
												title="Save the group information"><i class="fa fa-fw fa-save"></i></a>
											<hr class="m-t-sm m-b-sm">
										</div>
										<div class="col-md-12 no-padding">
											<form class="form-horizontal">
												<div class="form-group">
													<label class="col-sm-4 control-label" for="image-name">Image Name</label>
													<div class="col-sm-6">
														<input type="hidden" id="image-id">
														<input type="text" placeholder="image name" value="" id="image-name" class="form-control">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Image</label>
													<div class="col-sm-6">
														<div class="avatar-view" title="Change the avatar">
															<img gpid="image-avatar" src="../dist/img/avatar2.png" alt="Avatar">
														</div>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label" >Format</label>
													<div class="col-sm-6">
														<p class="form-control-static" gpid="image-format">png</p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Touch Date</label>
													<div class="col-sm-8">
														<p class="form-control-static" gpid="image-touch-date">2016-2-3 12:23:21</p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Modifier</label>
													<div class="col-sm-8">
														<p class="form-control-static" gpid="image-modifier">admin</p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-4 control-label">Modify Date</label>
													<div class="col-sm-8">
														<p class="form-control-static" gpid="image-modified-data">2016-2-3 12:23:21</p>
													</div>
												</div>
											</form><!-- /.form-horizontal -->
										</div>
									</div><!-- /.col-md-4 -->
								</div>
							</div><!-- /.tab-pane -->
							<div id="tab_2" class="tab-pane">
								<div class="row">
									<div class="col-md-12">
										<form class="avatar-form" action="../avatar" enctype="multipart/form-data" method="post">
										<div class="avatar-body">
											<div class="row">
											  <div class="col-md-2 p-xxs">
												<select gpid="image-category" class="form-control select2" name="category">								
													<option value="WGROUP_AVATAR">WorkGroup Avatar</option>
													<option value="USER_AVATAR">User Avatar</option>
												</select>
											  </div>
											  
												  <!-- Upload image and data -->
												<div class="avatar-upload col-md-10 p-xxs">
													<input type="hidden" class="avatar-src" name="avatar_src">
													<input type="hidden" class="avatar-data" name="avatar_data">
													<input type="hidden" name="oper_flag" value="new">
													<input type="file" class="avatar-input btn-sm" title="Select File" id="avatarInput" name="avatar_file">
												</div>
											</div>
											
											<!-- Crop and preview -->
											<div class="row">
											  <div class="col-md-9 p-xxs">
												<div class="avatar-wrapper m-t-none"></div>
											  </div>
											  <div class="col-md-3 p-xxs">
												<div class="avatar-preview preview-lg m-t-none"></div>
												<div class="avatar-preview preview-md"></div>
												<div class="avatar-preview preview-sm"></div>
											  </div>
											</div>
											<div class="row avatar-btns">
											  <div class="col-md-9">
												<div class="btn-group">
												  <button type="button" class="btn btn-primary" data-method="rotate" data-option="-90" title="Rotate -90 degrees"><i class="fa fa-rotate-left"></i></button>
												  <button type="button" class="btn btn-primary" data-method="rotate" data-option="-15">-15</button>
												  <button type="button" class="btn btn-primary" data-method="rotate" data-option="-30">-30</button>
												  <button type="button" class="btn btn-primary" data-method="rotate" data-option="-45">-45</button>
												</div>
												<div class="btn-group">
													<button type="button" class="btn btn-primary" data-method="rotate" data-option="45">+45</button>
													<button type="button" class="btn btn-primary" data-method="rotate" data-option="30">+30</button>
													<button type="button" class="btn btn-primary" data-method="rotate" data-option="15">+15</button>
													<button type="button" class="btn btn-primary" data-method="rotate" data-option="90" title="Rotate 90 degrees"><i class="fa fa-rotate-right"></i></button>
												</div>
											  </div>
											  <div class="col-md-3">											 
												<button type="submit" class="btn btn-primary pull-right avatar-save">Submit</button>											 
											  </div>											  
											</div>
										</div><!-- /.avatar-body -->
										</form><!-- /avatar-form -->
									</div><!-- /.col-md-12 -->
								</div><!-- /.row -->
								<div class="box-footer">
									<button gpid="new-close-btn" type="button" class="btn btn-default" >Close</button>
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
	<script src="${path_plugins}/cropper/2.3.0/cropper.min.js"></script>	
	<script src="${path_plugins}/bootstrap-fileinput/bootstrap.file-input.js"></script>	
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
    <!-- GPress Err Message -->
	<script src="${path_script}/message.js"></script>
	<%@include file="../../dialog/avatar-cropper.jsp" %>
	
	<script src="${path_script}/ga/image-list.js"></script>
  </body>
</html>
