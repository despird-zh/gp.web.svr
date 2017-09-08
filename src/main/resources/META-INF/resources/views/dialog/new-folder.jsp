<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<div class="modal fade" id="new-folder-modal" tabindex="-1" role="dialog" aria-labelledby="select-user-modal-label"><!-- clipboard modal -->
  <div class="modal-dialog" role="document" style="width:650px;">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="select-user-modal-label">New Folder</h4>
	  </div>
	  <div class="modal-body" style="padding-bottom: 0px;">
		  <form>
		  <div class="row no-margin" >
			  <div class="col-md-12">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="doc-name" class="col-sm-2 control-label">Parent Path</label>
							<div class="col-sm-5">
								<p class="form-control-static">Xuser001</p>
							</div>
						</div>
					</div>
				<hr style="margin-top: 10px; margin-bottom: 10px;">
			  </div>				  
		  </div>
		<div class="row no-margin">
			<div class="col-md-12">
				<div class="box-body no-padding">
					<div class="form-horizontal">
						<div class="form-group">
							<label for="doc-name" class="col-sm-2 control-label">Name</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="folder-name" placeholder="Email">
							</div>
						</div>
						<div class="form-group">
							<label for="doc-owner" class="col-sm-2 control-label">Owner</label>
							<div class="col-sm-5">
								<input type="text" class="form-control" id="folder-owner" placeholder="folder owner">
							</div>
						</div>
						<div class="form-group">
							<label for="doc-subject" class="col-sm-2 control-label">Description</label>
							<div class="col-sm-9">
								<textarea class="form-control" id="folder-descr" placeholder="description" rows="3"></textarea>
							</div>
						</div>
						<div class="form-group">
							<label for="doc-subject" class="col-sm-2 control-label">Create</label>
							<div class="col-sm-9">
								<p style="padding-bottom: 7px;" class="form-control-static">2015-6-7 12:30:32</p>
							</div>
						</div>
						<div class="form-group">
							<label for="doc-subject" class="col-sm-2 control-label">Creator</label>
							<div class="col-sm-10">
								<p style="padding-bottom: 7px;" class="form-control-static">Xuser001</p>
							</div>
						</div>
					</div>
				</div>
			</div><!-- /.col-md-7 -->
		</div><!-- /.row -->
		</form>	
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button gpid="btn_exec" type="button" class="btn btn-primary">Save</button>
	  </div>
	</div>
  </div>
</div><!-- clipboard modal -->

<script type="text/javascript">
$(function (){
	"use strict"
	
	var NewFolderModal = {
		
		$newfolder_modal : $("#new-folder-modal"),
		$folder_name : $('#folder-name'),
		$folder_description : $('#folder-descr'),
		$folder_owner : $('#folder-owner'),
		$save_btn : $('#new-folder-modal button[gpid="btn_exec"]'),
		
		initial : function(){
			
			var _self = this;
			_self.$newfolder_modal.modal('hide');
			
			_self.$save_btn.on('click', $.proxy(_self.saveFolder, _self));
		
		}
	};
	
	NewFolderModal.saveFolder = function(){
		
		var _self = this;
		$.ajax({
			url: "../cabinet/new-folder.do",
			dataType : "json",
			type: 'POST',
			data: { 
					cabinet_id : _self.cabid,
					folder_parent_id : _self.parentid,
					folder_name : _self.$folder_name.val(),
					folder_owner : _self.$folder_owner.val(),
					folder_descr : _self.$folder_description.val()
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, (response.state == "success") ? false : true);
				NewFolderModal.$newfolder_modal.modal('hide');
			}
		});
	};
	
	/*
	 * show select user dialog
	 */
	NewFolderModal.newFolderShow = function(_callback, _cabid, _parentid){
		
		var _self = this;
		_self.cabid = _cabid;
		_self.parentid = _parentid;
		_self.callback = _callback;
		_self.$newfolder_modal.modal('show');
	};
	
	NewFolderModal.initial();
	
	/*
	 * callback - callback method
	 * cabid    - the id of cabinet
	 * parentid - the id of parent folder
	 */
	GPContext.showNewFolder = function(callback,cabid,parentid){
		NewFolderModal.newFolderShow(callback,cabid,parentid);
	};
});
</script>