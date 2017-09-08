<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<div class="modal fade" id="select-wgroup-member-modal" tabindex="-1" role="dialog" aria-labelledby="select-user-modal-label"><!-- clipboard modal -->
  <div class="modal-dialog" role="document" style="width:650px;">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="select-workgroup-member-modal-label">Select Workgroup Member</h4>
	  </div>
	  <div class="modal-body" style="padding-bottom: 0px;">
		  <div class="row no-margin" >
			  <div class="col-md-12">
				<form class="form-inline">
					<label>User  </label> <input type="text" gpid="user-input" placeholder="account/name/ email" class="form-control input-sm"> &nbsp;
					<a gpid="user-search-btn" class="btn btn-default btn-sm"><i class="fa fa-search"></i></a>
					<a gpid="user-clear-btn" class="btn btn-default btn-sm"><i class="fa fa-close"></i></a>
				</form>	
				<hr style="margin-top: 10px; margin-bottom: 10px;">
			  </div>				  
		  </div>
		<div class="row no-margin" style="height:344px;">
			<div class="col-md-7">
				<div class="box-body no-padding">
					<table gpid="search-user-list" class="table table-condensed table-bordered table-ellipsis m-b-none" style="table-layout: fixed;">
						<thead>
							<tr>
								<th>
									<input type="checkbox" class="group-checkable" data-set="#select-wgroup-member-modal table[gpid=search-user-list] .checkboxes" />
								</th>
								<th>Name</th>
								<th>Email</th>
								<th>Type</th>
							</tr>						
						</thead>
						<tbody >
						</tbody>
					</table>
				</div>
			</div><!-- /.col-md-7 -->
			<div class="col-md-5">
				<div class="box-body no-padding">
				  <table gpid="select-user-list" class="table table-bordered  table-condensed" style="table-layout: fixed;">
					<thead>
						<th >Select user</th>
						<th>
							<div style="width:21px;height:21px;position: relative;">
								<span gpid="selected-remove-all-btn" role="button" class="label label-primary">
									<i class="fa fa-trash"></i>
								</span>
							</div>
						</th>
					</thead>
					<tbody >						
					</tbody>
				  </table>
				</div><!-- /.box-body -->
			</div><!-- /.col-md-5 -->
		</div><!-- /.row -->
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button gpid="btn_exec" type="button" class="btn btn-primary">Select</button>
	  </div>
	</div>
  </div>
</div><!-- clipboard modal -->
<script type="text/javascript">
var SelectWorkgroupMemberContext = (function ($, window, undefined){
	/*
	 * Common dialogue to select user.
	 */	
	var SelectWorkgroupMemberModal = {
		Workgroup_id : '',
		single : true,
		$select_modal : $("#select-wgroup-member-modal"), 
		$search_user : $('#select-wgroup-member-modal input[gpid="user-input"]'),
		$search_user_btn : $('#select-wgroup-member-modal a[gpid="user-search-btn"]'),
		$clear_user_btn : $('#select-wgroup-member-modal a[gpid="user-clear-btn"]'),

		$search_table : $('#select-wgroup-member-modal table[gpid=search-user-list]'),
		$select_table : $('#select-wgroup-member-modal table[gpid=select-user-list]'),
		
		$select_clear_btn : $('#select-wgroup-member-modal span[gpid=selected-remove-all-btn]'),
		
		$select_exec_btn : $('#select-wgroup-member-modal button[gpid=btn_exec]'),
		callback : function(){},
		
		initial : function(){
			
			var _self = this;
			_self.$select_modal.modal('hide');
			// bind execute button
			_self.$select_exec_btn.bind("click", $.proxy( _self.execute , _self));
			// bind search user button
			_self.$search_user_btn.bind('click', $.proxy( _self.search, _self));
			// bind clear up user search result button
			_self.$clear_user_btn.bind('click', $.proxy( _self.clear, _self));
			// bind clear up selected users button
			_self.$select_clear_btn.bind('click', $.proxy( _self.removeAllUsers, _self));
			
			_self.initSearchDataTables();
			_self.initSelectDataTables();
		}
	};

				
	SelectWorkgroupMemberModal.initSearchDataTables = function(){
		var _self = this;
        // begin first table
        _self.$search_table.dataTable({
			"dom": "<'table-scrollable'tr><'row'<'col-md-5 col-sm-5'i><'col-md-7 col-sm-7'p>>",
            "language": {
                "aria": {
                    "sortAscending": ": activate to sort column ascending",
                    "sortDescending": ": activate to sort column descending"
                },
                "emptyTable": "No data available in table",
                "info": "Found _TOTAL_ records",
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
            // set the initial value
            "pageLength": 8,
            
            "order": [
                [1, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[0] ,
				 'searchable': false,
				 'orderable': false,
				 'className': 'dt-body-center',
				 'render': function (data, type, full, meta){
					 return '<input type="checkbox" value="'+data+'" class="checkboxes">';
				 },
				 "width" : 25
				},{
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
				 },
				 "width" : 65
				}				
            ],
			"drawCallback" : function(settings){
				
				$('#select-wgroup-member-modal table[gpid="search-user-list"] input[type="checkbox"]').uniform();
			},
			"columns" : [
				{ data : 'userId'},
				{ data : 'name'},
				{ data : 'email'},
				{ data : 'type'}
			]
			
        });
		
		_self.$search_table.find('.group-checkable').change(function () {
            var set = $(this).attr("data-set");
			
            var checked = $(this).is(":checked");
            $(set).each(function () {
				var rdata = SelectWorkgroupMemberModal.$search_table.dataTable().api().row($(this).closest('tr')).data();
                if (checked) {
                    $(this).prop("checked", true);
                    $(this).parents('tr').addClass("active");
					SelectWorkgroupMemberModal.addUser(rdata);// add user
                } else {
                    $(this).prop("checked", false);
                    $(this).parents('tr').removeClass("active");
					SelectWorkgroupMemberModal.removeUser(rdata.serId); // remove user
                }
            });

            $.uniform.update(set);
        });

        _self.$search_table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");
			var tobeAdd = $(this).prop('checked');
			var rdata = SelectWorkgroupMemberModal.$search_table.dataTable().api().row($(this).closest('tr')).data();
			// prepare the row id to locate the row dom element
			rdata.DT_RowId = rdata.userId;			
			if(tobeAdd){

				SelectWorkgroupMemberModal.addUser(rdata);
			}else{
				
				SelectWorkgroupMemberModal.removeUser(rdata.userId);
			}
        });
	};

	SelectWorkgroupMemberModal.initSelectDataTables = function(){
		var _self = this;
        // begin first table
        _self.$select_table.dataTable({
			"dom": "<'table-scrollable'tr><'row'<'col-md-12 col-sm-12'p>>",
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
            // set the initial value
            "pageLength": 8,            
            "order": [
                [0, "asc"]
            ], // set first column as a default sort by asc
	
            "columnDefs": [
				{"targets":[1] ,
				 'searchable': false,
				 'orderable': false,
				 'render': function (data, type, full, meta){
					 
					 return '<div style="width:21px;height:21px;position: relative;"><span onclick="javascript:SelectWorkgroupMemberContext.RemoveSelected('+data+')" role="button" class="label label-primary"><i class="fa fa-trash"></i></span></div>';					 
				 },
				 'width' : 30
				}				
            ],

			"columns" : [
				{ data : 'name'},
				{ data : 'userId'}
			]
			
        });
	};
	
	/*
	 * execute selected user.
	 */
	SelectWorkgroupMemberModal.execute = function(){
		var _self = this;
		var data_ary = _self.$select_table.api().data();
		if(_self.single){
			var result = data_ary[0];
			// clean data
			if(result.hasOwnProperty('DT_RowId'))
				delete result.DT_RowId;
			
			_self.callback(data_ary[0]);
		}else{
			var result = [], k;
			$.each(data_ary, function(i, curr){
				//clean data
				if(curr.hasOwnProperty('DT_RowId')){
					delete curr.DT_RowId;
				}
				result.push(curr);
			});
			_self.callback(result);
		}
		
		_self.$select_modal.modal('hide');
	};
	/*
	 * clear up the search user list
	 */
	SelectWorkgroupMemberModal.clear = function(){
		
		var _self = this;
		_self.$search_table.dataTable().api().clear().draw();
	};
	/*
	 * search the page
	 */
	SelectWorkgroupMemberModal.search = function(){
		var _self = this;
		$.ajax({
			url: "../common/workgroup-member-list.do",
			dataType : "json",
			data: { 
					wgroup_id : _self.Workgroup_id,
					user_name : _self.$search_user.val()
				},
			success: function(response)
			{	
				_self.$search_table.dataTable().api().clear();
				$.each(response.data, function(i, cur){
					cur.DT_RowId = cur.userId;	
				});
				_self.$search_table.dataTable().api().rows.add(response.data).draw();			  
			}
		});
	};
	
	/*
	 * remove selected user
	 */
	SelectWorkgroupMemberModal.removeUser = function(user_id){				
		var _self = this;
		var dt_row = _self.$select_table.api().row('#' + user_id);
		dt_row.remove().draw();	
		var search_dt_row = _self.$search_table.api().row('#' + user_id).node();
		
		$(search_dt_row).removeClass("active");
		var chkbox = $(search_dt_row).find('input.checkboxes');		
		$.uniform.update(chkbox.prop('checked',false));
		var dt_data = _self.$select_table.dataTable().api().data();
		if(dt_data.length == 0){
			var group_chk = _self.$search_table.find('.group-checkable').prop('checked',false);
			$.uniform.update(group_chk);
		}
	
	}
	
	/*
	 * remove selected user
	 */
	SelectWorkgroupMemberModal.addUser = function(user_data){
				
		var _self = this;
		var dtdata = _self.$select_table.api().data();
		// remove from selected user list
		if(_self.single){
			_self.$select_table.dataTable().api().clear();			
		}else{

			var dt_row = _self.$select_table.api().row('#' + user_data.uid);
			if(dt_row.length > 0)
				return;
		}
		_self.$select_table.dataTable().api().row.add(user_data).draw();
	}
	
	/*
	 * remove selected user
	 */
	SelectWorkgroupMemberModal.removeAllUsers = function(){
		var _self = this;
		var dt_data = _self.$select_table.dataTable().api().data();
		$.each(dt_data, function(i, curr){
			
			var search_dt_row = _self.$search_table.api().row('#' + curr.uid).node();		
			$(search_dt_row).removeClass("active");
			var chkbox = $(search_dt_row).find('input.checkboxes');		
			$.uniform.update(chkbox.prop('checked',false));
			
		});
		var group_chk = _self.$search_table.find('.group-checkable').prop('checked',false);
		$.uniform.update(group_chk);
		_self.$select_table.dataTable().api().clear().draw();
		
	};
	
	/*
	 * show select user dialog
	 */
	SelectWorkgroupMemberModal.selectUserShow = function(wgroup_id, _callback, single){
		
		var _self = this;
		_self.Workgroup_id = wgroup_id,
		_self.single = single;
		_self.callback = _callback;
		if(single == true){
			//  as for single select, hide the check all button in table head
			_self.$search_table.find('th > div.checker').addClass('hidden');
		}else{
			_self.$search_table.find('th > div.checker').removeClass('hidden');
		}
		_self.$select_modal.modal('show');
	};
	
	SelectWorkgroupMemberModal.initial();
	
	GPContext.showSelectWorkgroupMember = function(wgroup_id, callback, single){
		SelectWorkgroupMemberModal.selectUserShow(wgroup_id, callback, single);
	};
	
	// expose the modal methods
	return {
		
		RemoveSelected : $.proxy( SelectWorkgroupMemberModal.removeUser, SelectWorkgroupMemberModal)
	};
})(jQuery, window);
</script>