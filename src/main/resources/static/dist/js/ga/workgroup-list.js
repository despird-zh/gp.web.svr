var PageContext = (function ($, AdminLTE) {

	"use strict";
	
	var LocalWorkgroups = {
	
		$search_wgroup_name : $('#tab_1 input[gpid="search-local-wname"]'),
		$search_btn : $('#tab_1 a[gpid="search-local-btn"]'),
		$clear_btn : $('#tab_1 a[gpid="search-clear-btn"]'),
		
		$table : $('#tab_1 table[gpid="wgroup-local-table"]'),

		initial : function(){
			
			var _self = this;
			_self.$search_btn.bind('click', function(){
				LocalWorkgroups.search(1);
			});
			
			_self.$clear_btn.bind('click', function(){
				LocalWorkgroups.clear();
			});
			
			_self.initDataTables();
		}
	};

	LocalWorkgroups.initDataTables = function(){
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
				{"targets":7,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadLocal(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadLocal(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'sourceName'},
				{ data : 'workgroupName'},
				{ data : 'state'},
				{ data : 'admin'},
				{ data : 'description'},
				{ data : 'taskOn'},
				{ data : 'createDate'},
				{ data : 'workgroupId'}
			]
			
        });
	};
	
	LocalWorkgroups.search = function(pageindex){
	
		var _self = this;
		var _wname = _self.$search_wgroup_name.val();
		$.ajax({
			url: "../ga/workgroup-local-search.do",
			dataType : "json",
			data: { 
					pageNumber : pageindex,
					pageSize : 10,
					wgroup_name : _wname
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();					  
			}
		});
	};
	
	LocalWorkgroups.loadLocalWorkgroup = function(el){
		var _self = this;
		var $tr = $(el).closest('tr');
		var row_data = _self.$table.dataTable().api().row($tr).data();
		var url = '../ga/workgroup-edit.do?wgroup_id='+row_data.workgroupId;
		window.location.href = url;
	};
	
	LocalWorkgroups.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	LocalWorkgroups.initial();
	
	var MirrorWorkgroups = {
	
		$search_wgroup_name : $('#tab_2 input[gpid="search-mirror-wname"]'),
		$search_btn : $('#tab_2 a[gpid="search-mirror-btn"]'),
		$clear_btn : $('#tab_2 a[gpid="search-clear-btn"]'),
		
		$table : $('#tab_2 table[gpid="wgroup-mirror-table"]'),

		initial : function(){
			var _self = this;
			_self.$search_btn.bind('click', function(){
				MirrorWorkgroups.search(1);
			});
			
			_self.$clear_btn.bind('click', function(){
				MirrorWorkgroups.clear();
			});
			
			_self.initDataTables();
		}
	};
	
	MirrorWorkgroups.initDataTables = function(){
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
				{"targets":[0] ,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return full.entityCode + '/' + full.nodeCode;
				 },
				},				
				{"targets":8,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadWorkgroup(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveWorkgroup(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'nodeCode'},
				{ data : 'sourceName'},
				{ data : 'workgroupName'},
				{ data : 'state'},
				{ data : 'admin'},
				{ data : 'description'},
				{ data : 'taskOn'},
				{ data : 'createDate'},
				{ data : 'workgroupId'}
			]
			
        });
	};
	
	MirrorWorkgroups.search = function(pageindex){
	
		var _self = this;
		var _wname = _self.$search_wgroup_name.val();
		$.ajax({
			url: "../ga/workgroup-mirror-search.do",
			dataType : "json",
			data: { 
					pageNumber : pageindex,
					pageSize : 10,
					wgroup_name : _wname
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();			  
			}
		});
	};
	
	MirrorWorkgroups.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	MirrorWorkgroups.initial();
	
	return {
		
		LoadLocal : $.proxy(LocalWorkgroups.loadLocalWorkgroup, LocalWorkgroups),
		LoadMirror : $.proxy(LocalWorkgroups.loadLocalWorkgroup, LocalWorkgroups)
	};
})(jQuery, $.AdminLTE);