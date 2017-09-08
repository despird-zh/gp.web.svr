<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<!-- Cropping modal -->
<div class="modal fade" id="avatar-cropper-modal" aria-hidden="true" aria-labelledby="avatar-modal-label" role="dialog" tabindex="-1">
  <div class="modal-dialog" style="width:650px;">
	<div class="modal-content">
	  <form class="avatar-form" action="../avatar" enctype="multipart/form-data" method="post">
		<div class="modal-header">
		  <button type="button" class="close" data-dismiss="modal">&times;</button>
		  <h4 class="modal-title" id="avatar-modal-label">Change Avatar</h4>
		</div>
		<div class="modal-body">
		  <div class="avatar-body">
			<!-- Upload image and data -->
			<div class="avatar-upload">
				<input type="hidden" class="avatar-src" name="avatar_src">
				<input type="hidden" class="avatar-data" name="avatar_data">
				<input type="hidden" class="avatar-oper" name="avatar_oper">
				<input type="file" class="avatar-input" title="Select file to upload" id="avatarInput" name="avatar_file">
			</div>

			<!-- Crop and preview -->
			<div class="row">
			  <div class="col-md-9 p-xxs">
				<div class="avatar-wrapper"></div>
			  </div>
			  <div class="col-md-3 p-xxs">
				<div class="avatar-preview preview-lg"></div>
				<div class="avatar-preview preview-md"></div>
				<div class="avatar-preview preview-sm"></div>
			  </div>
			</div>

			<div class="row avatar-btns">
			  <div class="col-md-10">
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
			  <div class="col-md-2">
				<button type="submit" class="btn btn-primary btn-block avatar-save">Done</button>
			  </div>
			</div>
		  </div>
		</div>
		<!-- <div class="modal-footer">
		  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		</div> -->
	  </form>
	</div>
  </div>
</div><!-- /.modal -->
<script type="text/javascript">
var CropperContext = (function ($, window, undefined){
	
	'use strict';
	
	var AvatarCropper = {

	    $avatarModal: $('#avatar-cropper-modal'),
	    //$avatarView: $('.avatar-view'),
	    $avatar : $('.avatar-view > img'),
	    $loading: $('#avatar-cropper-modal .loading'),

	    $avatarForm: $('#avatar-cropper-modal .avatar-form'),
	    $avatarUpload: $('#avatar-cropper-modal .avatar-upload'),
	    $avatarSrc: $('#avatar-cropper-modal .avatar-src'),
	    $avatarData: $('#avatar-cropper-modal .avatar-data'),
	    $avatarInput: $('#avatar-cropper-modal .avatar-input'),
	    $avatarSave: $('#avatar-cropper-modal .avatar-save'),
	    $avatarBtns: $('#avatar-cropper-modal .avatar-btns'),
		
	    $avatarWrapper: $('#avatar-cropper-modal .avatar-wrapper'),
	    $avatarPreview: $('#avatar-cropper-modal .avatar-preview'),

	    support: {
	        fileList: !! $('<input type="file">').prop('files'),
	        blobURLs: !! window.URL && URL.createObjectURL,
	        formData: !! window.FormData
	    },

	    initial: function () {

	        var _self = this;
	        _self.support.datauri = _self.support.fileList && _self.support.blobURLs;

	        if (!_self.support.formData) {
	            _self.initIframe();
	        }

	        _self.$avatarModal.modal({
	            show: false
	        });

	        _self.$avatarInput.on('change', $.proxy(_self.change, _self));

	        _self.$avatarForm.on('submit', $.proxy(_self.submit, _self));
	        _self.$avatarBtns.on('click', $.proxy(_self.rotate, _self));
			
			_self.$avatarInput.FileInput();
	    }
	};

	
	AvatarCropper.initIframe = function () {

	    var target = 'upload-iframe-' + (new Date()).getTime();
	    var $iframe = $('<iframe>').attr({
	        name: target,
	        src: ''
	    });
	    var _self = this;

	    // Ready ifrmae
	    $iframe.one('load', function () {

	        // respond response
	        $iframe.on('load', function () {
	            var data;

	            try {
	                data = $(this).contents().find('body').text();
	            } catch (e) {
	                console.log(e.message);
	            }

	            if (data) {
	                try {
	                    data = $.parseJSON(data);
	                } catch (e) {
	                    console.log(e.message);
	                }

	                AvatarCropper.submitDone(data);
	            } else {
	                AvatarCropper.submitFail('Image upload failed!');
	            }

	            AvatarCropper.submitEnd();

	        });
	    });

	    _self.$iframe = $iframe;
	    _self.$avatarForm.attr('target', target).after($iframe.hide());
	};

	AvatarCropper.change = function () {
	    var files;
	    var file;
	    var _self = this;
	    if (_self.support.datauri) {
	        files = _self.$avatarInput.prop('files');

	        if (files.length > 0) {
	            file = files[0];

	            if (_self.isImageFile(file)) {
	                if (this.url) {
	                    URL.revokeObjectURL(_self.url); // Revoke the old one
	                }

	                _self.url = URL.createObjectURL(file);
	                _self.startCropper();
	            }
	        }
	    } else {
	        file = _self.$avatarInput.val();

	        if (_self.isImageFile(file)) {
	            _self.syncUpload();
	        }
	    }
	};


	AvatarCropper.submit = function () {
	    var _self = this;
	    if (!_self.$avatarSrc.val() && !_self.$avatarInput.val()) {
	        return false;
	    }

	    if (_self.support.formData) {
	        _self.ajaxUpload();
	        return false;
	    }
	};

	AvatarCropper.rotate = function (e) {
	    var data;
	    var _self = this;
	    if (_self.active) {
	        data = $(e.target).data();

	        if (data.method) {
	            _self.$img.cropper(data.method, data.option);
	        }
	    }
	};


	AvatarCropper.isImageFile = function (file) {
	    if (file.type) {
	        return /^image\/\w+$/.test(file.type);
	    } else {
	        return /\.(jpg|jpeg|png|gif)$/.test(file);
	    }
	};

	AvatarCropper.startCropper = function () {
	    var _self = this;

	    if (_self.active) {
	        _self.$img.cropper('replace', _self.url);
	    } else {
	        _self.$img = $('<img src="' + _self.url + '">');
	        _self.$avatarWrapper.empty().html(_self.$img);
	        _self.$img.cropper({
	            aspectRatio: 1,
	            preview: this.$avatarPreview.selector,
	            crop: function (e) {
	                var json = [
	                        '{"x":' + e.x,
	                        '"y":' + e.y,
	                        '"height":' + e.height,
	                        '"width":' + e.width,
	                        '"rotate":' + e.rotate + '}'
	                ].join();

	                _self.$avatarData.val(json);
	            }
	        });

	        _self.active = true;
	    }

	    _self.$avatarModal.one('hidden.bs.modal', function () {
	        _self.$avatarPreview.empty();
	        _self.stopCropper();
	    });
	};

	AvatarCropper.stopCropper = function () {
	    var _self = this;
	    if (_self.active) {
	        _self.$img.cropper('destroy');
	        _self.$img.remove();
	        _self.active = false;
	    }
	};

	AvatarCropper.ajaxUpload = function () {
	    var _self = this;
	    var url = _self.$avatarForm.attr('action');
	    var data = new FormData(_self.$avatarForm[0]);

	    $.ajax(url, {
	        type: 'post',
	        data: data,
	        dataType: 'json',
	        processData: false,
	        contentType: false,

	        beforeSend: function () {
	            _self.submitStart();
	        },

	        success: function (data) {
	            _self.submitDone(data);
	        },

	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            _self.submitFail(textStatus || errorThrown);
	        },

	        complete: function () {
	            _self.submitEnd();
	        }
	    });
	};

	AvatarCropper.syncUpload = function () {
	    var _self = this;
	    _self.$avatarSave.click();
	};

	AvatarCropper.submitStart = function () {
	    var _self = this;
	    _self.$loading.fadeIn();
	};

	AvatarCropper.submitDone = function (data) {
	   
	    var _self = this;
	    if ($.isPlainObject(data) && data.state == 200) {
	        if (data.data) {
	            _self.url = data.data;

	            if (_self.support.datauri || _self.uploaded) {
	                _self.uploaded = false;
	                _self.cropDone();
	            } else {
	                _self.uploaded = true;
	                _self.$avatarSrc.val(_self.url);
	                _self.startCropper();
	            }

	            _self.$avatarInput.val('');
	        } else if (data.message) {
	            _self.alert(data.message);
	        }
	    } else {
	        _self.alert('Failed to response');
	    }
	};

	AvatarCropper.submitFail = function (msg) {
	    var _self = this;
	    _self.alert(msg);
	};

	AvatarCropper.submitEnd = function () {
	    var _self = this;
	    _self.$loading.fadeOut();
	};

	AvatarCropper.cropDone = function () {
	    var _self = this;
	    _self.$avatarForm.get(0).reset();
	    _self.$avatar.attr('src', _self.url);
	    _self.stopCropper();
	    _self.$avatarModal.modal('hide');
	};

	AvatarCropper.alert = function (msg) {
	    var $alert = [
	            '<div class="alert alert-danger avatar-alert alert-dismissable">',
	            '<button type="button" class="close" data-dismiss="alert">&times;</button>',
	            msg,
	            '</div>'
	    ].join('');
	    var _self = this;
	    _self.$avatarUpload.after($alert);
	};

	/*
	 * send the image element of original as parameter, it should be a JQuery object
	 */
	AvatarCropper.initPreview = function (_src_img_el) {
	    var _self = this;
		// force the original to be jQuery object.
		if(_src_img_el instanceof jQuery){
			_self.$avatar = _src_img_el;
		}else{
			_self.$avatar = $(_src_img_el);
		}
		_self.$avatarModal.modal('show');
	    var url = _self.$avatar.attr('src');
	    _self.$avatarPreview.html('<img src="' + url + '">');
	};

	AvatarCropper.initial();
	
	GPContext.showChangeAvatar = $.proxy( AvatarCropper.initPreview, AvatarCropper);
	
})(jQuery, window);
</script>