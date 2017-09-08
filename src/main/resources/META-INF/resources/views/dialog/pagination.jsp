<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
	<script id="pagination-template" type="x-tmpl-mustache">
		{{#previous}}
			<li><a href="javascript:void(0)" data-page="{{previousPage}}">«</a></li>
		{{/previous}}
		{{^previous}}
			<li><a disable="disabled" data-page="{{previousPage}}">«</a></li>
		{{/previous}}		
		{{#pages}}
			{{#current}}
			<li><a class="bg-blue" disable="disabled" data-page="{{pageNumber}}">{{pageNumber}}</a></li>
			{{/current}}
			{{^current}}
			<li><a href="javascript:void(0)" data-page="{{pageNumber}}">{{pageNumber}}</a></li>
			{{/current}}
		{{/pages}}
		{{#next}}
			<li><a href="javascript:void(0)" data-page="{{nextPage}}">»</a></li>
		{{/next}}
		{{^next}}
			<li><a disable="disabled" data-page="{{nextPage}}">»</a></li>
		{{/next}}
	</script><!-- /.template:pagination -->
	<script type="text/javascript">
	$(function (){
		
		"use strict";
		/*
		 * Pagination
		 * $pagination - the dom element of pagination
		 * pagingdata - {}
		 * jumppage - jump page buttong click callback function
		 */
		GPContext.pagination = function($pagination, pagingdata, jumppage){

			var $pagination_tmpl = $('#pagination-template');
			var template = $pagination_tmpl.html();
			Mustache.parse(template);   // optional, speeds up future uses
			var rendered = Mustache.render(template, pagingdata);			
			$pagination.html(rendered);
			// bind event to button
			$pagination.find("li a").bind("click",function(event){
			  
				var datapage = $(this).attr('data-page');
				var disabled = $(this).attr('disable');	
				if(disabled == undefined && jumppage && typeof(jumppage) == 'function'){// enabled page menu
					// research data
					jumppage(datapage);
				}
			});
		};
	});
	</script>