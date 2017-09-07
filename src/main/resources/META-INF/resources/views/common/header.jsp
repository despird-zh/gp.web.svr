<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
		<!-- Logo -->
        <a href="../main/mainpage.do" class="logo">
          <!-- mini logo for sidebar mini 50x50 pixels -->
          <span class="logo-mini"><b>G</b>PR</span>
          <!-- logo for regular state and mobile devices -->
          <span class="logo-lg"><b>Group</b>RESS</a>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top" role="navigation">
          <!-- Sidebar toggle button-->
          <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
          </a>
		  <div gpid="message-toggle-container" class="clearfix pull-left">
			  <div class="message-toggle" data-toggle="dropdown" >
				<div gpid="message-in-header" role="button" class="text-danger message-abstract">
					<i class="fa fa-hand-peace-o fa-fw m-r-sm"></i>Welcome to Groupress ECM !!!
				</div>
			  </div>
			  <ul class="dropdown-menu warn-err-message">
				  <li class="header">You have <strong gpid="err-message-count">4</strong> messages <span gpid="err-message-clear-btn" class="pull-right btn p-none text-muted"><i class="fa fa-trash fa-fw"></i></span></li>
				  <li>
				   <!-- inner menu: contains the actual data -->
				   <ul gpid="err-message-list" class="menu">
					 <li><!-- start message -->
					   <a href="javascript:;" role="button" >
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <span gpid="err-message-more-btn" class="pull-right"><i class="fa fa-angle-double-down fa-fw"></i></span>
						 <small class="text-warning"><i class="fa fa-exclamation-triangle fa-fw"></i> 18:23:43</small>
						 <p> Why not buy a new awesome theme sadf asdf dfs sdfsadf ?</p>
						 <ol class="text-warning hidden detail-msgs" >
							  <li>name : can be null.</li>
							  <li>name : can be null.</li>
						 </ol>
					   </a>
					 </li><!-- end message -->
					 <li>
					   <a href="javascript:;">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-info"><i class="fa fa-flag fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
					 <li>
					   <a href="#">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-danger"><i class="fa fa-star fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
					 <li>
					   <a href="javascript:;">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-info"><i class="fa fa-flag fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
					 <li>
					   <a href="#">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-danger"><i class="fa fa-star fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
					 <li>
					   <a href="javascript:;">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-info"><i class="fa fa-flag fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
					 <li>
					   <a href="#">
						 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
						 <small class="text-danger"><i class="fa fa-star fa-fw"></i> 5 mins</small>
						 <p>Why not buy a new awesome theme?</p>
					   </a>
					 </li>
				   </ul>
				  </li>
			  </ul>
		  </div>
          <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
              <!-- User Account: style can be found in dropdown.less -->
              <li class="dropdown user user-menu">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                  <img src="../dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                  <span class="hidden-xs">Alexander Pierce</span>
                </a>
                <ul class="dropdown-menu">
                  <!-- User image -->
                  <li class="user-header">
                    <img src="../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                    <p>
                      Alexander Pierce - Web Developer
                      <small>Member since Nov. 2012</small>
                    </p>
                  </li>
                  <!-- Menu Body -->
                  <li class="user-body">
                    <div class="row">
                      <div class="col-xs-4 text-center">
                        <a href="#">Followers</a>
                      </div>
                      <div class="col-xs-4 text-center">
                        <a href="#">Sales</a>
                      </div>
                      <div class="col-xs-4 text-center">
                        <a href="#">Friends</a>
                      </div>
                    </div><!-- /.row -->
                  </li>
                  <!-- Menu Footer-->
                  <li class="user-footer">
                    <div class="pull-left">
                      <a href="#" class="btn btn-default btn-flat">Profile</a>
                    </div>
                    <div class="pull-right">
                      <a href="#" class="btn btn-default btn-flat">Sign out</a>
                    </div>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
	<script id="message-text-template" type="x-tmpl-mustache">
		{{#warning}}
			 <li>
                   <a href="javascript:;">
					 <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
					 {{#hasDetail}}
                     <span gpid="err-message-more-btn" class="pull-right"><i class="fa fa-angle-double-down fa-fw"></i></span>
					 {{/hasDetail}}
                     <small class="text-warning"><i class="fa fa-warning fa-fw"></i> {{timeText}}</small>
                     <p>{{messageText}}</p>
					 <ol class="text-warning hidden detail-msgs" style="">
					 {{#detailMessages}}
						  <li style="word-wrap: break-word;">{{property}} : {{message}}</li>	
					 {{/detailMessages}}
					 </ol>
                   </a>
             </li>
		{{/warning}}
		{{#error}}
			 <li>
                   <a href="javascript:;">
				     <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
					  {{#hasDetail}}
                     <span gpid="err-message-more-btn" class="pull-right"><i class="fa fa-angle-double-down fa-fw"></i></span>
					 {{/hasDetail}}
                     <small class="text-danger"><i class="fa fa-exclamation-triangle fa-fw"></i> {{timeText}}</small>
                     <p>{{messageText}}</p>
					 <ol class="text-warning hidden detail-msgs" style="">
					 {{#detailMessages}}
						  <li style="word-wrap: break-word;">{{property}} : {{message}}</li>	
					 {{/detailMessages}}
					 </ol>
                   </a>
             </li>
		{{/error}}
		{{#info}}
			 <li>
                   <a href="javascript:;">
				     <span gpid="err-message-remove-btn" class="pull-right"><i class="fa fa-close fa-fw"></i></span>
					 {{#hasDetail}}
                     <span gpid="err-message-more-btn" class="pull-right"><i class="fa fa-angle-double-down fa-fw"></i></span>
					 {{/hasDetail}}
                     <small class="text-info"><i class="fa fa-flag fa-fw"></i> {{timeText}}</small>
                     <p>{{messageText}}</p>
					 <ol class="text-warning hidden detail-msgs" style="">
					 {{#detailMessages}}
						  <li style="word-wrap: break-word;">{{property}} : {{message}}</li>	
					 {{/detailMessages}}
					 </ol>
                   </a>
             </li>
		{{/info}}
	</script><!-- /.template:message-text -->
	<script id="message-header-template" type="x-tmpl-mustache">
		{{#warning}}
			<i class="fa fa-warning fa-fw m-r-sm"></i>{{messageText}}
		{{/warning}}
		{{#error}}
			<i class="fa fa-exclamation-triangle fa-fw m-r-sm"></i>{{messageText}}
		{{/error}}
		{{#info}}
			 <i class="fa fa-flag fa-fw m-r-sm"></i>{{messageText}}
		{{/info}}
	</script><!-- /.template:message-text -->