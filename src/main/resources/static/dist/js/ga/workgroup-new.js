;
var PageContext = (function ($, window, undefined){
	"use strict";	
	/*
	 * Workgroup New Tab 
	 */
	var WorkgroupBasic = {
		
		$workgroup_id : $('#wrokgroup-id'),
		$workgroup_name : $('#workgroup-name'),
		$workgroup_state : $('#workgroup-state-sel'),
		$workgroup_admin : $('#workgroup-admin'),
		$workgroup_manager : $('#workgroup-manager'),
		$workgroup_org_id : $('#workgroup-org-id'),
		$workgroup_org_name : $('#workgroup-org-name'),
		$workgroup_descr : $('#workgroup-description'),
		$workgroup_storage : $('#workgroup-storage-id'),
		$workgroup_image : $('img[gpid="workgroup-avatar"]'),
		
		$workgroup_publish : $('#workgroup-publish-enable'),
		$workgroup_publish_capacity : $('#workgroup-publish-capacity'),
		
		$workgroup_netdisk : $('#workgroup-netdisk-enable'),
		$workgroup_netdisk_capacity : $('#workgroup-netdisk-capacity'),
		
		$workgroup_topic : $('#workgroup-topic-enable'),
		$workgroup_share : $('#workgroup-share-enable'),
		$workgroup_link : $('#workgroup-link-enable'),
		$workgroup_task : $('#workgroup-task-enable'),
		$workgroup_weight : $('#workgroup-task-weight'),
		
		$workgroup_save_btn : $('#tab_2 button[gpid="workgroup-save-btn"]'),
		
		initial : function(){
			var _self = this;
			
			$('#tab_2 input[type="checkbox"]').uniform();
			
			_self.$workgroup_state.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : '150px'
			});
			
			_self.$workgroup_storage.select2({
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
			
			$('#tab_2 a[gpid="admin-sel-btn"]').on("click", function(){				
				GPContext.showSelectUser(function(user){
					WorkgroupBasic.$workgroup_admin.val(user.account);
				}, true);
			});	
			$('#tab_2 a[gpid="manager-sel-btn"]').on("click", function(){				
				GPContext.showSelectUser(function(user){
					WorkgroupBasic.$workgroup_manager.val(user.account);
				}, true);
			});	
			$('#tab_2 a[gpid="orghier-sel-btn"]').on("click", function(){				
				GPContext.showSelectOrghier(function(org_data){				
					WorkgroupBasic.$workgroup_org_id.val(org_data.org_id);
					WorkgroupBasic.$workgroup_org_name.val(org_data.org_name);
				});
			});	
			
			_self.$workgroup_save_btn.on('click', function(){			
				WorkgroupBasic.saveWorkgroup();
			});
						
			_self.$workgroup_image.on('click', function(){
				// this image dom element as parameter
				GPContext.showChangeAvatar($(this));
			});
		}
	};
	
	WorkgroupBasic.saveWorkgroup = function(){
		var _self = this;
		var wdata = _self.getWorkgroup();
		$.ajax({
			url: '../ga/workgroup-add.do',
			dataType : "json",
			data: wdata,
			method : "POST",
			success: function(response)
			{	
				GPContext.AppendResult(response, (response.state == 'success' ? false : true));
				  
			}
		});
	};
	
	WorkgroupBasic.getWorkgroup = function(){
				
		var wg_data = {}, _self = this;

		wg_data.workgroupName = _self.$workgroup_name.val();
		wg_data.state = _self.$workgroup_state.val();
		wg_data.admin = _self.$workgroup_admin.val();
		wg_data.manager = _self.$workgroup_manager.val();
		wg_data.orgId = _self.$workgroup_org_id.val();
		wg_data.description = _self.$workgroup_descr.val();
		wg_data.storageId = _self.$workgroup_storage.val();
		wg_data.publishOn = _self.$workgroup_publish.prop("checked");
		wg_data.netdiskOn = _self.$workgroup_netdisk.prop("checked");
		wg_data.publishCapacity = _self.$workgroup_publish_capacity.val();
		wg_data.netdiskCapacity = _self.$workgroup_netdisk_capacity.val();
		wg_data.topicOn = _self.$workgroup_topic.prop("checked");
		wg_data.shareOn = _self.$workgroup_share.prop("checked");
		wg_data.linkOn = _self.$workgroup_link.prop("checked");
		wg_data.taskOn = _self.$workgroup_task.prop("checked");
		wg_data.taskWeight = _self.$workgroup_weight.val();
		wg_data.imagePath = _self.$workgroup_image[0].src;
		
		return wg_data;
	};
	
	/*
	 * Workgroup member maintenance tab panel
	 */
	var WorkgroupMembers = {
		
		MemberMode : '',
		$member_search_user : $('#tab_3 input[gpid="member-search-user"]'),
		$member_search_enode : $('#tab_3 select[gpid="member-search-enode"]'),
		$member_search_btn : $('#tab_3 a[gpid="member-search-btn"]'),
		$member_save_btn : $('#tab_3 a[gpid="member-save-btn"]'),		
		$member_new_btn : $('#tab_3 a[gpid="add-new-member"]'),
		$member_cancel_btn : $('#tab_3 a[gpid="member-cancel-btn"]'),
		$member_close_btn : $('#tab_3 button[gpid="member-close-btn"]'),
		
		$member_account : $('#member-account'),
		$member_name : $('#member-name'),
		$member_email : $('#member-email'),
		$member_entity : $('#member-entity'),
		$member_avail_user : $('#avail-user-sel'),
		$member_role : $('#member-role'),
		
		$member_table : $('#tab_3 table[gpid="member-list"]'),
		
		initial : function(){
		
			var _self = this;
			_self.$member_search_enode.select2({
			  ajax: {
				url: "../common/entity-list.do",
				dataType: 'json',
				delay: 250,
				data: function (params) {
				  return {
					instance_name: params.term, // search term
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
			  escapeMarkup: function (markup) { return markup; },
			  minimumInputLength: 0,
			  placeholder: { id: "", text : "Select a entity node"}
			});
			
			_self.$member_avail_user.select2({
			  ajax: {
				url: "../common/avail-user-list.do",
				dataType: 'json',
				delay: 250,
				data: function (params) {
				  return {
					account: params.term, // search term
					wgroup_id : WorkgroupBasic.$workgroup_id.val(), // read from the workgroup create tab panel
					pageNumber: params.page,
					pageSize : 10
				  };
				},
				processResults: function (data, params) {
				  params.page = params.page || 1;
				   
				   for(var i = 0; i < data.items.length; i++){
					   data.items[i].id= data.items[i].account;
					   data.items[i].text = data.items[i].name;
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
			  placeholder: { id: "", text : "Available user..."}			
			}).on('select2:selecting', function(e){ 
				
				WorkgroupMembers.setMemberNewMode(e.params.args.data);
			});
			// disabled find user select
			_self.$member_avail_user.attr("disabled", true);
			
			_self.$member_role.select2({
				minimumResultsForSearch: -1, //hide the search box
			});
			// bind search button event
			_self.$member_search_btn.bind('click', function(){
				WorkgroupMembers.search(1);
			});
			// bind save button event
			_self.$member_save_btn.bind('click', function(){
				WorkgroupMembers.saveMember();
			});
			// bind new button event
			_self.$member_new_btn.bind('click', function(){				
				WorkgroupMembers.setMemberNewMode();
			});
			// bind cancel button event
			_self.$member_cancel_btn.bind('click', function(){
				WorkgroupMembers.setMemberEditMode();
			});
			_self.$member_close_btn.bind('click', function(){
				WorkgroupBasic.$member_tab.addClass('hidden');
				$('li[gpid="member-list-tab"] > a').tab('show');
			});
			_self.initDataTables();
		}
	};
	
	WorkgroupMembers.initDataTables = function(){
		var _self = this;
        // begin first table
        _self.$member_table.dataTable({
			
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
            //"pagingType": "bootstrap_full_number",
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [		
				{"targets":3,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<span class="label bg-green">' + data + '</span>' ;
				 }
				},
				{"targets":4,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<span class="label bg-blue">' + data + '</span>' ;
				 }
				},
				{"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-account="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.EditMember(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-account="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveMember(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'uname'},
				{ data : 'instanceName'},
				{ data : 'email'},
				{ data : 'role'},
				{ data : 'type'},
				{ data : 'uname'}
			]			
        });
	};
	
	WorkgroupMembers.setMemberEditMode = function(e){
		var _self = this;
		_self.MemberMode = 'edit';
		if(e != undefined){
			var user_account = $(e).attr('data-account');
			// find the row data
			var mbr_data = _self.$member_table.dataTable().api().row($(e).parents('tr')).data();			
			
			_self.$member_account.val(mbr_data.account);
			_self.$member_name.val(mbr_data.uname);
			_self.$member_email.val(mbr_data.email);
			_self.$member_entity.val(mbr_data.instanceName);
			_self.$member_role.val(mbr_data.role).trigger("change");
		}else{
			_self.$member_account.val("");
			_self.$member_name.val("");
			_self.$member_email.val("");
			_self.$member_entity.val("");
			_self.$member_role.val("").trigger("change");
		}
		_self.$member_avail_user.prop("disabled", true);
		_self.$member_cancel_btn.addClass('hidden');
		
	};
	
	// parameter will the data of item in available users
	WorkgroupMembers.setMemberNewMode = function(avl_user){
		var _self = this;
		_self.MemberMode = 'new';
		console.log(avl_user);
		if(avl_user == undefined){
			_self.$member_account.val("");
			_self.$member_name.val("");
			_self.$member_email.val("");
			_self.$member_entity.val("");
		}else{
			_self.$member_account.val(avl_user.account);
			_self.$member_name.val(avl_user.name);
			_self.$member_email.val(avl_user.email);
			_self.$member_entity.val(avl_user.sourceName);
		}
		_self.$member_role.val("").trigger("change");
		
		_self.$member_avail_user.prop("disabled", false);
		_self.$member_cancel_btn.removeClass('hidden');

	};
	
	WorkgroupMembers.search = function(){
		
		var _self = this;
		
		$.ajax({
			url: "../ga/workgroup-member-search.do",
			dataType : "json",
			data: { 
					account : _self.$member_search_user.val(),
					wgroup_id : WorkgroupBasic.$workgroup_id.val(),
					enode_id : _self.$member_search_enode.val(),
					pageNumber : 1,
					pageSize : 10
				},
			success: function(response)
			{	
				_self.$member_table.dataTable().api().clear();
				_self.$member_table.dataTable().api().rows.add(response.rows).draw();			  
			}
		});
	};
	
	WorkgroupMembers.saveMember = function(){
		var _self = this;
		$.ajax({
			url: '../ga/workgroup-member-add.do',
			dataType : "json",
			data: { 
				wgroup_id : WorkgroupBasic.$workgroup_id.val(),
				account : _self.$member_avail_user.val(),
				role : _self.$member_role.val()
			},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	WorkgroupMembers.removeMember = function(e){
		
		var _self = this;
		var user_account = $(e).attr('data-account');
		$.ajax({
			url: '../ga/workgroup-member-remove.do',
			dataType : "json",
			data: { 
				'wgroup_id' : WorkgroupBasic.$workgroup_id.val(),
				'account' : user_account
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	var WorkgroupGroups = {
	
		GroupMode : '',
		$group_search_name : $('#tab_1 input[gpid="wgroup-group-search-name"]'),
		$group_search_btn : $('#tab_1 a[gpid="wgroup-group-search-btn"]'),
		$group_table : $('#tab_1 table[gpid="wgroup-group-table"]'),

		$group_id : $('#wgroup-group-id'), // hidden elements
		$group_name : $('#wgroup-group-name'),
		$group_description : $('#wgroup-group-descr'),
		$group_save_btn : $('#tab_1 a[gpid="wgroup-group-save-btn"]'),
		$group_new_btn : $('#tab_1 a[gpid="wgroup-group-new-btn"]'),
		
		$group_cancel_btn : $('#tab_1 a[gpid="wgroup-group-cancel-btn"]'),
		$group_member_add_btn : $('#tab_1 a[gpid="wgroup-group-member-add-btn"]'),

		$group_mbr_regresh_btn : $('#tab_1 a[gpid="group-member-refresh-btn"]'),
		$group_mbr_table : $('#tab_1 table[gpid="group-member-table"]'),
		
		$group_close_btn : $('#tab_1 button[gpid="group-close-btn"]'),

		initial : function(){
			var _self = this;
			_self.$group_save_btn.bind('click', function(){
				
				WorkgroupGroups.newGroup();
			});
			
			_self.$group_search_btn.bind('click', function(){
				WorkgroupGroups.search(1);
			});
			
			_self.$group_member_add_btn.bind('click', function(){
				GPContext.showSelectWorkgroupMember(
					WorkgroupBasic.$workgroup_id.val(),// workgroup id
					function(users){ WorkgroupGroups.addGroupMember(users);	}, // callback
					false); // single flag
			});
			// bind refresh button event
			_self.$group_mbr_regresh_btn.bind('click', function(){
				WorkgroupGroups.membersSearch(1);
			});
			// bind new group button event
			_self.$group_new_btn.bind('click', function(){
				WorkgroupGroups.setGroupMode('new');
			});
			// bind cancel button event
			_self.$group_cancel_btn.bind('click', function(){
				WorkgroupGroups.setGroupMode('edit');
			});
			
			_self.$group_close_btn.bind('click', function(){
				WorkgroupBasic.$member_group_tab.addClass('hidden');
				$('li[gpid="member-list-tab"] > a').tab('show');
			});
			WorkgroupGroups.initGroupTable();
			WorkgroupGroups.initMemberTable();
		}
	}
	
	WorkgroupGroups.initGroupTable = function(){
		var _self = this;
        // begin first table
        _self.$group_table.dataTable({
			"dom": "<'table-scrollable'tr><'row'<'col-md-4 col-sm-4'i><'col-md-8 col-sm-8'p>>",
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
            "pageLength": 10,            
            //"pagingType": "bootstrap_full_number",
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [		
				{"targets":1,
				 'searchable': false,
				 'orderable': false
				},
				{"targets":2,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button data-account="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.EditMember(this);"><i class="fa fa-edit"></i></button>'+
					 '<button data-account="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveMember(this);"><i class="fa fa-close"></i></button>'+
					 '</div>';
				 },
				 'width' : 50
				}
            ],

			"columns" : [
				{ data : 'group'},
				{ data : 'description'},
				{ data : 'groupId'}
			]			
        });
	};
	
	WorkgroupGroups.initMemberTable = function(){
		var _self = this;
        // begin first table
        _self.$group_mbr_table.dataTable({
			"dom": "<'table-scrollable'tr><'row'<'col-md-4 col-sm-4'i><'col-md-8 col-sm-8'p>>",
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
            //"pagingType": "bootstrap_full_number",
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [		
				{"targets":2,
				 'searchable': false,
				 'orderable': false
				},
				{"targets":3,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<button data-account="'+data+'" class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveMember(this);"><i class="fa fa-close"></i></button>';
				 },
				 'width' : 25
				}
            ],

			"columns" : [
				{ data : 'uname'},
				{ data : 'email'},
				{ data : 'type'},
				{ data : 'account'}
			]			
        });
	};
	/*
	 * set group data to edit
	 */
	WorkgroupGroups.setGroupMode = function(mode, _group_data){
	
		var group_data = $.extend( {}, {groupid:'',group:'',description:''}, _group_data );
		var _self = this;
		_self.GroupMode = mode;
		_self.$group_id.val(group_data.groupid); // hidden elements
		_self.$group_name.val(group_data.group);
		_self.$group_description.val(group_data.description);		
		if(mode == 'new'){
			_self.$group_cancel_btn.removeClass('hidden');
			_self.$group_member_add_btn.addClass('hidden');
		}
		else if(mode == 'edit'){
			_self.$group_cancel_btn.addClass('hidden');
			_self.$group_member_add_btn.removeClass('hidden');
		}
	};
	
	WorkgroupGroups.membersSearch = function(pageindex){
		
		var _self = this;
		
		$.ajax({
			url: "../ga/workgroup-group-member-search.do",
			dataType : "json",
			data: { 
					group_id : _self.$group_id.val(),
					pageNumber : pageindex,
					pageSize : 10
				},
			success: function(response)
			{	
				_self.$group_mbr_table.dataTable().api().clear();
				_self.$group_mbr_table.dataTable().api().rows.add(response.rows).draw();
			}
		});
	};
	
	WorkgroupGroups.removeGroupMember = function(account){
	
		var _self = this;
		$.ajax({
			url: "../ga/workgroup-group-member-remove.do",
			dataType : "json",
			data: {
				'group_id' : _self.$group_id.val(),
				'account' : account
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	WorkgroupGroups.search = function(pageindex){
		
		var _self = this;
		
		$.ajax({
			url: "../ga/workgroup-group-search.do",
			dataType : "json",
			data: { 
					group : _self.$group_search_name.val(),
					wgroup_id : WorkgroupBasic.$workgroup_id.val(),
					pageNumber : pageindex,
					pageSize : 10
				},
			success: function(response)
			{	
				_self.$group_table.dataTable().api().clear();
				_self.$group_table.dataTable().api().rows.add(response.rows).draw();	  
			}
		});
	};
	
	WorkgroupGroups.removeGroup = function(groupid){
	
		var _self = this;
		$.ajax({
			url: "../ga/workgroup-group-remove.do",
			dataType : "json",
			data: {
				'group_id' : groupid
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	WorkgroupGroups.newGroup = function(){
		var _self = this;
		$.ajax({
			url: '../ga/workgroup-group-add.do',
			dataType : "json",
			data: { 
				'wgroup_id' : WorkgroupBasic.$workgroup_id.val(),
				'group' : _self.$group_name.val(),
				'description' : _self.$group_description.val()
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, true);
			}
		});
	};
	
	WorkgroupGroups.addGroupMember = function(users_data){
	
		var _self = this;
		$.ajax({
			url: '../ga/workgroup-group-member-add.do',
			dataType : "json",
			data: { 
				group_id : _self.$group_id.val(),
				users : JSON.stringify(users_data)
				},
			success: function(response)
			{	
				vGPContext.AppendResult(response, true);
			}
		});
	};
	
	WorkgroupGroups.initial();
	
	WorkgroupMembers.initial();
	
	WorkgroupBasic.initial();
	
	return {
		
		EditMember : function(e){WorkgroupMembers.setMemberEditMode(e);},
		RemoveMember : function(e){WorkgroupMembers.removeMember(e);}
	};
})(jQuery, window);