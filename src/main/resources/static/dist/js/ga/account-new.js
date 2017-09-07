/*! 
 * Copyright (c) 2014-2015 Gary diao
 */
;var PageContext = (function ($, window, undefined){

	var AccountNew = {
		
		$item_account : $('#new-account'),
		$item_name : $('#new-name'),
		$item_type : $('#new-type'),
		$item_password : $('#new-password'),
		$item_confirm : $('#new-password-confirm'),
		$item_email : $('#new-email'),
		$item_mobile : $('#new-mobile'),
		$item_phone : $('#new-phone'),
		$item_language : $('#new-language'),
		$item_timezone : $('#new-timezone'),
		$item_storage_sel : $('#new-storage-sel'),
		$item_pub_capacity : $('#new-pub-cap'),
		$item_pri_capacity : $('#new-pri-cap'),

		$save_btn : $('#tab_4 button[gpid="new-save-btn"]'),
		
		initial : function(){
			
			var _self = this;
			_self.$save_btn.on("click", function(evt){
				AccountNew.saveAccount();
			});
			
			_self.$item_type.select2({
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
	
	AccountNew.getAccount = function(){
		var _self = this;
		return {
			account : _self.$item_account.val(),
			name : _self.$item_name.val(),
			type : _self.$item_type.val(),
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
	
	AccountNew.clearAccount = function(){
		var _self = this;
		_self.$item_account.val('');
		_self.$item_name.val('');
		_self.$item_type.val('INLINE').trigger('click');
		_self.$item_password.val('');
		_self.$item_confirm.val('');
		_self.$item_email.val('');
		_self.$item_mobile.val('');
		_self.$item_language.val('zh_CN').trigger('click');
		_self.$item_timezone.val('GMT+08:00').trigger('click');
		_self.$item_phone.val('');
		_self.$item_storage_sel.val('').trigger('click');
		_self.$item_pub_capacity.val('0');
		_self.$item_pri_capacity.val('0');
		
	};
	
	AccountNew.saveAccount = function(){
		var _self = this;
		var account_data = _self.getAccount();
	
		$.ajax({
			url: "../ga/account-add.do",
			dataType : "json",
			data: account_data,
			method : "POST",
			success: function(response)
			{	
				if('success' == response.state){
					_self.clearAccount();
				}
				GPContext.AppendResult(response, ('success' != response.state));    
			}
		});
	};
	
	AccountNew.initial();
	
	return {

	};
})(jQuery, window);
