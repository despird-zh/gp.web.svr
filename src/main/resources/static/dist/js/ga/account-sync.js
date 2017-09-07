;
var PageContext =(function ($, window, undefined){
	
	"use strict";
	
	var SyncList = {
		
		$search_dt_range : $('#tab_1 input[gpid="search-dt-range"]'),
		$table : $('#tab_1 table[gpid="sync-list"]'),
		
		initial : function(){
			
			var _self = this;
			_self.$search_dt_range.daterangepicker();
			
			_self.initDataTables();
		}
	};
	
	SyncList.initDataTables = function(){
		
		var _self = this;
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
				{"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.SaveOption(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-save"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"columns" : [
				{ data : 'remote' },
				{ data : 'type'},
				{ data : 'state'},
				{ data : 'remark'},
				{ data : 'time'},
				{ data : 'syncid'}
			]
        });
	};
	
	SyncList.initial();
	
	var SyncSetting = {
		
		$table : $('#tab_2 table[gpid="mapping-table"]'),
		
		initial : function(){
			
			var _self = this;
			
			_self.initDataTables();
			
		}
	};
	
	SyncSetting.initDataTables = function(){
		
		var _self = this;
		_self.$table.dataTable({
			"dom": "<'table-scrollable'tr><'row'<'col-md-5 col-sm-5'i><'col-md-7 col-sm-7'p>>",
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
            "pageLength": 10,            
            //"pagingType": "bootstrap_full_number",
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":2,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.SaveOption(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-save"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"columns" : [
				{ data : 'attr' },
				{ data : 'value'},
				{ data : 'syncid'}
			]
        });
	};
	
	SyncSetting.initial();
	
	/*
	 * expose the save opertion 
	 */
	return {
		SaveOption : function(e){BaseSystemSetting.saveOptionValue(e);}
	}
})(jQuery, window);