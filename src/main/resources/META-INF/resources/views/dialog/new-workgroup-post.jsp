<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<style>

</style>
<div class="modal fade" id="new-post-modal" tabindex="-1" data-backdrop="false" aria-labelledby="new-post-modal-label"><!-- new post modal -->
  <div class="modal-dialog" style="width:700px;">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title">New Post</h4>
	  </div>
	  <div class="modal-body" style="overflow:hidden;">
		<div class=" box-form form-horizontal">
			<div class="form-group">
				<label class="col-sm-2 control-label" for="doc-name">Subject</label>
				<div class="col-sm-10">
					<input type="text" placeholder="Write subject" id="post-subject" class="form-control">
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label" for="doc-name">Content</label>

				<div class="col-sm-10" style="padding-top: 5px;">
					<div gpid="post-content" class="" placeholder="Content" 
						style="min-height:50px;max-height:200px;background:#ecf0f5;
						height:auto;overflow:auto; margin-bottom:5px;
						padding:5px;"></div>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Scope</label>
				<div class="col-sm-10">
					<label class="radio-inline">
						<input type="radio" gpid="post-private" name="post-scope" checked="checked" value="workgroup">Workgroup
					</label>
					<label class="radio-inline">
						<input type="radio" gpid="post-square" name="post-scope" value="square">Square
					</label>
				</div>
			</div>
			<div class="form-group" gpid="private-marker">
				<label class="col-sm-2 control-label">Attendee</label>
				<div class="col-sm-10">
					<select gpid="post-attendee" class="form-control select2" multiple="multiple" style="width: 100%;">
					</select>
				</div>
			</div>
			<div class="form-group" gpid="square-marker">
				<label class="col-sm-2 control-label" >Comment</label>
				<div class="col-sm-4">
					<label class="checkbox-inline">
						<input type="checkbox" gpid="post-comment" value="accept">Accept Public Comments
					</label>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Priority</label>
				<div class="col-sm-4">
					<select class="form-control select2 input-sm" gpid="post-priority">
					  <option>Normal</option>
					  <option>Medium</option>
					  <option>Urgent</option>
					</select>
				</div>
				<label class="col-sm-2 control-label">Classification</label>
				<div class="col-sm-4">
					<select class="form-control select2 input-sm" gpid="post-classification">
						<option>Top Secret</option>
						<option>Secret</option>
						<option>Confidential</option>
						<option>Unqualified</option>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">Files <span class="label label-primary">5</span></label>
				<div class="col-sm-10">
					<div>
						<select gpid="files-selector" class="form-control select2" data-placeholder="Select a File" style="width: 80%;">
						</select>
						<button type="button" class="btn btn-sm btn-default">
							<i class="fa fa-envelope-o"></i>
						</button>
						<button type="button" gpid="show-more-btn" class="btn btn-sm btn-default">
							<i class="fa fa-align-left"></i>
						</button>
					</div>
					<ul id="file-list" class="list-group m-t-xs m-b-none hidden">
						<li class="list-group-item p-xxs">Free Domain Name Registration <a class="pull-right" role="button"><i class="fa fa-times"></i></a></li>
						<li class="list-group-item p-xxs">Free Window Space hosting</li>
						<li class="list-group-item p-xxs">Number of Images</li>
						<li class="list-group-item p-xxs">24*7 support</li>
						<li class="list-group-item p-xxs">Renewal cost per year</li>
					</ul>
				</div>
			</div>
			<form id="post-form">
				<input name="wgroup_id" type="hidden">
				<input name="content" type="hidden">
				<input name="subject" type="hidden">
				<input name="scope" type="hidden">
				<input name="commentOn" type="hidden">
				<input name="type" type="hidden">
				<input name="priority" type="hidden">
				<input name="classification" type="hidden">
				<input name="attendees" type="hidden">
				<input name="attachments" type="hidden">
			</form>
		</div>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		<button gpid="post-save-btn" type="button" class="btn btn-primary">Save</button>
	  </div>
	</div>
  </div>
</div><!-- new post modal -->
<script src="${path_plugins}/summernote/summernote.js"></script>
<script type="text/javascript">
$(function (){

	"use strict";
	/*
	 * Common dialog to new post
	 */	
	var $modal = $('#new-post-modal');
	var NewPostModal = {

		$wgroup_id : $('#meta-wgroup-id'), // this work group id is from meta-sum-info.jsp
		$post_subject : $('#post-subject', $modal),
		$post_pub_rdo : $('input[gpid="post-square"]', $modal),
		$post_pri_rdo : $('input[gpid="post-private"]', $modal),
		$post_content : $('div[gpid="post-content"]',$modal),
		$post_attendee : $('select[gpid="post-attendee"]', $modal),
		$post_comment : $('input[gpid="post-comment"]', $modal),
		$post_priority : $('select[gpid="post-priority"]', $modal),
		$post_classification : $('select[gpid="post-classification"]', $modal),
		$post_file_list : $('#file-list', $modal),
		$post_file_more : $('button[gpid="show-more-btn"]', $modal),
		$post_file_sel : $('select[gpid="files-selector"]', $modal),
		$save_btn : $('button[gpid="post-save-btn"]', $modal),
		
		/** summernote editor toolbar */
		_snote_bar : [
			//['style', ['style']],
			['font', ['bold', 'italic',  'underline', 'clear']],
			//['fontsize', ['fontsize']],
			//['fontname', ['fontname']],
			['para', ['ul', 'ol', 'paragraph']],
			['insert', ['picture']]
			//['view', ['fullscreen', 'codeview']]
			
		],
		initial : function(){
			var _self = this;
			
			_self.$post_content.summernote({
				"height" : 200,
				"focus" : true,
				"toolbar" : _self._snote_bar
			});
			// apply uniform style
			$('input[type=radio], input[type=checkbox]', $modal).uniform();
			
			_self.$post_pub_rdo.on('click', function(){
				_self.$post_comment.prop('disabled', false);
				$.uniform.update(_self.$post_comment);
				_self.$post_attendee.val(null).trigger("change"); 
				_self.$post_attendee.prop('disabled', true);
				
			});
			
			_self.$post_pri_rdo.on('click', function(){
				_self.$post_comment.prop('disabled', true);
				$.uniform.update(_self.$post_comment);
				_self.$post_attendee.prop('disabled', false);
			});

			_self.$post_priority.select2({
				minimumResultsForSearch: -1,
				dropdownParent: $modal
			});

			_self.$post_classification.select2({
				minimumResultsForSearch: -1,
				dropdownParent: $modal
			});

			_self.$post_attendee.select2({
				ajax: {
					url: "../common/user-list.do",
					dataType: 'json',
					delay: 250,
					data: function (params) {
					  return {
						"user_name": params.term
					  };
					},
					processResults: function (data, params) {
				  		var _result = new Array();
					   	for(var i = 0; i < data.data.length; i++){
							var item = {};
							item.id= data.data[i].account;
						    item.text = data.data[i].name;
							_result.push(item);
					   	}
					  	return {
							results: _result
					  	};
					},
					cache: true
			  	},
			  	minimumInputLength: 0,
			  	placeholder: { id: "", text : "Select attendee"},
				dropdownParent: $modal
			});
				
			_self.$post_file_sel.select2({
				ajax: {
					url: "../common/workgroup-files.do",
					dataType: 'json',
					delay: 250,
					data: function (params) {
					  return {
					  	"wgroup_id" : 2,
						"file_name": params.term,
						"pageSize" : 10
					  };
					},
					processResults: function (data, params) {
						params.page = params.page || 1;
				  		var _result = new Array();
					   	for(var i = 0; i < data.data.length; i++){
						   _result[i].id= data.data[i].account;
						   _result[i].text = data.data[i].name;
					   	}
					  	return {
							results: _result,
							pagination: {
								more: (params.page * 10) < data.total_count
							}
					  	};
					},
					cache: true
			  	},
			  	minimumInputLength: 0,
			  	placeholder: { id: "", text : "Select Work group file"},
				dropdownParent: $modal
			});

			_self.$post_file_more.on('click', function(){
				_self.$post_file_list.toggleClass('hidden');
			});

			_self.$save_btn.on('click', $.proxy(_self.savePost, _self));
		}
	};
	
	NewPostModal.savePost = function(){
		var _self = this, _scope = "";

		if(_self.$post_pri_rdo.prop("checked")){
			_scope = "WORKGROUP";
		}else{
			_scope = "SQUARE";
		}

		var attendees = _self.$post_attendee.val();

		$('#post-form input[name="wgroup_id"]').val(_self.$wgroup_id.val());
		$('#post-form input[name="content"]').val(_self.$post_content.summernote('code'));
		$('#post-form input[name="subject"]').val(_self.$post_subject.val());
		$('#post-form input[name="scope"]').val(_scope);
		$('#post-form input[name="commentOn"]').val(_self.$post_comment.prop('checked'));
		$('#post-form input[name="type"]').val('DISCUSSION');
		$('#post-form input[name="priority"]').val(_self.$post_priority.val());
		$('#post-form input[name="classification"]').val(_self.$post_classification.val());
		$('#post-form input[name="attendees"]').val(attendees == null ? '' : attendees.join(','));
		$('#post-form input[name="attachments"]').val('');
		/*
		 * 1- Here if not use FormData to wrap&submit, the data will be form-url-encoded, coz content might
		 * include image, it collides with firebug tool :request size limit has been reached by Firebug.
		 * 2- contentType : false is OK, reversely 'multipart/form-data' will cause the boundary information
		 * lost in header.
		 */
		var postFromData = new FormData($('#post-form')[0]);
		$.ajax({
			url: "../workgroup/post-save.do",
            dataType : "json",
            type: 'POST',
			cache: false,
			contentType: false,
			processData: false,
			data : postFromData,
			success : function(response){

				GPContext.AppendResult(response, (response.state == "success") ? false : true);
				$modal.modal('hide');
			}
		});
	};

	/*
	 * show select user dialog
	 */
	NewPostModal.newPostShow = function(_callback, _cabinetId){

		var _self = this;
		_self.callback = _callback;
		_self.cabinetId = _cabinetId;
		$modal.modal('show');
	};

	NewPostModal.initial();

	GPContext.showNewPost = $.proxy(NewPostModal.newPostShow, NewPostModal);
});
</script>