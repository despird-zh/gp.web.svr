<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<!-- sidebar menu: : style can be found in sidebar.less -->
<%
	String curr_module = request.getParameter("curr_module");
	String curr_page = request.getParameter("curr_page");
%>
          <ul class="sidebar-menu">		  
            <li class="header">系统信息</li>
			<li class="<%= "dashboard".equals(curr_page) ? "active" : "" %>">
				<a href="dashboard.do"><i class="fa fa-area-chart"></i> <span>运行状态</span>
				</a>
			</li>
			<li class="<%= "basic".equals(curr_page) ? "active" : "" %>">
				<a href="basic.do"><i class="fa fa-television "></i> <span>基本信息</span>
				</a>
			</li>
		  	<li class="header">系统配置</li>
			<li class="<%= "sys-option".equals(curr_page) ? "active" : "" %>">
				<a href="sys-option.do"><i class="fa fa-gear"></i> <span>系统参数</span>
				</a>
			</li>
            <li class="treeview <%= "storage".equals(curr_module) ? "active" : "" %>">
              <a href="#">
                <i class="fa fa-database"></i>
                <span>存储管理</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li class="<%= "list".equals(curr_page) ? "active" : "" %>">
					<a href="storage-list.do"><i class="fa fa-circle-o"></i>存储查询</a>
				</li>
                <li class="<%= "new".equals(curr_page) ? "active" : "" %>">
					<a href="storage-new.do"><i class="fa fa-circle-o"></i>新建存储</a>
				</li>
              </ul>
            </li>
            <li class="treeview <%= "security".equals(curr_module) ? "active" : "" %>">
              <a href="#">
                <i class="fa fa-fire"></i>
                <span>安全管理</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li class="<%= "list".equals(curr_page) ? "active" : "" %>">
					<a href="account-list.do"><i class="fa fa-circle-o"></i>用户查询</a>
				</li>
                <li class="<%= "new".equals(curr_page) ? "active" : "" %>">
					<a href="account-new.do"><i class="fa fa-circle-o"></i>新建用户</a>
				</li>
                <li class="<%= "ext".equals(curr_page) ? "active" : "" %>">
					<a href="account-ext.do"><i class="fa fa-circle-o"></i>外部用户</a>
				</li>
                <li class="<%= "account-sync".equals(curr_page) ? "active" : "" %>">
					<a href="account-sync.do"><i class="fa fa-circle-o"></i>账户同步</a>
				</li>
              </ul>
            </li>
            <li class="treeview <%= "workgroup".equals(curr_module) ? "active" : "" %>">
              <a href="#">
                <i class="fa  fa-black-tie"></i>
                <span>工作组</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li class="<%= "list".equals(curr_page) ? "active" : "" %>">
					<a href="workgroup-list.do"><i class="fa fa-circle-o"></i>工作组查询</a>
				</li>
                <li class="<%= "new".equals(curr_page) ? "active" : "" %>">
					<a href="workgroup-new.do"><i class="fa fa-circle-o"></i>新建工作组</a>
				</li>
              </ul>
            </li>
			<li class="<%= "orghier".equals(curr_page) ? "active" : "" %>">
				<a href="orghier.do"><i class="fa fa-tags"></i> <span>组织设置</span>
				</a>
			</li>
			<li class="header">其他设置</li>
            <li class="treeview <%= "master".equals(curr_module) ? "active" : "" %>">
              <a href="#">
                <i class="fa  fa-black-tie"></i>
                <span>基础信息</span>
                <i class="fa fa-angle-left pull-right"></i>
              </a>
              <ul class="treeview-menu">
                <li class="<%= "images".equals(curr_page) ? "active" : "" %>">
					<a href="image-list.do"><i class="fa fa-circle-o"></i>图片查询</a>
				</li>
                <li class="<%= "dictionary".equals(curr_page) ? "active" : "" %>">
					<a href="dict-list.do"><i class="fa fa-circle-o"></i>字典查询</a>
				</li>
              </ul>
            </li>
			<li class="header">审计同步</li>
            <li class="<%= "access-ctrl".equals(curr_page) ? "active" : "" %>">
              <a href="access-ctrl.do">
                <i class="fa fa-sitemap"></i> <span>访问控制</span>
              </a>
            </li>
			<li class="<%= "audit-ctrl".equals(curr_page) ? "active" : "" %>">
				<a href="audit-ctrl.do">
					<i class="fa fa-user-secret"></i> <span>审计信息</span>
				</a>
			</li>
          </ul>