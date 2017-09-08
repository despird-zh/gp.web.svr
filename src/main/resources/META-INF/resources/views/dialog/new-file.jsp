<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<style>
.fileinput-button {
    overflow: hidden;
    position: relative;
}
.fileinput-button input {
    cursor: pointer;
    direction: ltr;
    font-size: 200px;
    margin: 0;
    opacity: 0;
    position: absolute;
    right: 0;
    top: 0;
}
</style>
<div class="modal fade" id="new-file-modal" tabindex="-1" role="dialog" aria-labelledby="new-file-modal-label"><!-- clipboard modal -->
  <div class="modal-dialog" role="document" style="width:650px;">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="select-user-modal-label">New File</h4>
	  </div>
	  <div class="modal-body clearfix">
		<div class="row">
			<div class="col-md-6">		
				<div class="box-body no-padding m-b-xs">		
					<table class="table table-bordered table-condensed">
					<tbody>
						<tr>
							<th style="width: 80%">File Name</th>
							<th style="width: 20%">Act.</th>
						</tr>
					</tbody>			
					</table>	
				  <div gpid="upload-files-wrapper" class="no-margin no-padding" style="min-height:237px;">
					  <table  gpid="upload-files" class="table table-condensed table-bordered table-ellipsis" style="table-layout: fixed;margin-bottom:0px;">
						<tbody>
							<tr gpid="blank-row"><td colspan="2">No results ... </td></tr>
						</tbody>
					  </table>
				  </div>			
				</div>	<!--./box-body-->
			</div><!--./col-md-5-->
			<div class="col-md-6">	
				<form class="form-horizontal">
					<div class="form-group">
					  <label for="item-storage-name" class="pull-left m-r-sm m-l-md" >File Detail:</label>
					  <button type="button" gpid="update-file-item-btn" class="btn btn-xs btn-primary pull-left" ><i class="fa fa-fw fa-save"></i></button>
					</div>
					<div class="form-group">
					  <label for="item-storage-name" class="col-sm-3 control-label">Name</label>
					  <div class="col-sm-8">
						<input type="text" class="form-control" id="file-dtl-name" placeholder="file name">					
					  </div>
					</div>
					<div class="form-group">
					  <label for="item-capacity" class="col-sm-3 control-label">Size</label>
					  <div class="col-sm-7">
						<div class="input-group">
							<input type="text" class="form-control" id="file-dtl-size" placeholder="file size">
							<span class="input-group-addon">Bytes</span>
						  </div>
					  </div>
					</div>
					<div class="form-group">
					  <label for="item-used" class="col-sm-3 control-label">Type</label>
					  <div class="col-sm-5">
						<input type="text" class="form-control" id="file-dtl-type" placeholder="doc type">
					  </div>
					</div>
					<div class="form-group">
					  <label for="item-description" class="col-sm-3 control-label">Description</label>
					  <div class="col-sm-8">
						<textarea placeholder="Enter ..." rows="2" id="file-dtl-description" class="form-control"></textarea>
					  </div>
					</div>
				</form>
			</div>
			
			<div class="col-md-12 m-b-n-sm">	
				<div class="progress sm m-b-xs">
					<div gpid="probress-bar" style="width: 0%" class="progress-bar progress-bar-blue progress-bar-striped"></div>
				</div>	
			</div>		
		</div>
	  </div>
	  <div class="modal-footer">
		<span class="btn btn-success fileinput-button pull-left">
			<i class="glyphicon glyphicon-plus"></i>
			<span>Select</span>
			<!-- The file input field used as target for the file upload widget -->
			<input gpid="fileupload" type="file" name="files[]" multiple>
		</span>
		<button type="button" gpid="upload-clear-btn" class="btn btn-default pull-left" >Clear</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button gpid="btn_exec" type="button" class="btn btn-primary">start</button>
	  </div>
	</div>
  </div>
</div><!-- clipboard modal -->
<script src="${path_plugins}/jQueryUpload/js/vendor/jquery.ui.widget.js"></script>
<script src="${path_plugins}/jQueryUpload/js/jquery.fileupload.js"></script>
<script src="${path_plugins}/jQueryUpload/js/jquery.iframe-transport.js"></script>
<script id="select-file-template" type="x-tmpl-mustache">
	<tr >
	  <td style="width: 80%;">{{name}}</td>
	  <td style="width: 20%;text-align:center;">
		<div class="btn-group">
		  <button gpid="item-edit-btn" gp-data-file="{{name}}" class="btn btn-default btn-xs"><i class="fa fa-edit"></i></button>
		  <button gpid="item-remove-btn" gp-data-file="{{name}}" class="btn btn-default btn-xs"><i class="fa fa-trash"></i></button>
		</div>
	  </td>
	</tr>
</script><!-- /.template:select-user-list-template -->
<!--script src="${path_plugins}/jQueryUpload/js/jquery.fileupload-ui.js"></script>
<script src="${path_plugins}/jQueryUpload/js/jquery.fileupload-process.js"></script-->
<script type="text/javascript">
$(function (){
	"use strict";
	/*
	 * Common dialogue to select user.
	 */	
	var NewFileModal = {
		
		$newfile_modal : $("#new-file-modal"), 
		$files_tbody : $('#new-file-modal table[gpid="upload-files"] > tbody'),
		$file_upload : $('#new-file-modal input[gpid="fileupload"]'),
		$progress_bar : $('#new-file-modal div[gpid="probress-bar"]'),
		
		_blank_row : '<tr gpid="blank-row"><td colspan="2">No results ... </td></tr>',
		
		$upload_btn : $('#new-file-modal button[gpid="btn_exec"]'),
		$clear_btn : $('#new-file-modal button[gpid="upload-clear-btn"]'),
		
		$file_tmpl : $('#select-file-template'),
		$file_upload_wrapper : $('#new-file-modal div[gpid="upload-files-wrapper"]'),
		$update_file_item : $('#new-file-modal a[gpid="update-file-item-btn"]'),
		
		$file_dtl_name : $('#file-dtl-name'),
		$file_dtl_size : $('#file-dtl-size'),
		$file_dtl_type : $('#file-dtl-type'),
		$file_dtl_description : $('#file-dtl-description'),
		
		filedata_map : {},
		total_size : 0,
		uploaded_size : 0,
		initial : function(){
			
			var _self = this;
			_self.$newfile_modal.modal('hide');
			_self.bindFileSelect();
			// bind upload button
			_self.$upload_btn.on('click', function(){
				
				_self.startUpload();
			});
			// set scroll bar
			_self.$file_upload_wrapper.slimscroll({
			  height: "237px",
			  alwaysVisible: true,
			  size: "4px"
			});
			// bind save file item
			_self.$update_file_item.on('click', function(){
				
				var filename = $(this).attr('gp-data-file-name');
				NewFileModal.saveFileItem(filename);
			});
			
			_self.$clear_btn.on('click', $.proxy(_self.destroyFileSelect , _self));
		}
	};

	NewFileModal.destroyFileSelect = function(e){
		var _self = this;
		e.stopPropagation();
		_self.total_size = 0;
		for (var k in _self.filedata_map) {
			
			delete _self.filedata_map[k];
		}
		_self.$files_tbody.empty().append(_self._blank_row);
		_self.$progress_bar.css(
					'width',
					0 + '%'
				);
	};
	
	NewFileModal.bindFileSelect = function(){
		
		var _self = this;
		
		_self.$file_upload.fileupload({
			dataType: 'json',
			url: '../transfer',
			autoUpload : false,
			maxChunkSize: 1024000, // 1000K			
			add :function (e, data) {
				
				NewFileModal.$files_tbody.find('tr[gpid=blank-row]').remove();
				data.formData = {
							'cabinet-id' : NewFileModal.cabinetId,
							'file-id' : GPContext.GenerateUID(),
							'file-name' : data.files[0].name,
							'file-size' : data.files[0].size,
							'file-type' : '',							
							'file-description' : ''
						};
				NewFileModal.filedata_map[data.files[0].name] = data;
				
				var template = NewFileModal.$file_tmpl.html();
				Mustache.parse(template);
				
				$.each(data.files, function (index, file) {

					var rendered = Mustache.render(template, {'name' : file.name});						
					NewFileModal.$files_tbody.append(rendered);
					NewFileModal.$files_tbody.find('button[gpid="item-edit-btn"]').unbind('click');
					NewFileModal.$files_tbody.find('button[gpid="item-edit-btn"]').bind('click', function(){
						var filename = $(this).attr('gp-data-file');
						NewFileModal.editFileItem(filename);
					});
					NewFileModal.$files_tbody.find('button[gpid="item-remove-btn"]').unbind('click');
					NewFileModal.$files_tbody.find('button[gpid="item-remove-btn"]').bind('click', function(){
						var filename = $(this).attr('gp-data-file'); // get file name
						$(this).parent().parent().parent().remove();// remove tr
						NewFileModal.removeFileItem(filename); // remove by file name
					});
				});
			},
			progressall : function (e, data) {
				console.log(data);
				var progress = parseInt(data.loaded / data.total * 100, 10);
				NewFileModal.$progress_bar.css(
					'width',
					progress + '%'
				);
			}
		});
		
	};
	
	/*
	 * edit file item
	 */
	NewFileModal.editFileItem = function(filename){
		var _self = this;
		_self.$update_file_item.attr('gp-data-file-name', filename);// reserve the file name
		var fileitem = _self.filedata_map[filename];
		_self.$file_dtl_name.val(fileitem.formData['file-name']);
		_self.$file_dtl_size.val(fileitem.formData['file-size']);
		_self.$file_dtl_type.val(fileitem.formData['file-type']);
		_self.$file_dtl_description.val(fileitem.formData['file-description']);
		
	};
	
	NewFileModal.saveFileItem = function(filename){
		var _self = this;
		var fileitem = _self.filedata_map[filename];
		fileitem.formData['file-name'] = _self.$file_dtl_name.val();
		fileitem.formData['file-size'] = _self.$file_dtl_size.val();
		fileitem.formData['file-type'] = _self.$file_dtl_type.val();
		fileitem.formData['file-description'] = _self.$file_dtl_description.val();
	}
	/*
	 * remove file item
	 */
	NewFileModal.removeFileItem = function(filename){
		var _self = this;
		delete _self.filedata_map[filename];
		if(_self.$files_tbody.find('tr').length == 0){
			// append blank row place holder
			_self.$files_tbody.append('<tr gpid="blank-row"><td colspan="2">No results ... </td></tr>');
		}
	};
	
	/*
	 * start to upload files
	 */ 
	NewFileModal.startUpload = function(){
		var _self = this;
		for (var k in _self.filedata_map) {
			_self.total_size = _self.total_size + _self.filedata_map[k].files[0].size;	
			_self.filedata_map[k].submit();
		}
	};
	/*
	 * show select user dialog
	 */
	NewFileModal.newFileShow = function(_callback, _cabinetId){
		
		var _self = this;
		_self.callback = _callback;
		_self.cabinetId = _cabinetId;
		_self.$newfile_modal.modal('show');
	};
	
	NewFileModal.initial();
	
	GPContext.showNewFile = function(callback, cabinetId){
		NewFileModal.newFileShow(callback, cabinetId);
	};

});
</script>