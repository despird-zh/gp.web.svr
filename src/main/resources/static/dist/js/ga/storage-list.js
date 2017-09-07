var PageContext = (function ($, AdminLTE) {
	"use strict";
	var StorageList = {
	
		$storage_name : $('#tab_1 input[gpid=list-search-sname]'),
		$storage_type : $('#tab_1 select[gpid=list-search-type]'),
		$storage_state : $('#tab_1 select[gpid=list-search-state]'),
		$search_btn : $('#tab_1 a[gpid="list-search-btn"]'), 
		$clear_btn : $('#tab_1 a[gpid="list-clear-btn"]'), 

		$table : $('#tab_1 table[gpid="list-table"]'), 
		
		initial : function(){
			var _self = this;
			// bind search button
			_self.$search_btn.bind('click', function(){
			
				StorageList.search();
			});
			_self.$storage_type.select2({
				minimumResultsForSearch: -1, //hide the search box
			});
			_self.$storage_state.select2({
				minimumResultsForSearch: -1, //hide the search box
			});
			// bind clear button
			_self.$clear_btn.bind('click', function(){
				StorageList.clear();
			});

			_self.initDataTables();
		}
	};	
		
	StorageList.initDataTables = function(){
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
                [1, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[0,7] ,
				 'searchable': false,
				 'orderable': false
				},
				{
				"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){					 
					 return '<div class="progress progress-sm">'+
					 ' <div style="width: ' + data + '% " class="progress-bar progress-bar-yellow "></div>'+
					 '</div>';
				 }
				},
				{"targets":8,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-storage-id="'+full.storageId+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadStorage(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-storage-id="'+full.storageId+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveStorage(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'storageId'},
				{ data : 'name'},
				{ data : 'type'},
				{ data : 'capacity'},
				{ data : 'used'},
				{ data : 'percent'},
				{ data : 'state'},
				{ data : 'description'},
				{ data : 'storageId'}
			]
        });
	};
	
	StorageList.search = function(pageindex){
	
		var _self = this;
		var _sname = _self.$storage_name.val();
		$.ajax({
			url: "../ga/storage-search.do",
			dataType : "json",
			data: { 
					storage_name : _sname,
					storage_type : _self.$storage_type.val(),
					storage_state : _self.$storage_state.val()
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();			  
			}
		});
	};
	
	StorageList.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	StorageList.initial();
	
	/*
	 * declare the storage item js wrapper.
	 */
	var StorageEdit = {
		
		$id : $('#edit-storage-id'),
		$name : $('#edit-storage-name'),
		$type : $('#edit-storage-type'),
		$capacity : $('#edit-capacity'),
		$used : $('#edit-used'),
		$state : $('#edit-state'),
		$description : $('#edit-description'),
		$store_path : $('#edit-storepath'),
		$hdfs_host : $('#edit-hdfs-host'),
		$hdfs_port : $('#edit-hdfs-port'),
		
		$hdfs_marker : $('#tab_2 div[gpid="hdfs-marker"]'),
		
		$store_setting : $('#tab_2 p[gpid="edit-store-setting"]'),
		
		$save_btn : $('#tab_2 button[gpid="edit-save-btn"]'),

		initial : function(){
			var _self = this;
			
			_self.$save_btn.bind('click', function(){
				StorageEdit.saveStorage();
			});
			
			_self.$type.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'150px'
			});
			
			_self.$type.on('change', function(e){
				var _$self = $(this);
				var _type = _$self.val();
				if('DISK' == _type){
					StorageEdit.$store_setting.html('Hard disk store setting');
					StorageEdit.$hdfs_marker.addClass('hidden');
				}else if('HDFS' == _type){
					StorageEdit.$store_setting.html('HDFS store setting');
					StorageEdit.$hdfs_marker.removeClass('hidden');
				}
			});
			_self.$state.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'150px'
			});

		}
	};
	
	/* 
	 * save the storage information
     */
	StorageEdit.saveStorage = function (){
		
		var _self = this, _url;
		var storagedata = {
			storageId : _self.$id.val(),
			name : _self.$name.val(),
			type : _self.$type.val(),
			capacity : _self.$capacity.val(),
			used : _self.$used.val(),
			state : _self.$state.val(),
			description : _self.$description.val(),
			storePath : _self.$store_path.val(),
			hdfsHost : _self.$hdfs_host.val(),
			hdfsPort : _self.$hdfs_port.val()
		};
		
		$.ajax({
			url: "../ga/storage-save.do",
			dataType : "json",
			method : "POST",
			data: storagedata, // 
			success: function(response)
			{	
				GPContext.AppendResult(response, ('success' != response.state));
			}
		});
	}
	
	/*
	 * load data from backend system
	 * parameter : sotrage id
	 */
	StorageEdit.loadStorage = function(e){
		var _self = this;
		$('a[gpid="edit-tab"]').tab('show');
		var storageId = $(e).attr('data-storage-id');
		$.ajax({
			url: "../ga/storage-info.do",
			dataType : "json",
			data: {storage_id : storageId}, // 
			success: function(response)
			{	
				var currDate = new Date();
				if(response.state == 'success'){
					_self.$id.val(response.data.storageId);
					_self.$name.val(response.data.name);
					
					_self.$type.val(response.data.type);
					_self.$type.trigger('change'); // Notify only Select2 of changes
					
					_self.$capacity.val(response.data.capacity);
					_self.$used.val(response.data.used);
					
					_self.$state.val(response.data.state);					
					_self.$state.trigger('change'); // Notify only Select2 of changes
					
					_self.$description.val(response.data.description);
					_self.$store_path.val(response.data.storePath);
					_self.$hdfs_host.val(response.data.hdfsHost);
					_self.$hdfs_port.val(response.data.hdfsPort);
				}else{
					// show message.
					GPContext.AppendResult(response, true);
				}
			}
		});
	};
	
	StorageEdit.removeStorage = function(e){
		var storageId = $(e).attr('data-storage-id');
		$.ajax({
			url: "../ga/storage-remove.do",
			dataType : "json",
			method : "POST",
			data: {
				storage_id : storageId
			}, // 
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	StorageEdit.initial();
	
	/*
	 * expose the necessary functions
	 */
	return {
		LoadStorage : function(e){StorageEdit.loadStorage(e);},
		RemoveStorage : function(e){StorageEdit.removeStorage(e);}
	};
	
})(jQuery, $.AdminLTE);