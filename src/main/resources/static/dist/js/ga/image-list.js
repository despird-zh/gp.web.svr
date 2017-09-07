;
var PageContext =(function ($, window, undefined){
	"use strict";
	var $ListTab = $('#tab_1');
	var ImageList = {
	
		$format_select : $('#tab_1 select[gpid="image-format"]'),
		$search_btn : $('#tab_1 a[gpid="image-search-btn"]'), 
		$clear_btn : $('#tab_1 a[gpid="image-clear-btn"]'), 		
		$new_image_btn : $('a[gpid="new-image-btn"]'),
		
		$table : $('#tab_1 table[gpid="image-table"]'),  
		
		$detail_img : $('#tab_1 img[gpid="image-avatar"]'),
		$detail_imgid : $('#image-id'),
		$detail_format : $('#tab_1 p[gpid="image-format"]'),
		$detail_name : $('#image-name'),
		$detail_touch_date : $('#tab_1 p[gpid="image-touch-date"]'),
		$detail_modifier : $('#tab_1 p[gpid="image-modifier"]'),
		$detail_modified_date : $('#tab_1 p[gpid="image-modified-data"]'),
		$detail_save_btn : $('#tab_1 a[gpid="image-save-btn"]'),
		
		initial : function(){
			var _self = this;
			
			_self.$format_select.select2({
				dropdownParent: $ListTab,
				minimumResultsForSearch: -1 //hide the search box			
			});			
			// fix the select2 in tab panel width auto shrink.
			_self.$format_select.next().css("min-width","100px");
			// bind search button
			_self.$search_btn.on('click', $.proxy( _self.search , _self));
			// bind clear button			
			_self.$clear_btn.on('click', $.proxy( _self.clear , _self));
			// open new page
			_self.$new_image_btn.on('click', function(){				
				$('li[gpid="new-tab"]').removeClass('hidden');
				$('li[gpid="new-tab"] > a').trigger('click');
			});
			// open change avatar modal panel
			_self.$detail_img.on('click', function(){
				GPContext.showChangeAvatar($(this));
			});
			// save image detail
			_self.$detail_save_btn.on('click', $.proxy( _self.saveDetail , _self));
			
			_self.initDataTables();
		}
	};
	
	ImageList.initDataTables = function(){
		var _self = this;
        // begin first table
        _self.$table.dataTable({
			
            // Internationalisation. For more info refer to http://datatables.net/manual/i18n
            "language": {
                "aria": {
                    "sortAscending": ": activate to sort column ascending",
                    "sortDescending": ": activate to sort column descending"
                },
                "emptyTable": "No data available in table",
                "info": "Showing _START_ to _END_ of _TOTAL_ records",
                "infoEmpty": "No records found",
                "infoFiltered": "(filtered1 from _MAX_ total records)",
                "lengthMenu": "Show _MENU_",
                "search": "Filter:",
                "zeroRecords": "No matching records found",
                "paginate": {
                    "previous":"Prev",
                    "next": "Next",
                    "last": "Last",
                    "first": "First"
                }
            },
            "bStateSave": true, // save datatable state(pagination, sort, etc) in cookie.
			"autoWidth" : false,
            "lengthMenu": [
                [5, 10, 20, -1],
                [5, 10, 20, "All"] // change per page values here
            ],
            // set the initial value
            "pageLength": 5,            
            //"pagingType": "bootstrap_full_number",
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[0] ,
				 'searchable': false,
				 'orderable': false
				},
				{"targets":1,
				 'searchable': false,
				 'orderable': false,
				 'className' : "avatar-cell",
				 'render': function (data, type, full, meta){
					 return '<div>' +
						'<img alt="Avatar" src="' + data + '" gpid="workgroup-avatar">' +
					 '</div>';
				 }
				},
				{"targets":4,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 if('WGROUP_AVATAR' === data){
						 return 'Workgroup';
					 }else if('USER_AVATAR' === data){
						 return 'User';
					 }else{
						 return data;
					 }
				 }
				},
				{"targets":7,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.ShowDetail(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-info-circle"></i></button>' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveImage(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-close"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"columns" : [
				{ data : 'imageId' , width : 50},
				{ data : 'imageUrl', width : 50},
				{ data : 'imageName', width : 100},
				{ data : 'format', width : 80},
				{ data : 'category' , width : 140},
				{ data : 'modifier'},
				{ data : 'modifyDate' },
				{ data : 'imageId', width : 50}
			]
        });
	};
	
	/*
	 * search the images list
	 */
	ImageList.search = function(){
	
		var _self = this;
		var _format = _self.$format_select.val();
		$.ajax({
			url: "../ga/image-search.do",
			dataType : "json",
			method : "POST",
			data: { 
					"format" : _format
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();
				
			}
		});
	};
	/*
	 * save detail information
	 */
	ImageList.saveDetail = function(){
		var _self = this;
		$.ajax({
			url: "../ga/image-save.do",
			dataType : "json",
			method : "POST",
			data: { 
					"image_id" : _self.$detail_imgid.val(),
					"image_name" : _self.$detail_name.val(),
					"image_src" : _self.$detail_img.attr('src')				
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, false);				
			}
		});
	};
	
	/*
	 * show detail information
	 */
	ImageList.showDetail = function(_btn){
		var _self = this;
		var rowdata = _self.$table.dataTable().api().row($(_btn).closest('tr')).data();
		// use[0] to access the image dom element
		_self.$detail_img.attr('src',rowdata.imageUrl);
		_self.$detail_imgid.val(rowdata.imageId);
		_self.$detail_name.val(rowdata.imageName);
		_self.$detail_format.html(rowdata.format);
		_self.$detail_touch_date.html(rowdata.touchDate);
		_self.$detail_modifier.html(rowdata.modifier);
		_self.$detail_modified_date.html(rowdata.modifyDate);
	};
	
		/*
	 * show detail information
	 */
	ImageList.removeImage = function(_btn){
		var _self = this;
		var rowdata = _self.$table.dataTable().api().row($(_btn).closest('tr')).data();
		$.ajax({
			url: "../ga/image-remove.do",
			dataType : "json",
			method : "POST",
			data: { 
					"image_id" : rowdata.imageId		
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, false);				
			}
		});
	};
	
	/*
	 * clear the table search result 
	 */
	ImageList.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	ImageList.initial();
	
	var $NewTab = $('#tab_2');
	var ImageNew = {

	    $avatarView: $('.avatar-view'),
	    $avatar: $('.avatar-view > img'),
	    $loading: $('#tab_2 .loading'),

	    $avatarForm: $('#tab_2 .avatar-form'),
	    $avatarUpload: $('#tab_2 .avatar-upload'),
	    $avatarSrc: $('#tab_2 .avatar-src'),
		$avatarCate: $('#tab_2 select[gpid="image-category"]'),
	    $avatarData: $('#tab_2 .avatar-data'),
	    $avatarInput: $('#tab_2 .avatar-input'),
	    $avatarSave: $('#tab_2 .avatar-save'),
	    $avatarBtns: $('#tab_2 .avatar-btns'),
		
	    $avatarWrapper: $('#tab_2 .avatar-wrapper'),
	    $avatarPreview: $('#tab_2 .avatar-preview'),
		$close_btn : $('#tab_2 button[gpid="new-close-btn"]'),
		
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

			_self.$close_btn.bind('click', function(){
				
				$('li[gpid="new-tab"]').addClass('hidden');
				$('li[gpid="list-tab"] > a').trigger('click');
				ImageNew.$avatarPreview.empty();
				ImageNew.stopCropper();
				
			});
	        _self.$avatarInput.on('change', $.proxy(_self.change, _self));
			_self.$avatarCate.select2({
				dropdownParent: $NewTab,
				minimumResultsForSearch: -1, //hide the search box	
				width : 170,
			});
	        _self.$avatarForm.on('submit', $.proxy(_self.submit, _self));
	        _self.$avatarBtns.on('click', $.proxy(_self.rotate, _self));
			// initial input element
			_self.$avatarInput.FileInput();
	    }
	};

	ImageNew.initIframe = function () {

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

	                ImageNew.submitDone(data);
	            } else {
	                ImageNew.submitFail('Image upload failed!');
	            }

	            ImageNew.submitEnd();

	        });
	    });

	    _self.$iframe = $iframe;
	    _self.$avatarForm.attr('target', target).after($iframe.hide());
	};

	ImageNew.change = function () {
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


	ImageNew.submit = function () {
	    var _self = this;
	    if (!_self.$avatarSrc.val() && !_self.$avatarInput.val()) {
	        return false;
	    }

	    if (_self.support.formData) {
	        _self.ajaxUpload();
	        return false;
	    }
	};

	ImageNew.rotate = function (e) {
	    var data;
	    var _self = this;
	    if (_self.active) {
	        data = $(e.target).data();

	        if (data.method) {
	            _self.$img.cropper(data.method, data.option);
	        }
	    }
	};


	ImageNew.isImageFile = function (file) {
	    if (file.type) {
	        return /^image\/\w+$/.test(file.type);
	    } else {
	        return /\.(jpg|jpeg|png|gif)$/.test(file);
	    }
	};

	ImageNew.startCropper = function () {
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

	};

	ImageNew.stopCropper = function () {
	    var _self = this;
	    if (_self.active) {
	        _self.$img.cropper('destroy');
	        _self.$img.remove();
	        _self.active = false;
	    }
	};

	ImageNew.ajaxUpload = function () {
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

	        success: function ( _resp ) {
	            _self.submitDone( _resp );
	        },

	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            _self.submitFail(textStatus || errorThrown);
	        },

	        complete: function () {
	            _self.submitEnd();
	        }
	    });
	};

	ImageNew.syncUpload = function () {
	    var _self = this;
	    _self.$avatarSave.click();
	};

	ImageNew.submitStart = function () {
	    var _self = this;
	    _self.$loading.fadeIn();
	};

	ImageNew.submitDone = function (_resp) {
	    
	    var _self = this;
	    if ($.isPlainObject(_resp) && _resp.state == '200') {
	        if (_resp.data) {
	            _self.url = _resp.data;

	            if (_self.support.datauri || _self.uploaded) {
	                _self.uploaded = false;
	                _self.cropDone();
	            } else {
	                _self.uploaded = true;
	                _self.$avatarSrc.val(_self.url);
	                _self.startCropper();
	            }

	            _self.$avatarInput.val('');
	        } else if (_resp.message) {
	            _self.alert(_resp.message);
	        }
	    } else {
	        _self.alert('Failed to response');
	    }
	};

	ImageNew.submitFail = function (msg) {
	    var _self = this;
	    _self.alert(msg);
	};

	ImageNew.submitEnd = function () {
	    var _self = this;
	    _self.$loading.fadeOut();
	};

	ImageNew.cropDone = function () {
	    var _self = this;
	    _self.$avatarForm.get(0).reset();
	    _self.$avatar.attr('src', _self.url);
	    _self.stopCropper();
	};

	ImageNew.alert = function (msg) {
	    var $alert = [
	            '<div class="alert alert-danger avatar-alert alert-dismissable">',
	            '<button type="button" class="close" data-dismiss="alert">&times;</button>',
	            msg,
	            '</div>'
	    ].join('');
	    var _self = this;
	    _self.$avatarUpload.after($alert);
	};


	ImageNew.initPreview = function () {
	    var _self = this;
	    var url = _self.$avatar.attr('src');
	    _self.$avatarPreview.html('<img src="' + url + '">');
	};

	ImageNew.initial();
	/*
	 * expose the save opertion 
	 */
	return {
		ShowDetail : $.proxy( ImageList.showDetail, ImageList),
		RemoveImage : $.proxy( ImageList.removeImage, ImageList)
	}
})(jQuery, window);