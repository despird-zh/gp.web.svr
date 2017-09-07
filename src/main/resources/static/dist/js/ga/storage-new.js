var PageContext = (function ($, AdminLTE) {
	"use strict";

	/*
	 * declare the storage item js wrapper.
	 */
	var StorageNew = {
		
		$name : $('#new-storage-name'),
		$type : $('#new-storage-type'),
		$capacity : $('#new-capacity'),
		$state : $('#new-state'),
		$description : $('#new-description'),
		$store_path : $('#new-storepath'),
		$hdfs_host : $('#new-hdfs-host'),
		$hdfs_port : $('#new-hdfs-port'),
		
		$hdfs_marker : $('#tab_3 div[gpid="hdfs-marker"]'),		
		$store_setting : $('#tab_3 p[gpid="new-store-setting"]'),
		
		$save_btn : $('#tab_3 button[gpid="new-save-btn"]'),

		initial : function(){
			var _self = this;
			
			_self.$save_btn.bind('click', $.proxy(_self.saveStorage, _self));
			
			_self.$type.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'150px'
			});
			
			_self.$type.on('change', function(e){
				var _$self = $(this);
				var _type = _$self.val();
				if('DISK' == _type){
					StorageNew.$store_setting.html('Hard disk store setting');
					StorageNew.$hdfs_marker.addClass('hidden');
				}else if('HDFS' == _type){
					StorageNew.$store_setting.html('HDFS store setting');
					StorageNew.$hdfs_marker.removeClass('hidden');
				}
			});
			
			_self.$state.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'150px'
			});

		}
	};
	
	StorageNew.saveStorage = function (){
		
		var _self = this;
		var storagedata = {
			name : _self.$name.val(),
			type : _self.$type.val(),
			capacity : _self.$capacity.val(),
			used : '0',
			state : _self.$state.val(),
			description : _self.$description.val(),
			storePath : _self.$store_path.val(),
			hdfsHost : _self.$hdfs_host.val(),
			hdfsPort : _self.$hdfs_port.val()
		};
		
		$.ajax({
			url: "../ga/storage-add.do",
			dataType : "json",
			method : "POST",
			data: storagedata, // 
			success: function(response)
			{	
				if('success' == response.state){
					_self.clear();
				}
				GPContext.AppendResult(response, ('success' != response.state));
			}
		});
	}
	
	StorageNew.clear = function(){
		var _self = this;
		_self.$name.val('');		
		_self.$type.val('DISK');
		_self.$type.trigger('change'); // Notify only Select2 of changes		
		_self.$capacity.val('0');
		_self.$state.val('OPEN');					
		_self.$state.trigger('change'); // Notify only Select2 of changes		
		_self.$description.val('');
		_self.$store_path.val(''),
		_self.$hdfs_host.val(''),
		_self.$hdfs_port.val('')
	};
	
	StorageNew.initial();
	/*
	 * expose the necessary functions
	 */
	return {

	};
	
})(jQuery, $.AdminLTE);