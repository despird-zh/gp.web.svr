/**
 * AdminLTE Demo Menu
 * ------------------
 * You should not use this file in production.
 * This file is for demo purposes only.
 */
var PageContext = (function ($, AdminLTE) {

	"use strict";
	var LocalInstance = {
		
		$source_name : $('#source-name'),
		$global_id : $('#global-id'),
		$entity_code : $('#entity-code'),
		$node_code : $('#node-code'),
		$source_abbr : $('#source-abbr'),
		$short_name : $('#short-name'),
		$binary_url : $('#binary-url'),
		$service_url : $('#service-url'),
		$description : $('#description'),
		$admin : $('#sys-admin'),
		$email : $('#sys-email'),
		
		$save_btn : $('a[gpid=save-source-btn]'),
		$refresh_btn : $('a[gpid=refresh-source-info]'),
		
		initial : function(){
			var _self = this;
			$.ajax({
				url: "../ga/source-info.do",
				dataType : "json",
				data: { "source_id" : -9999}, // local instance id
				success: function(response)
				{	
					if(response.state == 'success'){
						LocalInstance.$source_name.val(response.data.name);
						LocalInstance.$global_id.val(response.data.globalId);
						LocalInstance.$entity_code.val(response.data.entityCode);
						LocalInstance.$node_code.val(response.data.nodeCode);
						LocalInstance.$source_abbr.val(response.data.abbr);
						LocalInstance.$short_name.val(response.data.shortName);
						LocalInstance.$binary_url.val(response.data.binaryUrl);
						LocalInstance.$service_url.val(response.data.serviceUrl);
						LocalInstance.$description.val(response.data.description);
						LocalInstance.$admin.val(response.data.admin);
						LocalInstance.$email.val(response.data.email);
					}

					GPContext.AppendResult(response, false);
					
				},
				error: function (jqXHR, textStatus, errorThrown){}
			});
			
			/** save button bind*/
			_self.$save_btn.bind('click', function(){
			
				LocalInstance.save();
			});
		}

	};
	
	LocalInstance.save = function(){
		
		var _self = this;
				
		var instdata = {
			sourceId : -9999,
			name : _self.$source_name.val(),
			globalId : _self.$global_id.val(),
			entityCode : _self.$entity_code.val(),
			nodeCode : _self.$node_code.val(),
			abbr : _self.$source_abbr.val(),
			shortName : _self.$short_name.val(),
			binaryUrl : _self.$binary_url.val(),
			serviceUrl : _self.$service_url.val(),
			description : _self.$description.val(),
			admin : _self.$admin.val(),
			email : _self.$email.val()
		};
		
		$.ajax({
			url: "../ga/source-save.do",
			dataType : "json",
			method: "POST",
			data: instdata, // local instance id
			success: function(response)
			{	
				// process message show it in header
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	LocalInstance.initial();

	/* instance list  */
	var InstanceList = {
	
		$search_ename : $('#tab_1 input[gpid="list_search_ename"]'), 
		$search_btn : $('#tab_1 a[gpid="list_search_btn"]'), 
		$clear_btn : $('#tab_1 a[gpid="list_clear_btn"]'), 
		$table : $('#tab_1 table[gpid="list_table"]'),  
	
		initial : function(){
			var _self = this;
			_self.$search_btn.bind("click",function(event){
				
				InstanceList.search();
			});
			_self.$clear_btn.bind("click",function(event){
				InstanceList.clear();
			});
			
			InstanceList.initDataTables();
		}
	};
	
	InstanceList.initDataTables = function(){
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
            "bStateSave": false, // save datatable state(pagination, sort, etc) in cookie.
			"autoWidth" : false,
            "lengthMenu": [
                [5, 10, 20, -1],
                [5, 10, 20, "All"] // change per page values here
            ],
            // set the initial value
            "pageLength": 5,            
            "order": [
                [1, "asc"]
            ], // set first column as a default sort by asc
            "columnDefs": [
				{"targets":[3] ,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){	
					var css = 'label-primary';
					if(data == 'DEACTIVE'){
						css = 'label-warning';
					}else if(data == 'FROZEN'){
						css = 'label-default';
					}
					 return '<span class="label '+ css +'">'+data+'</span>';					 
				 },
				 'width' : 70
				},
				{"targets":7 ,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){

					 var prefix = '<div class="btn-group">', css, funcstr;
					 css = full.state == 'DEACTIVE' ? 'disabled"' : '"';
					 funcstr = full.state == 'DEACTIVE' ? ' onclick="javascript:;"' : ' onclick="javascript:PageContext.ChangeState(\''+ data +'\', \'DEACTIVE\');"';
					 prefix += '<button class="btn btn-primary btn-xs '+ css + funcstr + '><i class="fa fa-lock"></i></button>';
					 css = full.state == 'ACTIVE' ? 'disabled"' : '"';
					 funcstr = full.state == 'ACTIVE' ? ' onclick="javascript:;"' : ' onclick="javascript:PageContext.ChangeState(\''+ data +'\', \'ACTIVE\');"';
					 prefix += '<button class="btn btn-primary btn-xs '+ css + funcstr + '><i class="fa fa-unlock"></i></button>';
					 css = full.state == 'FROZEN' ? 'disabled"' : '"';
					 funcstr = full.state == 'FROZEN' ? ' onclick="javascript:;"' : ' onclick="javascript:PageContext.ChangeState(\''+ data +'\', \'FROZEN\');"';
					 prefix += '<button class="btn btn-primary btn-xs '+ css + funcstr + '><i class="fa fa-unlock-alt"></i></button></div>';
					
					 return prefix;
				 },
				 "width": 70
				}
            ],
			"columns" : [
				{data : 'entityCode'},
				{data : 'nodeCode'},
				{data : 'name'},
				{data : 'state'},
				{data : 'abbr'},
				{data : 'shortName'},
				{data : 'description'},
				{data : 'sourceId'}
			]
        });
	};
	/*
	 * search the instance list 
	 * parameter - pageindex index of expected page.
	 */
	InstanceList.search = function(){
		var _self = this;
		$.ajax({
			url: "../ga/source-search.do",
			dataType : "json",
			async: false,
			data: { 
					source_name : _self.$search_ename.val()
				},
			success: function(response)
			{
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();
			}
		});
	};
	
	InstanceList.changeState = function(id, state){
		var _self = this;
		$.ajax({
			url: "../ga/source-change-state.do",
			dataType : "json",
			async: false,
			data: { 
					source_id : id,
					source_state : state
				},
			success: function(response)
			{	
				// process message
				GPContext.AppendResult(response, true);
			}
		});
	};
	InstanceList.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
		
	InstanceList.initial();
	
	/*
	 * Define the extern instance processing
	 */
	var ExtInstances = {
		
		$search_ename : $('#tab_2 input[gpid="extern_search_ename"]'), 
		$search_btn : $('#tab_2 a[gpid="extern_search_btn"]'), 
		$clear_btn : $('#tab_2 a[gpid="extern_clear_btn"]'), 
		$table : $('#tab_2 table[gpid="extern_list_table"]'), 

		$global_id : $('#ext-global-id'),
		$entity_code : $('#ext-entitycode'),
		$node_code : $('#ext-nodecode'),
		$name : $('#ext-name'),
		$abbr : $('#ext-abbr'),
		$short_name : $('#ext-shortname'),
		$email : $('#ext-email'),
		$admin : $('#ext-admin'),
		$binurl : $('#ext-binurl'),
		$svcurl : $('#ext-svcurl'),
		
		$ext_save_btn : $('#tab_2 a[gpid="extern-save-btn"]'), 
		
		initial : function(){
			var _self = this;
			_self.$search_btn.bind("click",function(event){
				ExtInstances.search(1);
			});
			
			_self.$clear_btn.bind("click",function(event){
				ExtInstances.clear();
			});
			
			_self.$ext_save_btn.bind("click", function(event){
				ExtInstances.saveExternInstance();				
			});
			_self.initDataTables();
		}
	};
			
	ExtInstances.initDataTables = function(){
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
                [1, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[0] ,
				 'searchable': false,
				 'orderable': false
				},
				{"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.LoadExtIntance(this)" data-global-id="'+data+'" type="button"><i class="fa fa-edit"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"drawCallback" : function(settings){
				
				$('#tab_2 table[gpid="extern_list_table"] input[type="checkbox"]').uniform();
			},
			"columns" : [
				{data : 'sourceId'},
				{data : 'entityCode'},
				{data : 'nodeCode'},
				{data : 'email'},
				{data : 'name'},
				{data : 'globalId'}
			]
        });
	};
	
	ExtInstances.search = function(pageindex){
		var _self = this;
		$.ajax({
			url: "../ga/ext-source-search.do",
			dataType : "json",
			async: false,
			data: { 
					pageNumber : 1,
					pageSize : 10,
					instanceName : ''
				},
			success: function(response)
			{
				_self.$table.dataTable().api().clear();
				$.each(response.data, function(index, e ){
					e.DT_RowId = e.instanceId;
				});
				
				_self.$table.dataTable().api().rows.add(response.data).draw();
			}
		});
	}
	
	ExtInstances.loadExternInstance = function(evt){
		var _self = this;
		var global_id = $(evt).attr('data-global-id');
		$.ajax({
			url: "../ga/ext-source-info.do",
			dataType : "json",
			data: { 
					"global_id" : global_id
				},
			method : "POST",
			success: function(response)
			{	
				_self.$global_id.val(response.data.globalId);
				_self.$entity_code.val(response.data.entityCode);
				_self.$node_code.val(response.data.nodeCode);
				_self.$name.val(response.data.name);
				_self.$abbr.val(response.data.abbr);
				_self.$short_name.val(response.data.shortName);
				_self.$email.val(response.data.email);
			}
		});
	};
	
	ExtInstances.saveExternInstance = function(){
		var _self = this;
		$.ajax({
			url: "../ga/ext-source-save.do",
			dataType : "json",
			data: { 
					globalId : _self.$global_id.val(),
					entityCode : _self.$entity_code.val(),
					nodeCode : _self.$node_code.val(),
					name : _self.$name.val(),
					abbr : _self.$abbr.val(),
					shortName : _self.$short_name.val(),
					email : _self.$email.val(),
					admin : _self.$admin.val(),
					binaryUrl : _self.$binurl.val(),
					serviceUrl : _self.$svcurl.val()
				},
			method : "POST",
			success: function(response)
			{	
				GPContext.AppendResult(response, (response.state != "success"));
			}
		});
		
	};
	
	ExtInstances.clear = function(){
		var _self = this;
		_self.$table.dataTable().api().clear().draw();
	};
	
	ExtInstances.initial();
	
	/*
	 * Expose the required function for page dom content element callback
	 */
	return {
		LoadExtIntance : function(e){ExtInstances.loadExternInstance(e);},
		ChangeState : function(id, state){InstanceList.changeState(id, state);}
	}
})(jQuery, $.AdminLTE);
