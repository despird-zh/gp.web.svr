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
				 <jsp:param name="curr_module" value="workgroup"/> 
				 <jsp:param name="curr_page" value="new"/> 
		  </jsp:include> 
        </section>
        <!-- /.sidebar -->
      </aside>

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            编辑工作组
            <small>维护工作组信息</small>
          </h1>
        </section>
        <!-- Main content -->
        <section class="content">
			 <!-- Custom Tabs -->
              <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
					<li class="active" gpid="member-list-tab"><a data-toggle="tab" href="#tab_2">基本信息</a></li>      
					<li class="" gpid="member-tab" ><a data-toggle="tab" href="#tab_3">成员用户</a></li>
					<li class="" gpid="member-group-tab" ><a data-toggle="tab" href="#tab_1">成员用户组</a></li>    						
                </ul>
                <div class="tab-content">
                  <div id="tab_2" class="tab-pane active">
					<div class="row m-l-none m-r-none">
						<div class="col-md-6">
							<div class="col-md-12 no-padding">
								<form class="form-inline">
									<label>Workgroup basic information</label> 
								</form>	
								<hr class="m-t-sm m-b-sm">
							</div>
							<div class="col-md-12 no-padding">
								<form class="form-horizontal">
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-name">工作组名称</label>
									  <div class="col-sm-6">
										<input type="text" id="wrokgroup-id" value="${wgroup_id}" class="hidden">
										<input type="text" placeholder="Workgroup Name" value="Local" id="workgroup-name" class="form-control">
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-state-sel">状态</label>
									  <div class="col-sm-3">
											<select class="form-control select2" id="workgroup-state-sel">
												<option value="READ_WRITE">Read/Write</option>
												<option value="READ_ONLY">Read Only</option>
												<option value="CLOSE">Close</option>
											</select>		
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-admin">管理员</label>
									  <div class="col-sm-4">
										<div class="input-group">
											<input type="text" class="form-control" id="workgroup-admin" disabled>
											<span class="input-group-btn">
											  <a gpid="admin-sel-btn" class="btn btn-info btn-sm" ><i class="fa fa-user"></i></a>
											</span>
										  </div>
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-manager">管理人</label>
									  <div class="col-sm-4">
										<div class="input-group">
											<input type="text" class="form-control" id="workgroup-manager" disabled>
											<span class="input-group-btn">
											  <a gpid="manager-sel-btn" class="btn btn-info btn-sm" ><i class="fa fa-user"></i></a>
											</span>
										  </div>
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-org-id">Organization</label>
									  <div class="col-sm-4">
										<div class="input-group">
											<input type="hidden" id="workgroup-org-id">
											<input type="text" id="workgroup-org-name" class="form-control" disabled>
											<span class="input-group-btn">
											  <a gpid="orghier-sel-btn" class="btn btn-info btn-sm" ><i class="fa fa-search"></i></a>
											</span>
										  </div>
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-description">头像</label>
									  <div class="col-sm-2">
										<div class="avatar-view" title="Change the avatar">
										  <img id="workgroup-avatar" src="../img_cache/57-20160416-233518.png" alt="Avatar">
										</div>
									  </div>									  
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-description">描述</label>
									  <div class="col-sm-7">
										<textarea placeholder="Enter ..." rows="3" id="workgroup-description" class="form-control"></textarea>
									  </div>
									</div>
								</form><!-- /.form-horizontal -->
							</div>
						</div><!-- /.col-md-6 -->
						<div class="col-md-6">
							<div class="col-md-12 no-padding">
								<form class="form-inline">
									<label>Storage and Feature information</label> 
								</form>	
								<hr class="m-t-sm m-b-sm">
							</div>
							<div class="col-md-12 no-padding">
								<form class="form-horizontal">		
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-storage-id">Storage</label>
									  <div class="col-sm-4">
											<select class="form-control select2" id="workgroup-storage-id">												
											</select>						
									  </div>
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-publish-enable">Public Cabinet</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
										  <input type="checkbox" id="workgroup-publish-enable"> 开启
										</label>			
									  </div>
									  <label class="col-sm-2 control-label" for="workgroup-publish-capacity">Capacity</label>
									  <div class="col-sm-4">
										<div class="input-group">
										<input type="text" value="20" id="workgroup-publish-capacity" class="form-control">
										<span class="input-group-addon">M</span>
										</div>					
									  </div>						  
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-netdisk-enable">Private Cabinet</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
										  <input type="checkbox" id="workgroup-netdisk-enable"> 开启
										</label>			
									  </div>
									  <label class="col-sm-2 control-label" for="workgroup-netdisk-capacity">Capacity</label>
									  <div class="col-sm-4">
										<div class="input-group">
										<input type="text" value="20" class="form-control" id="workgroup-netdisk-capacity">
										<span class="input-group-addon">M</span>
										</div>					
									  </div>						  
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-topic-enable">Topic</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
											<input type="checkbox"  id="workgroup-topic-enable">  开启
										</label> 							
									  </div>					  
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-share-enable">Share</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
											<input type="checkbox" id="workgroup-share-enable">  开启
										</label> 			
									  </div>	
									  <label class="col-sm-2 control-label" for="workgroup-link-enable">Link</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
											<input type="checkbox" id="workgroup-link-enable"> Enable
										</label> 			
									  </div>		  
									</div>
									<div class="form-group">
									  <label class="col-sm-3 control-label" for="workgroup-task-enable">Task</label>
									  <div class="col-sm-3">
										<label class="checkbox-inline">
											<input type="checkbox" id="workgroup-task-enable">  开启
										</label> 		
									  </div>
									  <label class="col-sm-2 control-label" for="workgroup-task-weight">Weight</label>
									  <div class="col-sm-3">
										<div class="input-group">
										<input type="text" value="20" class="form-control" id="workgroup-task-weight">
										<span class="input-group-addon">%</span>
										</div>	
									  </div>	
									</div>
								</form><!-- /.form-horizontal -->
							</div>
					  </div><!-- /.col-md-6 -->
					</div><!-- /.row -->
					<div class="box-footer">
						<button gpid="workgroup-back-btn" type="button" class="btn btn-default" >Back</button>
						<button gpid="workgroup-save-btn" class="btn btn-info pull-right" >Save</button>
					</div><!-- /.box-footer -->
                  </div><!-- /.tab-pane -->
				<div id="tab_3" class="tab-pane">
					 <div class="row no-margin" >
						<div class="col-md-7">
							<div class="col-md-12 no-padding">
								<form class="form-inline">
									<label>用户 : </label> 
									<input gpid="member-search-user" type="text" placeholder="user name, account, email" class="form-control input-sm"> &nbsp;
									<label>Entity : </label> 
									<select gpid="member-search-enode" class="form-control select2" style="width: 150px;">														
									</select>&nbsp;
									<a gpid="member-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
								</form>	
								<hr class="m-t-sm m-b-sm">
							</div>
							<div class="col-md-12 no-padding" style="min-height:280px;">
							  <table gpid="member-list" class="table table-bordered table-condensed table-hover">
								<thead>
									<tr>
									  <th >Name</th>
									  <th >Entity</th>
									  <th >Email</th>
									  <th >Role</th>
									  <th >Source</th>
									  <th >Act.</th>
									</tr>
								</thead>
								<tbody>						
								</tbody>
							  </table>
							</div><!-- /.box-body -->
						</div><!-- /.col-md-7 -->
						<div class="col-md-5">
						  <div class="col-md-12 no-padding">
							  <form class="form-inline">
								<label class="p-t-xxs  m-r-sm"> Member Information </label>
								<a gpid="add-new-member" class="btn btn-warning btn-xs"
									data-placement="top" 
									data-toggle="tooltip" 
									title="Switch to new member mode"><i class="fa fa-fw fa-plus"></i> </a>	
								<a gpid="member-save-btn" class="btn btn-primary btn-xs"
									data-placement="top" 
									data-toggle="tooltip" 
									title="Save workgroup member"><i class="fa fa-fw fa-save"></i></a>									
								</form>
								<hr class="m-t-sm m-b-sm">
						  </div>
						  <div class="col-md-12 no-padding">
							<form class="form-horizontal">
							  
								<div id="account-finder" class="form-group hidden">
								  <label class="col-sm-3 control-label" for="avail-user-sel">Find </label>
								  <div class="col-sm-4">
										<select class="form-control select2" id="avail-user-sel" style="width: 150px;">
										</select>
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="member-account">Account</label>
								  <div class="col-sm-4">
									<input type="text" placeholder="account" value="" id="member-account" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="member-name">name</label>
								  <div class="col-sm-4">
									<input type="text" placeholder="member name" id="member-name" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="member-email">Email</label>
								  <div class="col-sm-4">
									<input type="text" placeholder="member email" value="" disabled="" id="member-email" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="member-entity">Entity</label>
								  <div class="col-sm-8">
									<input type="text" placeholder="Entity name" value="" id="member-entity" class="form-control">
								  </div>
								</div>
								<div class="form-group">
								  <label class="col-sm-3 control-label" for="member-role">Role</label>
								  <div class="col-sm-4">
										<select class="form-control select2" id="member-role">
											<option value="MANAGER">Manager</option>
											<option value="CONTRIBUTOR">Contributor</option>
											<option value="CONSUMER">Consumer</option>
										</select>
								  </div>
								</div>
							</form>
							</div><!-- /.box-body -->
						</div><!-- /.col-md-6 -->
					</div><!-- /.row -->
					<div class="box-footer">
						<button gpid="member-close-btn" type="button" class="btn btn-default" >Back</button>
					</div><!-- /.box-footer -->
				</div><!-- /.tab-pane -->
				<div id="tab_1" class="tab-pane">
				<div class="row no-margin">
					<div class="col-md-4">
						<div class="col-md-12 no-padding">
							<form class="form-inline">
								<label>Group : </label> <input gpid="wgroup-group-search-name" type="text" placeholder="group name" class="form-control input-sm"> 
								<a gpid="wgroup-group-search-btn" type="submit" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
							</form>	
							<hr class="m-t-sm m-b-sm">
						</div>
						<div class="col-md-12 no-padding" style="min-height:280px;">
							<table gpid="wgroup-group-table" class="table table-bordered table-condensed table-hover table-ellipsis" style="table-layout: fixed;">
								<thead><tr>
								  <th>Group Name</th>
								  <th>Descr</th>
								  <th>Act.</th>
								</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div><!-- /.box-body -->
					</div><!-- /.col-md-6 -->
					<div class="col-md-4">
						<div class="col-md-12 no-padding">
							<label class="p-t-xxs  m-r-sm"> Group Information </label>
							<a gpid="wgroup-group-new-btn" class="btn btn-warning btn-xs"
								data-placement="top" 
								data-toggle="tooltip" 
								title="Create new Group"><i class="fa fa-fw fa-plus"></i> </a>
							<a gpid="wgroup-group-save-btn" class="btn btn-primary btn-xs" 
								data-placement="top" 
								data-toggle="tooltip" 
								title="Save the group information"><i class="fa fa-fw fa-save"></i></a>
							<hr class="m-t-sm m-b-sm">
						</div>
						<div class="col-md-12 no-padding">
							<form class="form-horizontal"  style="min-height:280px;">								
								<div class="form-group">
									<label class="col-sm-3 control-label" for="wgroup-group-name">Group</label>
									<div class="col-sm-8">
									<input type="text" id="wgroup-group-id" value="" class="hidden">
									<input type="text" placeholder="Group Name" value="" id="wgroup-group-name" class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label" for="wgroup-group-descr">Descr</label>
									<div class="col-sm-8">
									<textarea class="form-control" rows="3" id="wgroup-group-descr" placeholder="Enter ..."></textarea>
									</div>
								</div>
							</form>
						</div>
					</div><!-- /.col-md-4 -->
					<div class="col-md-4">
						<div class="col-md-12 no-padding">
							<label class="p-t-xxs m-r-sm"> Group Member </label> 
							<a gpid="group-member-refresh-btn" class="btn btn-default btn-xs"
								data-placement="top" 
								data-toggle="tooltip" 
								title="Refresh members of group"> <i class="fa fa-fw fa-refresh"></i> </a>
							<a gpid="wgroup-group-member-add-btn" class="btn btn-primary btn-xs"
								data-placement="top" 
								data-toggle="tooltip" 
								title="Add member to group"><i class="fa fa-fw fa-user-plus"></i></a>
							<hr class="m-t-sm m-b-sm">
						</div>
						<div class="col-md-12 no-padding" style="min-height:280px;">
							<table gpid="group-member-table" class="table table-bordered table-condensed table-hover">
								<thead>
									<tr>
										<th>Name</th>
										<th>Mail</th>
										<th>Type</th>
										<th>Act</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>	
						</div><!-- /.box-body -->			  
					</div><!-- /.col-md-4 -->
				</div><!-- /.row -->
				<div class="box-footer">
					<button gpid="group-close-btn" type="button" class="btn btn-default" >Back</button>
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
	<%@include file="../../dialog/select-user.jsp" %>
	<%@include file="../../dialog/select-wgroup-mbr.jsp" %>
	<%@include file="../../dialog/select-orghier.jsp" %>
	<%@include file="../../dialog/avatar-cropper.jsp" %>
    <!-- AdminLTE for demo purposes -->
    <script src="${path_script}/ga/workgroup-edit.js"></script>
  </body>
</html>
