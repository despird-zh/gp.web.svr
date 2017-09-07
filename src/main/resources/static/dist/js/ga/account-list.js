/*! 
 * Copyright (c) 2014-2015 Gary diao
 */
;var PageContext = (function ($, window, undefined){

	"use strict";
	// declare name space.
	var AccountList = {

		current_page : 1,
		$search_user : $('input[gpid="list-search-user"]'), 
		$search_enode : $('.select2[gpid=list-search-enode]'),
		$search_state : $('.select2[gpid=list-search-state]'),
		$search_type : $('.select2[gpid=list-search-type]'),
		
		$search_btn : $('a[gpid="list-search-btn"]'), 
		$clear_btn : $('a[gpid="list-clear-btn"]'), 
		$table : $('table[gpid="list-table"]'),  
		
		// declare initialize function, to bind event.
		initial : function(){
			var _self = this;
			_self.$search_btn.bind("click",function(event){
				AccountList.search(1);
			});
			_self.$clear_btn.bind("click",function(event){
				AccountList.clear();
			});
			
			_self.$search_type.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'100px'
			});

			_self.$search_state.select2({
				minimumResultsForSearch: -1, //hide the search box
				width:'100px'
			});
			
			// initial select2 element
			_self.$search_enode.select2({
			  ajax: {
				url: "../common/entity-list.do",
				dataType: 'json',
				delay: 250,
				data: function (params) {
				  return {
					instance_name: params.term, // search term
					pageNumber: params.page,
					pageSize : 10,
					all_option : true
				  };
				},
				processResults: function (data, params) {
				  // parse the results into the format expected by Select2
				  // since we are using custom formatting functions we do not need to
				  // alter the remote JSON data, except to indicate that infinite
				  // scrolling can be used
				  params.page = params.page || 1;
				   
				   for(var i = 0; i < data.items.length; i++){
					   data.items[i].id= data.items[i].key;
					   data.items[i].text = data.items[i].value;
				   }
				  return {
					results: data.items,
					pagination: {
					  more: (params.page * 10) < data.total_count
					}
				  };
				},
				cache: true
			  },
			  escapeMarkup: function (markup) { return markup; },
			  minimumInputLength: 0,
			  placeholder: { id: "", text : "Select a entity node"},
			  width : '150px'
			});
			
			_self.initDataTables();
		}
	};

			
	AccountList.initDataTables = function(){
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
				{
				 "targets":[3] ,
				 'searchable': false,
				 'orderable': false,
				 'className': 'dt-body-center',
				 'render': function (data, type, full, meta){
						 if(data == 'INLINE'){
							return '<span class="label label-primary">'+data+'</span>';
						 }else if(data == 'LDAP'){
							return '<span class="label label-success">'+data+'</span>';
						 }else{
							return '<span class="label label-info">'+data+'</span>';
						 }
					 }
				 },		
				{
				 "targets":[4] ,
				 'searchable': false,
				 'orderable': false,
				 'className': 'dt-body-center',
				 'render': function (data, type, full, meta){
						 if(data == 'ACTIVE'){
							return '<span class="label label-primary">'+data+'</span>';
						 }else if(data == 'DEACTIVE'){
							return '<span class="label label-success">'+data+'</span>';
						 }else{
							return '<span class="label label-warning">'+data+'</span>';
						 }
					 }
				 },					 
				{"targets":7,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadAccount(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-user-id="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveAccount(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'account'},
				{ data : 'email'},
				{ data : 'mobile'},
				{ data : 'type'},
				{ data : 'state'},
				{ data : 'sourceName'},
				{ data : 'createDate'},
				{ data : 'userId'}
			]
			
        });
	};
	/*
	 * search the account list 
	 * parameter - pageindex index of expected page.
	 */
	AccountList.search = function(){
		
		var _self = this;
		
		$.ajax({
			url: "../ga/account-search.do",
			dataType : "json",
			data: { 
					uname : _self.$search_user.val(),
					instance_id : _self.$search_enode.val(),
					type : _self.$search_type.val(),
					state : _self.$search_state.val()
				},
			success: function(response)
			{					  
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();				  
			}
		});
	};
	
	/*
	 * clear the content of search result
	 */
	AccountList.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	AccountList.removeAccount = function(e){
	
		var userid = $(e).attr('data-user-id');
		$.ajax({
			url: "../ga/account-delete.do",
			dataType : "json",
			data: {
				user_id : userid
			},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);  
			}
		});
	};
	// run initialization 
	AccountList.initial();	
	
	var AccountEdit = {
		
		$item_uid : $('#edit-uid'),
		$item_account : $('#tab_2 p[gpid="edit-account"]'),
		$item_name : $('#edit-name'),
		$item_type : $('#edit-type'),
		$item_state : $('#edit-state'),
		$item_password : $('#edit-password'),
		$item_confirm : $('#edit-password-confirm'),
		$item_email : $('#edit-email'),
		$item_mobile : $('#edit-mobile'),
		$item_phone : $('#edit-phone'),
		$item_language : $('#edit-language'),
		$item_timezone : $('#edit-timezone'),
		$item_storage_sel : $('#edit-storage-sel'),
		$item_pub_capacity : $('#edit-pub-cap'),
		$item_pri_capacity : $('#edit-pri-cap'),
		
		$item_save_btn : $('a[gpid=edit-save-btn]'),

		// declare initialize function, to bind event.
		initial : function(){
			var _self = this;
			
			_self.$item_save_btn.bind("click", function(evt){			
				AccountEdit.saveAccount();				
			});
			
			_self.$item_type.select2({
				minimumResultsForSearch: -1, //hide the search box
			});
			_self.$item_state.select2({
				minimumResultsForSearch: -1, //hide the search box
			});
			_self.$item_language.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : 150
			});
			_self.$item_timezone.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : 150
			});
			
			_self.$item_storage_sel.select2({
			  ajax: {
				url: "../common/storage-list.do",
				dataType: 'json',
				delay: 250,
				data: function (params) {
				  return {
					storage_name: params.term, // search term
					pageNumber: params.page,
					pageSize : 10
				  };
				},
				processResults: function (data, params) {
				  params.page = params.page || 1;
				   
				   for(var i = 0; i < data.items.length; i++){
					   data.items[i].id= data.items[i].key;
					   data.items[i].text = data.items[i].value;
				   }
				  return {
					results: data.items,
					pagination: {
					  more: (params.page * 10) < data.total_count
					}
				  };
				},
				cache: true
			  },
			  minimumInputLength: 0,
			  placeholder: { id: "", text : "Select a storage"}
			});
		}
	};
	
	AccountEdit.getAccount = function(){
		var _self = this;
		return {
			userId : _self.$item_uid.val(),
			account : _self.$item_account.html(),
			name : _self.$item_name.val(),
			type : _self.$item_type.val(),
			state : _self.$item_state.val(),
			password : _self.$item_password.val(),
			confirm : _self.$item_confirm.val(),
			email : _self.$item_email.val(),
			mobile : _self.$item_mobile.val(),
			language : _self.$item_language.val(),
			timezone : _self.$item_timezone.val(),
			phone : _self.$item_phone.val(),
			storageId : _self.$item_storage_sel.val(),
			pubcapacity: _self.$item_pub_capacity.val(),
			pricapacity: _self.$item_pri_capacity.val()
		};
	};
	
	AccountEdit.setAccount = function(item){
		var _self = this;
		
		_self.$item_uid.val(item.userId);
		_self.$item_account.html(item.account);
		_self.$item_name.val(item.name);

		_self.$item_type.val(item.type).trigger('change');
		_self.$item_state.val(item.state).trigger('change');

		_self.$item_email.val(item.email);
		_self.$item_mobile.val(item.mobile);
		_self.$item_phone.val(item.phone);	
		_self.$item_language.val(item.language).trigger('change');
		_self.$item_timezone.val(item.timezone).trigger('change');
		
		_self.$item_storage_sel.empty();
		var dft_opt = '<option value="' + item.storageId + '" selected>' + item.storageName + '</option>';
		_self.$item_storage_sel.append(dft_opt).trigger('change');
	
		_self.$item_pub_capacity.val(item.pubcapacity),
		_self.$item_pri_capacity.val(item.pricapacity)
	};
	
	AccountEdit.saveAccount = function(){
		var _self = this;
		var account_data = _self.getAccount();
		
		$.ajax({
			url: "../ga/account-update.do",
			dataType : "json",
			data: account_data,
			success: function(response)
			{	
				GPContext.AppendResult(response, false);    
			}
		});
	};
	
	/*
	 * load the account data by account information
	 */
	AccountEdit.loadAccount = function(e){
		var _self = this;
		$('a[gpid="edit-tab"]').tab('show');
		var uid = $(e).attr('data-user-id');
		$.ajax({
			url: "../ga/account-info.do",
			dataType : "json",
			data: {
				'user_id' : uid,
				},
			success: function(response)
			{	
				_self.setAccount(response.data);
				// show message in header
				GPContext.AppendResult(response, false);  
			}
		});
	};
	
	AccountEdit.initial();
		
	return {
		LoadAccount : function(e){AccountEdit.loadAccount(e);},
		RemoveAccount : function(e){AccountList.removeAccount(e);},
		LoadAccountExt : function(e){AccountExt.loadAccountExt(e);}
	};
})(jQuery, window);
