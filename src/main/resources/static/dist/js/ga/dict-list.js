;
var PageContext =(function ($, window, undefined){
	"use strict";
	
	var DictList = {
	
		$group_select : $('#tab_1 select[gpid="search-group"]'),
		$lang_select : $('#tab_1 select[gpid="search-lang"]'),
		
		$search_btn : $('#tab_1 a[gpid="dict-search-btn"]'), 
		$clear_btn : $('#tab_1 a[gpid="dict-clear-btn"]'), 		

		$table : $('#tab_1 table[gpid="dict-table"]'),  
		
		$detail_group : $('#tab_1 p[gpid="entry-group"]'),
		$detail_entry_key : $('#tab_1 p[gpid="entry-key"]'),
		$detail_entry_val : $('#entry-value'),
		$detail_lable: $('#entry-label'),
		$detail_entry_id: $('#entry-id'),
		$detail_language : $('#tab_1 p[gpid="entry-language"]'),
		$entry_save_btn : $('#tab_1 a[gpid="entry-save-btn"]'),
		
		initial : function(){
			var _self = this;
			
			_self.$group_select.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : 100
			});
			_self.$lang_select.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : 100
			});		
			// bind search button
			_self.$search_btn.on('click', $.proxy( _self.search , _self));
			// bind clear button			
			_self.$clear_btn.on('click', $.proxy( _self.clear , _self));
			
			_self.$entry_save_btn.on('click', $.proxy(_self.saveEntry, _self));
			_self.initDataTables();
		}
	};
	
	DictList.initDataTables = function(){
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
				{"targets":[5] ,
				 'searchable': false,
				 'orderable': false
				},				
				{"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.ShowDetail(this)" data-opt-key="'+full.option+'" type="button"><i class="fa fa-edit"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"columns" : [
				{ data : 'groupKey'},
				{ data : 'entryKey'},
				{ data : 'entryValue' },
				{ data : 'language'},
				{ data : 'label'},
				{ data : 'entryId', width : 30}
			],
			"initComplete" : function(){
				console.log(_self.$table.find('div.dataTables_length'));
				_self.$table.find('div.dataTables_length select').select2(
				{
					minimumResultsForSearch: -1 //hide the search bo		
				});
			}
        });
	};
	
	/*
	 * search the system options
	 */
	DictList.search = function(){
	
		var _self = this;
		$.ajax({
			url: "../ga/dict-search.do",
			dataType : "json",
			method : "POST",
			data: { 
					"group" : _self.$group_select.val(),
					"language" : _self.$lang_select.val()
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();
				
			}
		});
	};
	
	DictList.showDetail = function(_btn){
		var _self = this;
		var rowdata = _self.$table.dataTable().api().row($(_btn).parents('tr')[0]).data();
		// use[0] to access the image dom element
		_self.$detail_entry_id.val(rowdata.entryId);
		_self.$detail_group.html(rowdata.groupKey);
		_self.$detail_entry_key.html(rowdata.entryKey);
		_self.$detail_entry_val.val(rowdata.entryValue);
		_self.$detail_lable.val(rowdata.label);
		_self.$detail_language.html(rowdata.language);
	};
	
	DictList.saveEntry = function(){
		
		var _self = this;
		$.ajax({
			url: "../ga/dict-save.do",
			dataType : "json",
			method : "POST",
			data: { 
					"entryId" : _self.$detail_entry_id.val(),
					"groupKey" : _self.$detail_group.html(),
					"entryKey" : _self.$detail_entry_key.html(),
					"entryValue" : _self.$detail_entry_val.val(),
					"label" : _self.$detail_lable.val(),
					"language" : _self.$detail_language.html()
				},
			success: function(response)
			{	
				// show detail message
				GPContext.AppendResult(response, true);
				
			}
		});
	};
	/*
	 * clear the table search result 
	 */
	DictList.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	DictList.initial();
	
	/*
	 * expose the save opertion 
	 */
	return {
		ShowDetail : $.proxy(DictList.showDetail, DictList)
	}
})(jQuery, window);