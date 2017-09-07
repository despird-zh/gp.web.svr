;
var PageContext =(function ($, window, undefined){
	"use strict";
	
	var BaseSystemSetting = {
	
		$group_select : $('#tab_1 select[gpid=basic-setting-group]'),
		$search_btn : $('#tab_1 a[gpid="basic-search-btn"]'), 
		$clear_btn : $('#tab_1 a[gpid="basic-clear-btn"]'), 

		$table : $('#tab_1 table[gpid="basic-table"]'),  
		
		initial : function(){
			var _self = this;
			
			_self.$group_select.select2({
				minimumResultsForSearch: -1, //hide the search box
				ajax: {
					url: "../ga/sys-option-groups.do",
					dataType: 'json',
					delay: 250,
					data: {},
					processResults: function (resp_data, params) {
					  // parse the results into the format expected by Select2
					  // since we are using custom formatting functions we do not need to
					  // alter the remote JSON data, except to indicate that infinite
					  // scrolling can be used
					  params.page = params.page || 1;
					   
					   for(var i = 0; i < resp_data.data.length; i++){
						   resp_data.data[i].id= resp_data.data[i].key;
						   resp_data.data[i].text = resp_data.data[i].value;
					   }
					  return {
						results: resp_data.data,
						pagination: {
						  more: false
						}
					  };
					},
					cache: true
				},
			    placeholder: { id: "", text : "Select a Option Group"},
			    width : 170
			});			
			// bind search button
			_self.$search_btn.bind('click', function(){
			
				BaseSystemSetting.search();
			});
			// bind clear button
			_self.$clear_btn.bind('click', function(){
				BaseSystemSetting.clear();
			});
			_self.initDataTables();
		}
	};
	
	BaseSystemSetting.initDataTables = function(){
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
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[2,3] ,
				 'searchable': false,
				 'orderable': false
				},
				{"targets":4,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.SaveOption(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-save"></i></button>' +
					 '</div>';
				 }
				},{
					"targets":2,
					 'searchable': false,
					 'orderable': false,
					 'className' : 'no-padding',
					 'render': function (data, type, full, meta){
						 return '<input class="form-control input-md" data-opt-key="'+ full.option +'" value="'+data+'" style="width:150px;" placeholder="setting value" />';
					 }
				
				}
            ],
			"columns" : [
				{ data : 'group' , width : 150},
				{ data : 'option', width : 250},
				{ data : 'value', width : 200},
				{ data : 'description'},
				{ data : 'option', width : 30}
			]
        });
	};
	
	/*
	 * search the system options
	 */
	BaseSystemSetting.search = function(){
	
		var _self = this;
		var _group = _self.$group_select.val();
		$.ajax({
			url: "../ga/sys-option-search.do",
			dataType : "json",
			data: { 
					opt_group : _group
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();
				
			}
		});
	};
	/*
	 * clear the table search result 
	 */
	BaseSystemSetting.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	/*
	 * Save the system option value
	 */
	BaseSystemSetting.saveOptionValue = function(e){
		var _$self = $(e);
		var _opt_key = _$self.attr('data-opt-key');
		var _opt_value = $('input[data-opt-key=' + _opt_key + ']').val();
		$.ajax({
			url: "../ga/sys-option-save.do",
			dataType : "json",
			data: { 
					option_key : _opt_key,
					option_value : _opt_value
				},
			success: function(response)
			{	
				// show message in header
				GPContext.AppendResult(response, false);
			}
		});
	};
	
	BaseSystemSetting.initial();
	
	/*
	 * expose the save opertion 
	 */
	return {
		SaveOption : function(e){BaseSystemSetting.saveOptionValue(e);}
	}
})(jQuery, window);