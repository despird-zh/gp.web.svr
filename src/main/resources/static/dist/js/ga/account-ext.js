/*! 
 * Copyright (c) 2014-2015 Gary diao
 */
;var PageContext = (function ($, window, undefined){

	"use strict";
	// declare name space.	
	var AccountExt = {
		
		$search_cond : $('#tab_3 input[gpid="ext-search-cond"]'),
		$table : $('#tab_3 table[gpid="ext-list-table"]'),
		$search_btn : $('#tab_3 a[gpid="ext-search-btn"]'),
		$clear_btn : $('#tab_3 a[gpid="ext-clear-btn"]'),
		
		$ext_entity : $('#tab_3 span[gpid="ext-entity-p"]'),
		$ext_node : $('#tab_3 span[gpid="ext-node-p"]'),
		$ext_entity_name : $('#tab_3 p[gpid="ext-entity-name-p"]'),
		$ext_account : $('#tab_3 p[gpid="ext-account-p"]'),
		$ext_gbl_account : $('#ext-global-account'),
		$ext_name : $('#ext-name'),
		$ext_mobile : $('#tab_3 p[gpid="ext-mobile-p"]'),
		$ext_email : $('#tab_3 p[gpid="ext-email-p"]'),
		
		$close_btn : $('#tab_3 button[gpid="ext-close-btn"]'),
		$save_btn : $('#tab_3 button[gpid="ext-save-btn"]'),
		
		initial : function(){
			
			var _self = this;
			_self.$close_btn.bind('click', function(){				
				$('a[gpid="list-tab"]').tab('show');
				$('a[gpid="ext-tab"]').parents('li').addClass('hidden');
			});
			
			_self.$save_btn.bind('click', function(){				
				AccountExt.saveAccountExt();
			});
			
			_self.$search_btn.bind('click', function(){				
				AccountExt.search();
			});
			
			_self.$clear_btn.bind('click', function(){				
				AccountExt.$table.dataTable().api().clear().draw();
			});
			
			_self.initDataTables();
		}
	};
	
	AccountExt.initDataTables = function(){
		
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
				{"targets":4,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.loadAccountExt(this);"><i class="fa fa-edit"></i></button>'+
					 '</div>';
				 },
				 'width' : 30
				}
            ],

			"columns" : [
				{ data : 'account'},
				{ data : 'name'},
				{ data : 'mobile'},
				{ data : 'email'},
				{ data : 'account'},
			]
			
        });
	};
	
	AccountExt.loadAccountExt = function(e){
		var _self = this;
		var rdata = _self.$table.dataTable().api().row($(e).parents('tr')[0]).data();
		_self.$ext_entity.html(rdata.entity);
		_self.$ext_node.html(rdata.node);
		_self.$ext_entity_name.html(rdata.source);
		_self.$ext_account.html(rdata.account);
		_self.$ext_gbl_account.val(rdata.globalAccount);
		_self.$ext_name.val(rdata.name);
		_self.$ext_mobile.html(rdata.mobile);
		_self.$ext_email.html(rdata.email);
	};
	
	AccountExt.saveAccountExt = function(){
		
		var _self = this;

		$.ajax({
			url: "../ga/account-ext-new.do",
			dataType : "json",
			method : "POST",
			data: {
					entity : _self.$ext_entity.html(),
					node : _self.$ext_node.html(),
					account : _self.$ext_account.html(),
					globalAccount : _self.$ext_gbl_account.val(),
					name : _self.$ext_name.val(),
					email : _self.$ext_email.html(),
					mobile : _self.$ext_mobile.html(),
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);  
			}
		});
	};
	
	AccountExt.search = function(){
		var _self = this;
		var _scond = _self.$search_cond.val();
		$.ajax({
			url: "../ga/account-ext-search.do",
			dataType : "json",
			data: { 
					search_cond : _scond,
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();			  
			}
		});
	};
	
	AccountExt.initial();
		
	return {
		loadAccountExt : $.proxy(AccountExt.loadAccountExt,AccountExt)
	};
})(jQuery, window);
