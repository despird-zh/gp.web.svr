<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<div class="modal fade" id="select-orghier-modal" tabindex="-1" role="dialog" aria-labelledby="select-orghier-modal-label"><!-- clipboard modal -->
  <div class="modal-dialog" role="document" style="width:650px;">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="select-orghier-modal-label">Select Organization</h4>
	  </div>
	  <div class="modal-body" style="padding-bottom: 0px;">

		<div class="row no-margin">
			<div class="col-md-7">
				<div class="p-xxs" style="height: 32px;border-bottom: 1px solid #f4f4f4;">
					<span>Browse organization<span>
				</div>
				<div class="m-t-sm m-b-sm">
				<div gpid="org-hier-tree" >					
				</div>  
				</div>
			</div><!-- /.col-md-7 -->
			<div class="col-md-5">
				<div class="p-xxs" style="height: 32px;border-bottom: 1px solid #f4f4f4;">
					<span>Browse organization<span>
				</div>
				<div class="m-t-sm m-b-sm row">
					<form class="form-horizontal">
						<div class="form-group">
						  <label class="col-sm-3 control-label" for="node-name"> Name</label>
						  <div class="col-sm-8">
							<input type="text" placeholder="orgnization name" gpid="node-name" value="org name x" class="form-control">
						  </div>
						</div>
						<div class="form-group">
						  <label class="col-sm-3 control-label" for="node-description">Descr</label>
						  <div class="col-sm-8">
							<textarea placeholder="Enter ..." rows="4" gpid="node-description" value="this is the dmeo description xxx" class="form-control"></textarea>
						  </div>
						</div>
						<input type="text" class="hidden" gpid="node-id">
					</form>
				</div>
			</div><!-- /.col-md-5 -->
		</div><!-- /.row -->
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button gpid="btn_exec" type="button" class="btn btn-primary">Select</button>
	  </div>
	</div>
  </div>
</div><!-- org hier selection modal -->
<!-- JStree -->
<script src="${path_plugins}/jstree/dist/jstree.min.js"></script>

<script type="text/javascript">
$(function (){
	/*
	 * Common dialogue to select user.
	 */	
	var SelectOrghierModal = {
		$select_modal : $("#select-orghier-modal"), 
		$orghier_tree : $('#select-orghier-modal div[gpid=org-hier-tree]'),
		$orgnode_id : $('#select-orghier-modal input[gpid=node-id]'),
		$orgnode_name : $('#select-orghier-modal input[gpid=node-name]'),
		$orgnode_description : $('#select-orghier-modal input[gpid=node-description]'),
		$select_exec_btn : $('#select-orghier-modal button[gpid=btn_exec]'),
		callback : function(){},
		initial : function(){
			var _self = this;
			// create scrollbar for folder hierarchy 
			_self.$orghier_tree.slimscroll({
			  height: "250px",
			  alwaysVisible: true,
			  size: "5px"
			}).css("width", "100%");
			
			// create file directory tree panel
			_self.$orghier_tree.jstree({
				core: {
					'data' : {
						'url' : '../common/org-nodes.do',
						'data' : function (node) {
						  return { 'org_id' : node.id };
						},
				      	'cache' : false
					},
					'themes': {
						responsive: !1,
		                'name': 'proton'
		            }
				},
				types: {
					"default": {
						icon: "fa fa-folder text-warning fa-md"
					},
					"file": {
						icon: "fa fa-file text-inverse fa-md"
					}
				},
				"plugins": ["types"]
			}).on('loaded.jstree', function() {
				// find the first node to be selected
				var nid = SelectOrghierModal.$orghier_tree.find("ul li:first-child").attr('id');
				SelectOrghierModal.$orghier_tree.jstree('select_node', nid);
			}).on("after_close.jstree", function (evt, data) {
				var tree = _self.$orghier_tree.jstree(true);
					tree.delete_node(data.node.children);
					tree._model.data[data.node.id].state.loaded = false;
				
			}).on("select_node.jstree", function(e, treenode) {
				SelectOrghierModal.setOrgNode(treenode.node.original);
			});
			
			// bind execute button
			_self.$select_exec_btn.bind("click", function(){
				SelectOrghierModal.execute();
			});
		}
	};

	SelectOrghierModal.setOrgNode = function(nodedata){
		var _self = this;
		_self.$orgnode_name.val(nodedata.text);
		_self.$orgnode_id.val(nodedata.id);
		_self.$orgnode_description.val(nodedata.description);
	}; 
	
	SelectOrghierModal.selectOrghierShow = function(callback){
		var _self = this;
		_self.callback = callback;
		$('#select-orghier-modal').modal('show');
	};
	/*
	 * execute selected user.
	 */
	SelectOrghierModal.execute = function(){
		var _self = this;
		
		var org_data = {			
			org_name : _self.$orgnode_name.val(),
			org_id : _self.$orgnode_id.val()
		};
		
		_self.callback(org_data);
		
		_self.$select_modal.modal('hide');
	};
	
	
	SelectOrghierModal.initial();
	
	GPContext.showSelectOrghier = function(callback){
		SelectOrghierModal.selectOrghierShow(callback);
	};
});
</script>