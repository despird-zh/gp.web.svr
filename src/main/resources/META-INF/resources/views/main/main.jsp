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
        <div class="container bg-white">
          <section class="content-header">
			<div class="row ">
				<div class="col-sm-12">	
					<h1 class="page-title">
					  Groupress
					  <small>Documents Cooperation Work System.</small>
					</h1>
					<jsp:include page="../common/page-navs.jsp" flush="true">
						<jsp:param name="module" value="dashboard"/>
					</jsp:include>
				</div>
			 </div>
          </section>
          <!-- Main content -->
          <section class="content">
				<div class="row  border-bottom bg-white dashboard-header" style="padding-bottom:10px;">
                    <div class="col-sm-3">
                        <h4 class="m-t-none"><i class="fa fa-cube m-r-xs"></i>Most Active Groups</h4>
                        <small>You have <strong>42</strong> Groups and <strong>660</strong> members.</small>
                        <ul class="list-group clear-list m-t-xs m-b-xs">
                            <li class="list-group-item first-item">
                                <span class="label label-success">1</span> 商务问题讨论组
								<span class="pull-right m-t-xs" style="font-size: 12px;color:#444;">
                                    <span class="">
									  <span><i class="fa fa-database"></i></span>
									  13
									</span>
									<span class="">
									  <span><i class="fa fa-user "></i></span>
									  25
									</span>
									<span class="">
									  <span><i class="fa fa-flag "></i></span>
									  25
									</span>
                                </span>
								
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    10:16 am
                                </span>
                                <span class="label label-info">2</span> Sign a contract
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    08:22 pm
                                </span>
                                <span class="label label-primary">3</span> Open new shop
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    11:06 pm
                                </span>
                                <span class="label label-default">4</span> Call back to Sylvia
                            </li>
                            <li class="list-group-item">
                                <span class="pull-right">
                                    12:00 am
                                </span>
                                <span class="label label-primary">5</span> Write a letter to Sandra
                            </li>
                        </ul>
                    </div>
                    <div class="col-sm-6">
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
                    </div>

					<div class="col-sm-3">
						<h4 class="m-t-none m-l-xs m-b-xs"><i class="fa fa-files-o m-r-xs"></i> Documents</h4>						
						<div class="board-num clearfix">
							<p data-docnum="143,450,822" class="num pull-right m-b-xs">
								<span data-num="1" class="num0" style="background-position: 0px -29px;">
								</span><span data-num="4" class="num0" style="background-position: 0px -116px;">
								</span><span data-num="3" class="num0" style="background-position: 0px -87px;">
								</span><b class="spr">,</b>
								<span data-num="4" class="num0" style="background-position: 0px -116px;">
								</span><span data-num="5" class="num0" style="background-position: 0px -145px;"></span>
								<span data-num="0" class="num0" style="background-position: 0px 0px;">
								</span><b class="spr">,</b>
								<span data-num="8" class="num0" style="background-position: 0px -232px;"></span>
								<span data-num="2" class="num0" style="background-position: 0px -58px;"></span>
								<span data-num="2" class="num0" style="background-position: 0px -58px;"></span>
							</p>
						</div>						
						<hr class="m-t-none m-b-xs">
						<h4 class="m-t-none m-l-xs m-b-xs"><i class="fa fa-commenting-o m-r-xs"></i> Topics</h4>						
						<div class="board-num clearfix">
							<p data-docnum="143,450,822" class="num pull-right m-b-xs">
								<span data-num="4" class="num0" style="background-position: 0px -116px;"></span>
								<span data-num="5" class="num0" style="background-position: 0px -145px;"></span>
								<span data-num="0" class="num0" style="background-position: 0px 0px;"></span>
								<b class="spr">,</b>
								<span data-num="8" class="num0" style="background-position: 0px -232px;"></span>
								<span data-num="2" class="num0" style="background-position: 0px -58px;"></span>
								<span data-num="2" class="num0" style="background-position: 0px -58px;"></span>
							</p>
						</div>						
						<hr class="m-t-none m-b-xs">
						<h4 class="m-t-none m-l-xs"><i class="fa fa-graduation-cap m-r-xs"></i> Experts</h4>	
						<div class=" list-group list-group-horizontal">
							<div class="list-group-item" style="border-width: 0px;">
								<div class="m-b-xs">
									<span class="label label-success yellow-bg m-r-xs"><i class="fa fa-bookmark"></i> Expert 5000</span> 
									<span class="label label-success blue-bg"><i class="fa fa-flag"></i> Expert 1000</span>
								</div>
							</div>
						</div>
					</div><!-- /.col-sm-3 -->
            </div>
		  <div class="row bg-white border-bottom ">
		  	<div class="col-md-12 " style="padding-left: 15px; padding-right: 5px; padding-top: 0px;">
				<div class="row bg-white" style="margin-left: -15px; margin-right: -5px;">
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
					<div class="col-md-2 thumbnails thumbnail-style thumbnail-kenburn">
						<div class="thumbnail-img">
							<div class="overflow-hidden">
								<img alt="" src="http://placehold.it/250x150" class="img-responsive">
							</div>
							<a href="#" class="btn-more hover-effect">read more +</a>					
						</div>
						<div class="caption">
							<h4 class="m-t-xs m-b-xs"><a href="#" class="hover-effect">Project One</a></h4>
							<p>Donec id elit non mi porta gravida at eget metus. Fusce dapibus, justo sit amet risus etiam porta sem.</p>
						</div>
					</div>
				</div>
			</div>
			</div>
			<div class="row ">
				<div class="col-sm-4">
					<div class="border-right">
						<h4><i class="fa fa-diamond"></i> Newest topic</h4>
						<ul class="list-group clear-list no-radius p-xxs m-r-sm">
                            <li class="list-group-item first-item">
                                    <small class="pull-right text-muted"> 16.02.2015</small>
                                    <strong>郭展宏</strong>
                                    <div class="small m-t-xs ">
                                        <p style="margin-bottom: 5px;">
                                            发动机是汽车的动力装置 , 发动机又称为 ，是一种能够把其它形式的能量转化
                                        </p>
                                        <p class="m-b-none">
                                          <i class="fa fa-clock-o"></i> 2天前  <i class="fa fa-map-marker"></i> 来自电气
                                        </p>
                                    </div>
                            </li>
                            <li class="list-group-item">
                                
                                    <small class="pull-right text-muted"> 11.10.2015</small>
                                    <strong>Paul Morgan</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            There are many variations of passages of Lorem Ipsum.
                                            <br>
                                        </p>
                                        <p class="m-b-none">
                                            <span class="label pull-right label-primary">SPECIAL</span>
                                            <i class="fa fa-map-marker"></i> California 10F/32
                                        </p>
                                    </div>
                               
                            </li>
                            <li class="list-group-item">
                              
                                    <small class="pull-right text-muted"> 08.04.2015</small>
                                    <strong>Michael Jackson</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            Look even slightly believable. If you are going to use a passage of.
                                        </p>
                                        <p class="m-b-none">
                                            <i class="fa fa-map-marker"></i> Berlin 120R/15
                                        </p>
                                    </div>
                              
                            </li>
                            <li class="list-group-item">
                               
								<small class="pull-right text-muted"> 16.02.2015</small>
								<strong>Mark Smith</strong>
								<div class="small m-t-xs">
									<p class="m-b-xs">
										It was popularised in the 1960s with the release of Letraset sheets
									</p>
									<p class="m-b-none">
										<i class="fa fa-map-marker"></i> San Francisko 12/100
									</p>
								</div>
                            
                            </li>
                            <li class="list-group-item">
                             
								<small class="pull-right text-muted"> 21.04.2015</small>
								<strong>Monica Novak</strong>
								<div class="small m-t-xs">
									<p class="m-b-xs">
										Printer took a galley of type and scrambled.
									</p>
									<p class="m-b-none">
										<i class="fa fa-map-marker"></i> New York 15/43
									</p>
								</div>
                           
                            </li>
                            <li class="list-group-item">                            
								<small class="pull-right text-muted"> 03.12.2015</small>
								<strong>Jack Smith</strong>
								<div class="small m-t-xs">
									<p class="m-b-xs">
										Also the leap into electronic typesetting, remaining.
									</p>
									<p class="m-b-none">
										<i class="fa fa-map-marker"></i> Sant Fe 10/106
									</p>
								</div>                             
                            </li>
                        </ul>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="border-right">
						<h4><i class="fa fa-diamond"></i> Newest topic</h4>
						<ul class="list-group clear-list no-radius p-xxs m-r-sm">
                            <li class="list-group-item first-item">
                                    <small class="pull-right text-muted"> 16.02.2015</small>
                                    <strong>郭展宏</strong>
                                    <div class="small m-t-xs ">
                                        <p style="margin-bottom: 5px;">
                                            发动机是汽车的动力装置 , 发动机又称为 ，是一种能够把其它形式的能量转化
                                        </p>
                                        <p class="m-b-none">
                                          <i class="fa fa-clock-o"></i> 2天前  <i class="fa fa-map-marker"></i> 来自电气
                                        </p>
                                    </div>
                            </li>
                            <li class="list-group-item">
                                
                                    <small class="pull-right text-muted"> 11.10.2015</small>
                                    <strong>Paul Morgan</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            There are many variations of passages of Lorem Ipsum.
                                            <br>
                                        </p>
                                        <p class="m-b-none">
                                            <span class="label pull-right label-primary">SPECIAL</span>
                                            <i class="fa fa-map-marker"></i> California 10F/32
                                        </p>
                                    </div>
                               
                            </li>
                            <li class="list-group-item">
                              
                                    <small class="pull-right text-muted"> 08.04.2015</small>
                                    <strong>Michael Jackson</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            Look even slightly believable. If you are going to use a passage of.
                                        </p>
                                        <p class="m-b-none">
                                            <i class="fa fa-map-marker"></i> Berlin 120R/15
                                        </p>
                                    </div>
                              
                            </li>
                            <li class="list-group-item">
                               
                                    <small class="pull-right text-muted"> 16.02.2015</small>
                                    <strong>Mark Smith</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            It was popularised in the 1960s with the release of Letraset sheets
                                        </p>
                                        <p class="m-b-none">
                                            <i class="fa fa-map-marker"></i> San Francisko 12/100
                                        </p>
                                    </div>
                            
                            </li>
                            <li class="list-group-item">
                             
                                    <small class="pull-right text-muted"> 21.04.2015</small>
                                    <strong>Monica Novak</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            Printer took a galley of type and scrambled.
                                        </p>
                                        <p class="m-b-none">
                                            <i class="fa fa-map-marker"></i> New York 15/43
                                        </p>
                                    </div>
                           
                            </li>
                            <li class="list-group-item">
                            
                                    <small class="pull-right text-muted"> 03.12.2015</small>
                                    <strong>Jack Smith</strong>
                                    <div class="small m-t-xs">
                                        <p class="m-b-xs">
                                            Also the leap into electronic typesetting, remaining.
                                        </p>
                                        <p class="m-b-none">
                                            <i class="fa fa-map-marker"></i> Sant Fe 10/106
                                        </p>
                                    </div>
                             
                            </li>


                        </ul>
					</div>
				</div>
				<div class="col-sm-3">
					<h4 class="m-t-none">Project Beta progress</h4>
					<p>
						You have two project with not compleated task.
					</p>
					<div class="row text-center">
						<div class="col-lg-6">
							<canvas height="120" width="120" id="polarChart"></canvas>
							<h5 class="m-t-none m-b-none">Kolter</h5>
						</div>
						<div class="col-lg-6">
							<canvas height="120" width="120" id="doughnutChart" ></canvas>
							<h5 class="m-t-none m-b-none">Maxtor</h5>
						</div>
					</div>
					<div class="m-t">
						<small>Lorem Ipsum is simply dummy text of the printing and typesetting industry.</small>
					</div>
				</div>
		</div>
          </section><!-- /.content -->
        </div><!-- /.container -->
      </div><!-- /.content-wrapper -->
      <footer class="main-footer">
        <%@include file="../common/footer.jsp" %>
      </footer>
    </div><!-- ./wrapper -->

	<!-- SlimScroll -->	
	<script src="${path_plugins}/slimScroll/jquery.slimscroll.min.js"></script> 
	<!-- FastClick -->	
	<script src="${path_plugins}/fastclick/fastclick.min.js"></script> 
    <!-- js/newsTicker.js -->
    <script src="${path_plugins}/newsticker/jquery.newsTicker.min.js"></script>
    <!-- ChartJS 1.0.1 -->
    <script src="${path_plugins}/chartjs/Chart.min.js"></script>
    <!-- AdminLTE App -->
    <script src="${path_script}/app.ctx.js"></script>
	<script src="${path_plugins}/jstree/dist/jstree.min.js"></script>
	<script src="${path_script}/pages/mainpage.js"></script>
    <!-- AdminLTE for demo purposes 
    <script src="${path_script}/demo.js"></script>-->
	
    <script>
        $(document).ready(function() {
           
            var doughnutData = [
                {
                    value: 300,
                    color: "#a3e1d4",
                    highlight: "#1ab394",
                    label: "App"
                },
                {
                    value: 50,
                    color: "#dedede",
                    highlight: "#1ab394",
                    label: "Software"
                },
                {
                    value: 100,
                    color: "#b5b8cf",
                    highlight: "#1ab394",
                    label: "Laptop"
                }
            ];

            var doughnutOptions = {
                segmentShowStroke: true,
                segmentStrokeColor: "#fff",
                segmentStrokeWidth: 2,
                percentageInnerCutout: 45, // This is 0 for Pie charts
                animationSteps: 100,
                animationEasing: "easeOutBounce",
                animateRotate: true,
                animateScale: false,
            };

            var ctx = document.getElementById("doughnutChart").getContext("2d");
            var DoughnutChart = new Chart(ctx).Doughnut(doughnutData, doughnutOptions);

            var polarData = [
                {
                    value: 300,
                    color: "#a3e1d4",
                    highlight: "#1ab394",
                    label: "App"
                },
                {
                    value: 140,
                    color: "#dedede",
                    highlight: "#1ab394",
                    label: "Software"
                },
                {
                    value: 200,
                    color: "#b5b8cf",
                    highlight: "#1ab394",
                    label: "Laptop"
                }
            ];

            var polarOptions = {
                scaleShowLabelBackdrop: true,
                scaleBackdropColor: "rgba(255,255,255,0.75)",
                scaleBeginAtZero: true,
                scaleBackdropPaddingY: 1,
                scaleBackdropPaddingX: 1,
                scaleShowLine: true,
                segmentShowStroke: true,
                segmentStrokeColor: "#fff",
                segmentStrokeWidth: 2,
                animationSteps: 100,
                animationEasing: "easeOutBounce",
                animateRotate: true,
                animateScale: false,
            };
            var ctx = document.getElementById("polarChart").getContext("2d");
            var Polarchart = new Chart(ctx).PolarArea(polarData, polarOptions);

        });
    </script>
  </body>
</html>
