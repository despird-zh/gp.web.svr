;
var PageContext =(function ($, window, undefined){
	"use strict";
	
	var OrgHierarchy = {

		$orghier_tree : $('#tab_1 div[gpid=org-hier-tree]'),
		$node_type : $('#node-type'),
		$node_parent_id : $('#node-parent-id'),
		$node_id : $('#node-id'),
		$node_name : $('#node-name'),
		$node_admin : $('#node-admin'),
		$node_manager : $('#node-manager'),
		$node_email : $('#node-email'),
		$node_description : $('#node-description'),
		$curr_node_name : $('#current-node-name'),
		
		$node_type_selector : $('#node-type-selector'),
		
		$admin_sel_btn : $('#tab_1 a[gpid="admin-sel-btn"]'),
		$mgmr_sel_btn : $('#tab_1 a[gpid="mgmr-sel-btn"]'),
		
		$node_new_btn : $('#tab_1 a[gpid="orghier-new-btn"]'),
		$node_save_btn : $('#tab_1 a[gpid="orghier-save-btn"]'),

		initial : function(){
			var _self = this;
			_self.$node_type.select2({
				minimumResultsForSearch: -1, //hide the search box
				width : '150px'
			});
			// create scrollbar for folder hierarchy 
			_self.$orghier_tree.slimscroll({
				height: "350px",
				alwaysVisible: true,
				size: "5px"
			}).css("width", "100%");
			// create file directory tree panel
			_self.$orghier_tree.jstree({
					'core': {
						'themes': {
							responsive: !1,
		                	'name': 'proton'
						},
						'data' : {
							'url' : '../common/org-nodes.do',
							'data' : function (node) {
							  return { 'org_id' : node.id };
							}
						}
					},
					'types': {
						"default": {
							'icon': "fa fa-folder text-warning fa-md"
						},
						'file': {
							'icon': "fa fa-file text-inverse fa-md"
						}
					},
					'plugins': ["types"]
				}).on('loaded.jstree', function() {
					// find the first node to be selected
					var nid = OrgHierarchy.$orghier_tree.find("ul li:first-child").attr('id');
					OrgHierarchy.$orghier_tree.jstree('select_node', nid);
			  	}).on("select_node.jstree", function(e, treenode) {					
					OrgHierarchy.editOrgNode(treenode.node.original);
				});			
			// bind save operation
			_self.$node_save_btn.on('click', function(){
				var nodedata = OrgHierarchy.getOrgNode();
				OrgHierarchy.saveOrgNode(nodedata);
			});
			// bind new org node event
			_self.$node_new_btn.on('click', $.proxy(_self.newOrgNode, _self));
			// bind admin select event
			_self.$admin_sel_btn.on('click', function(){	

				GPContext.showSelectUser(function(user){
					OrgHierarchy.$node_admin.val(user.account);
				}, true);
			});
			_self.$mgmr_sel_btn.on('click', function(){	

				GPContext.showSelectUser(function(user){
					OrgHierarchy.$node_manager.val(user.account);
				}, true);
			});
		}
	};
	
	/*
	 * set the detail part on new mode
	 */
	OrgHierarchy.newOrgNode = function(){
		var _self = this;
		_self.operationmode = 'new';
		_self.$node_name.val('');
		_self.$node_admin.val('');
		_self.$node_manager.val('');
		_self.$node_email.val('');
		_self.$node_description.val('');
		
		_self.$node_type_selector.removeClass('hidden');
		OrgMembers.showTab(false);
	};
	
	/*
	 * set the org hier node data to editor
	 */
	OrgHierarchy.editOrgNode = function(nodedata){
		var _self = this;
		_self.operationmode = 'edit';
		_self.$node_name.val(nodedata.text);
		_self.$node_admin.val(nodedata.admin);
		_self.$node_manager.val(nodedata.manager);
		_self.$node_email.val(nodedata.email);
		_self.$node_description.val(nodedata.description);

		_self.$node_parent_id.val(nodedata.parent);
		_self.$node_id.val(nodedata.id);
		OrgMembers.$org_node_id.val(nodedata.id);
		_self.$curr_node_name.html(nodedata.text);
		OrgMembers.$org_node_name.val(nodedata.text);
		
		_self.$node_type_selector.addClass('hidden');
		OrgMembers.showTab(true);
	}; 
	
	/*
	 * fetch the org node data
	 */
	OrgHierarchy.getOrgNode = function(){
		var _self = this;
		var nodedata = {
			parent : _self.$node_parent_id.val(),
			id : _self.$node_id.val(),
			text : _self.$node_name.val(),
			admin : _self.$node_admin.val(),
			manager : _self.$node_manager.val(),
			email : _self.$node_email.val(),
			description : _self.$node_description.val(),
			nodetype : _self.$node_type.val()
		};
		// for new mode, need clarify the parent id
		if(_self.operationmode == 'new' && nodedata.nodetype == 'SIBLING'){
			nodedata.id = '';
		}else if(_self.operationmode == 'new' && nodedata.nodetype == 'CHILDREN'){			
			nodedata.parent = nodedata.id;
			nodedata.id = '';
		}
		return nodedata;
	}; 
	
	OrgHierarchy.saveOrgNode = function(nodedata){
		
		var svr_url, _self = this;
		if(_self.operationmode == 'edit'){
			svr_url = '../ga/orghier-save.do';
		}else{
			svr_url = '../ga/orghier-add.do';
		}
		
		$.ajax({
			url: svr_url,
			dataType : "json",
			data: nodedata,
			method : 'POST',
			success: function(response)
			{	
				GPContext.AppendResult(response, ('success' != response.state));	
			}
		});
	};
	// initialize the hierarchy tab panel
	OrgHierarchy.initial();
		
	var OrgMembers = {
	
		$org_node_refresh_btn : $('#tab_2 a[gpid="orgmember-node-refresh-btn"]'),
		$org_node_id : $('#orgmember-node-id'),
		$org_node_name : $('#orgmember-node-name'),
		$table : $('#tab_2 table[gpid="orgmember-list"]'),

		$member_tab : $('ul.nav-tabs li[gpid="org-member-tab"]'),
		
		$add_member_btn : $('#tab_2 a[gpid="orgmember-add-btn"]'),
		
		initial : function(){
			var _self = this;
			
			_self.$add_member_btn.bind('click', function(){
				GPContext.showSelectUser(
					OrgMembers.addMembers, 
					false
				);
			});
			_self.initDataTables();
			_self.$org_node_refresh_btn.bind('click', $.proxy( _self.search, _self));
		}
	};
	 
	OrgMembers.initDataTables = function(){
		var _self = this;
        // begin first table
        _self.$table.dataTable({			
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
				{"targets":5,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 return '<div class="btn-group">' +
					 '<button class="btn btn-primary btn-xs" onclick="javascript:PageContext.RemoveOrgMember(this)" data-account="' + full.account + '" type="button"><i class="fa fa-close"></i></button>' +
					 '</div>';
				 }
				}
            ],
			"columns" : [
				{ data : 'account'},
				{ data : 'name'},
				{ data : 'email'},
				{ data : 'type'},
				{ data : 'state'},
				{ data : 'userId'}
			]
        });
	};
	
	// control the member tab panel show or hide
	OrgMembers.showTab = function(show_flag){
		var _self = this;
		if(show_flag){
			_self.$member_tab.removeClass('hidden');
		}else{
			_self.$member_tab.addClass('hidden');
		}
	}
	
	/*
	 * search members of current node.
	 */
	OrgMembers.search = function(){
		
		var _self = this;
		
		$.ajax({
			url: "../ga/orghier-member-search.do",
			dataType : "json",
			data: { 
					org_id : _self.$org_node_id.val()
				},
			success: function(response)
			{	
				_self.$table.dataTable().api().clear();
				_self.$table.dataTable().api().rows.add(response.data).draw();
			}
		});
	};
	
	
	OrgMembers.addMembers = function(users_data){
		var _self = OrgMembers;
		$.ajax({
			url: '../ga/orghier-member-add.do',
			dataType : "json",
			data: { 
				org_id : _self.$org_node_id.val(),
				users : JSON.stringify(users_data)
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, false);
			}
		});
	};
	
	OrgMembers.removeOrgMember = function(el){
		var _self = this;
		var user_account = $(el).attr('data-account');
		$.ajax({
			url: '../ga/orghier-member-remove.do',
			dataType : "json",
			data: { 
				'org_id' : _self.$org_node_id.val(),
				'account' : user_account
				},
			success: function(response)
			{	
				GPContext.AppendResult(response, false);
			}
		});
	};

	OrgMembers.initial();
	
	return {
		RemoveOrgMember : $.proxy(OrgMembers.removeOrgMember , OrgMembers)
	};
})(jQuery, window);