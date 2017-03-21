<!DOCTYPE html>
<html>
<head>
  	<title>任务调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
  	<!-- daterangepicker -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker-bs3.css">
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>调度日志<small>日志中心</small></h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度日志</a></li>
				<li class="active">调度管理</li>
			</ol>
			-->
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    	<div class="row">
	    		<div class="col-xs-3">
 					<div class="input-group">
	                	<span class="input-group-addon">分组</span>
                		<select class="form-control" id="groupId"  paramVal="${groupId}" >
                			<!--
                            <option value="0" selected>请选择</option>
                            -->
                			<#list jobGroupsWithNull as group>
                				<option value="${group.id}" <#if groupId == group.id>selected</#if> >${group.groupName}</option>
                			</#list>
	                  	</select>
	                  	
	              	</div>
	            </div>
	            <div class="col-xs-3">
	              	<div class="input-group">
	                	<span class="input-group-addon">描述</span>
                        <select class="form-control" id="jobId" paramVal="${jobId}" >
						</select>
	              	</div>
	            </div>
	    </div>
	            
		<div class="row">	            
	            <div class="col-xs-4">
              		<div class="input-group">
                		<span class="input-group-addon">
	                  		调度时间
	                	</span>
	                	<input type="text" class="form-control" id="filterTime" readonly 
	                		value2="<#if triggerTimeStart?exists && triggerTimeEnd?exists >${triggerTimeStart?if_exists?string('yyyy-MM-dd HH:mm:ss')} - ${triggerTimeEnd?if_exists?string('yyyy-MM-dd HH:mm:ss')}</#if>"  >
	              	</div>
	            </div>
	            
				
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
          	</div>
			
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header hide"><h3 class="box-title">调度日志</h3></div>
			            <div class="box-body">
			              	<table id="joblog_list" class="table table-bordered table-striped display" width="100%" >
				                <thead>
					            	<tr>
					                	<th name="id" >id</th>
					                	<th name="groupId" >分组ID</th>
					                	<th name="jobId" >任务ID</th>
					                  	<th name="jobName" >任务名称</th>
					                  	<th name="jobClassApplication" >作业执行应用名</th>
                                        <th name="triggerType" >触发类型</th>
                                        
					                  	<th name="cronExpression" >cron任务表达式</th>
					                  	<th name="execJobStatus" >执行状态</th>
					                  	<th name="jobStartTime" >任务开始时间</th>
					                  	<th name="jobEndTime" >任务结束时间</th>
					                  	<th name="needTriggleNext" >触发下级</th>
					                  	<th name="forceTriggle" >强制触发</th>
					                  	<th name="remark" >执行结果</th>
					                  	
										
					                  	<th name="cuckooClientIp" >执行器IP</th>
					                  	<th name="cuckooClientTag" >客户端标识</th>
					                  	<th name="typeDaily" >是否日切</th>
					                  	<th name="txDate" >业务日期</th>
					                  	<th name="flowLastTime" >开始时间参数 </th>
					                  	<th name="flowCurTime" >结束时间参数 </th>
					                  	<th name="cuckooParallelJobArgs" >并发/集群任务参数</th>
					                  	<th name="handleMsg" >操作</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/joblog.index.1.js"></script>
</body>
</html>
