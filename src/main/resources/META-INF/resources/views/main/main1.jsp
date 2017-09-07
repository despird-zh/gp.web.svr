<%@ page language="java" errorPage="/WEB-INF/view/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@include file="../common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh_CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>AdminLTE 2 | Top Navigation</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<%@include file="../common/include.jsp" %>
	
  </head>
  <!-- ADD THE CLASS layout-top-nav TO REMOVE THE SIDEBAR. -->
  <body class="hold-transition skin-blue fixed layout-top-nav">
    <div class="wrapper">

      <header class="main-header">
		<%@include file="../common/header.jsp" %>
      </header>
      <!-- Full Width Column -->
      <div class="content-wrapper">
        <div class="container">

          <section class="content-header">
            <h1>
              Groupress Documents Cooperation Work System.
              <small>查看网盘内部</small>
            </h1>
          </section>

          <!-- Main content -->
          <section class="content">

		  <div class="row">
		  	<div class="col-md-3" >
				<div class="box box-solid no-radius" >
					<div class="box-header with-border">
					  <i class="fa fa-bar-chart"></i>
					  <h3 class="box-title">Summary</h3>
					</div><!-- /.box-header -->
					<div class="box-body" style="padding:5px 0;">
					<div class="row">
						<div class="col-md-8">
						  <div class="chart-responsive">
							<canvas  id="pieChart" ></canvas>
						  </div><!-- ./chart-responsive -->
						</div><!-- /.col -->
						<div class="col-md-4">
						  <ul class="chart-legend clearfix">
							<li><i class="fa fa-circle-o text-red"></i> Chrome</li>
							<li><i class="fa fa-circle-o text-green"></i> IE</li>
							<li><i class="fa fa-circle-o text-yellow"></i> FireFox</li>
							<li><i class="fa fa-circle-o text-aqua"></i> Safari</li>
							<li><i class="fa fa-circle-o text-light-blue"></i> Opera</li>
							<li><i class="fa fa-circle-o text-gray"></i> Navigator</li>
						  </ul>
						</div><!-- /.col -->
					  </div>
					</div><!-- /.box-body -->
					<div class="box-footer">
						<a class="btn btn-primary btn-xs"><i class="fa fa-play"></i></a>
					</div>
				</div>
			</div>
			<div class="col-md-6">
			 <div class="box box-solid">
			 <div class="box-body " style="padding:0px;">
				<div id="carousel-example-generic" class="carousel slide" data-ride="carousel" data-interval="3000">
				  <!-- Indicators -->
				  <ol class="carousel-indicators">
					<li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
					<li data-target="#carousel-example-generic" data-slide-to="1"></li>
					<li data-target="#carousel-example-generic" data-slide-to="2"></li>
				  </ol>
				 
				  <!-- Wrapper for slides -->
				  <div class="carousel-inner">
					<div class="item active">
					  <img src="http://placehold.it/1200x600" alt="...">
					  <div class="carousel-caption">
						  <h3>Caption Text</h3>
					  </div>
					</div>
					<div class="item">
					  <img src="http://placehold.it/1200x600" alt="...">
					  <div class="carousel-caption">
						  <h3>Caption Text</h3>
					  </div>
					</div>
					<div class="item">
					  <img src="http://placehold.it/1200x600" alt="...">
					  <div class="carousel-caption">
						  <h3>Caption Text</h3>
					  </div>
					</div>
				  </div>
				 
				  <!-- Controls -->
				  <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
					<span class="glyphicon glyphicon-chevron-left"></span>
				  </a>
				  <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
					<span class="glyphicon glyphicon-chevron-right"></span>
				  </a>
				</div> <!-- Carousel -->
			</div> <!-- Carousel -->
			</div><!-- box body -->
			</div>
			<div class="col-md-3">
				<div class="box box-solid no-radius" style="">
					<div class="box-header with-border">
					  <i class="fa fa-commenting"></i>
					  <h3 class="box-title">Text Emphasis</h3>
					  <div class="box-tools pull-right">
						<button id="start-button"  class="btn btn-box-tool" type="button"><i class="fa fa-play"></i></button>
						<button id="stop-button" class="btn btn-box-tool" type="button"><i class="fa fa-stop"></i></button>
						<button id="prev-button" class="btn btn-box-tool" type="button"><i class="fa fa-chevron-up"></i></button>
						<button id="next-button" class="btn btn-box-tool" type="button"><i class="fa fa-chevron-down"></i></button>
					  </div>
					</div><!-- /.box-header -->
					<div class="box-body" style="padding:0px 5px;">
						<ul id="newsticker" class="list-group list-group-noborder" style="margin-bottom:0px;">
						  <li class="list-group-item text-green">Lead to emphasize importance</li>
						  <li class="list-group-item text-green">Text green to emphasize success</li>
						  <li class="list-group-item text-aqua">Text aqua to emphasize info</li>
						  <li class="list-group-item text-light-blue">Text light blue to emphasize info (2)</li>
						  <li class="list-group-item text-red">Text red to emphasize danger</li>
						  <li class="list-group-item text-yellow">Text yellow to emphasize warning</li>
						  <li class="list-group-item text-muted">Text muted to emphasize general</li>
						  <li class="list-group-item text-muted">Text muted to emphasize general</li>
						  <li class="list-group-item text-muted">Text muted to emphasize general</li>
						  <li class="list-group-item text-muted">Text muted to emphasize general</li>
						  <li class="list-group-item text-muted">Text muted to emphasize general</li>
						</ul>
					</div><!-- /.box-body -->
					<div class="box-footer">
						<span>Last update 13:34:31 2015-6-7</span>
					</div>
				</div>
			</div>
		</div>
		  <div class="row">
			<div class="col-md-2">
				<div class="thumbnails thumbnail-style thumbnail-kenburn">
                	<div class="thumbnail-img">
                        <div class="overflow-hidden">
                            <img alt="" src="http://placehold.it/250x150" class="img-responsive">
                        </div>
                        <a href="#" class="btn-more hover-effect">read more +</a>					
                    </div>
                    <div class="caption">
                        <h3><a href="#" class="hover-effect">Project One</a></h3>
                        <p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
                    </div>
                </div>
			</div><!-- /.col-md-8 -->
		  </div><!-- /.row -->

          </section><!-- /.content -->
        </div><!-- /.container -->
      </div><!-- /.content-wrapper -->
      <footer class="main-footer">
        <div class="container">
          <div class="pull-right hidden-xs">
            <b>Version</b> 2.3.0
          </div>
          <strong>Copyright &copy; 2014-2015 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights reserved.
        </div><!-- /.container -->
      </footer>
    </div><!-- ./wrapper -->

	<!-- jQuery 2.1.4 -->
	<script src="${path_plugins}/jQuery/jquery.min.js"></script> 
	<!-- Bootstrap 3.3.5 -->	
	<script src="${path_bootstrap}/js/bootstrap.min.js"></script> 
	<!-- SlimScroll -->	
	<script src="${path_plugins}/slimScroll/jquery.slimscroll.min.js"></script> 
	<!-- FastClick -->	
	<script src="${path_plugins}/fastclick/fastclick.min.js"></script> 
    <!-- js/newsTicker.js -->
    <script src="${path_plugins}/newsticker/jquery.newsTicker.min.js"></script>
    <!-- ChartJS 1.0.1 -->
    <script src="${path_plugins}/chartjs/Chart.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.min.js"></script>
	<script src="${path_plugins}/jstree/dist/jstree.min.js"></script>
	<script src="${path_script}/pages/mainpage.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="${path_script}/demo.js"></script>
  </body>
</html>
