<!DOCTYPE html>
<html>
<head>
  	<title>任务调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

	<#-- select2
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
    //$(".select2").select2();
    -->

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>调度管理<small>客户端管理</small></h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度管理</a></li>
				<li class="active">调度中心</li>
			</ol>
			-->
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    	<div class="row">
	    		<div class="col-xs-4">
	              	<div class="input-group">
	                	<span class="input-group-addon">作业应用名</span>
                		<select class="form-control" id="jobClassApplication" >
                			<#list jobAppWithNull as list>
                				<option value="${list.value}" >${list.description}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
          	</div>
          	
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body">
			              	<table id="job_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
					            		<th name="id" >标准ID</th>
					                	<th name="jobClassApplication" >作业执行应用名</th>
					                	<th name="cuckooClientIp" >执行器IP</th>
					                  	<th name="cuckooClientTag" >客户端标识</th> 
					                  	<th name="cuckooClientStatus" >客户端状态</th>
					                  	<th name="jobName" >任务名称</th> 
					                  	<th name="beanName" >实现类名称</th>
					                  	<th name="methodName" >方法名称</th> 
					                  	<th name="createDate" >创建时间</th> 
					                  	<th name="modifyDate" >修改时间</th> 
					                  	
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
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

<!-- job新增.模态框 -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" name="title" >新增任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
				
					
				
				
					<div class="form-group">
						<!--
						.分组下拉
		            	.任务执行应用名称下拉
		            	-->
						<label for="firstname" class="col-sm-2 control-label">分组名称<font color="red">*</font></label>
						<div class="col-sm-4">
							<select class="form-control" name="groupId" >
	                			<#list jobGroupList as group>
	                				<option value="${group.id}" >${group.groupName}</option>
	                			</#list>
		                  	</select>
						</div>
                        <label for="lastname" class="col-sm-2 control-label">执行应用<font color="red">*</font></label>
                        <div class="col-sm-4">
	                        <select class="form-control" name="jobClassApplication" >
	                			<#list jobAppList?keys as app>
	                				<option value="${app}" >${jobAppList[app]}</option>
	                			</#list>
		                  	</select>
	                  	</div>
					
					</div>
					<div class="form-group">
						<!--
						.任务名称和任务执行应用名称唯一
            			.并发参数设置  
						-->
                        <label for="firstname" class="col-sm-2 control-label">任务名称<font color="red">*</font></label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="jobName" placeholder="与@CuckooTask(‘任务名称’)对应" maxlength="100" > 
						</div>
						
						<label for="firstname" class="col-sm-2 control-label">执行参数<font color="black">*</font></label>
                        <div class="col-sm-4">
                        	<input type="text" class="form-control" name="cuckooParallelJobArgs" placeholder="请输入“执行参数”" maxlength="100" >
                        </div>
					</div>
					
					
                    <div class="form-group">
                       	<!--
                       	.触发类型下拉【CRON触发/上级任务触发】
            			.CRON表达式/上级任务名称（与4对应）
                       	-->
                    	<label for="firstname" class="col-sm-2 control-label">触发方式<font color="red">*</font></label>
					    <div class="col-sm-4">
							<select class="form-control" name="triggerType" >
		                		<#list jobTriggerTypeNoNull as item>
		                			<option value="${item.value}" >${item.description}</option>
		                		</#list>
			                </select>
						</div>
                       	
                       	<div name="cronDiv"  class="">
                       		<!-- 如果是cron触发，需要输入cron表达式 -->
                       		<label for="lastname" class="col-sm-2 control-label">CRON表达式<font color="black">*</font></label>
                        	<div class="col-sm-4"><input type="text" class="form-control" name="cronExpression" placeholder="请输入CRON表达式" maxlength="100" ></div>
                       	</div>
                       	
                       	<div name="triggerJobDiv" class="hide">
                       		<!-- 如果是上级任务触发，需要选择上级任务信息-->
                       		<label for="lastname" class="col-sm-2 control-label">上级任务ID<font color="black">*</font></label>
                        	<div class="col-sm-4"><input type="text" class="form-control" name="preJobId" placeholder="请输入上级触发任务的ID" maxlength="100" ></div>
                       	</div>
						
                    </div>
                    
                    
					<div class="form-group">
						<!--
						.是否日切任务
            			.日切偏移量
            			-->
					
						<label for="lastname" class="col-sm-2 control-label">日切任务<font color="red">*</font></label>
						<div class="col-sm-4">
							<select class="form-control" name="typeDaily" >
		                		<#list jobIsTypeDailyNoNull as item>
		                			<option value="${item.value}" >${item.description}</option>
		                		</#list>
			                </select>
						</div>
						
						<div name="offsetDiv" class="hide">
	                        <label for="lastname" class="col-sm-2 control-label">偏移量<font color="red">*</font></label>
	                        <div class="col-sm-4"><input type="text" class="form-control" name="offset" placeholder="-1表示执行时间减1为业务日期(txDate)" maxlength="50" ></div>
						</div>
					</div>
                    
                    
	            	<div class="form-group">
	            		<!--
		            	.设置依赖任务
		            	.任务描述
		            	-->
                       	<label for="lastname" class="col-sm-2 control-label">依赖任务<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="dependencyIds" placeholder="依赖任务ID，以逗号分隔，例如【1,2】" maxlength="100" ></div>
                       	<label for="lastname" class="col-sm-2 control-label">任务描述<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="任务描述说明" maxlength="100" ></div>
					</div>
                    <hr>
                    
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<input type="hidden" name="id" >
						</div>
					</div>
					
				</form>
         	</div>
		</div>
	</div>
</div>




<!-- 执行参数.模态框 -->
<div class="modal fade" id="triggerModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" name="triggerHeader" >CRON任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group" name="dailyParam">
                        <label for="lastname" class="col-sm-2 control-label">执行日期<font color="red">*</font></label>
                        <div class="col-sm-4">
                        	<input type="text" class="form-control" name="txDate" placeholder="格式 “yyyyMMdd”" maxlength="50" >
                        </div>
                        
					</div>
                   
                   	<div class="form-group hide" name="cronParam">
                        <label for="lastname" class="col-sm-2 control-label">任务起始日期<font color="red">*</font></label>
                        <div class="col-sm-4">
                        	<input type="text" class="form-control" name="flowLastTime" placeholder="格式“yyyyMMddHHmmss”" maxlength="50" >
                        </div>
                        <label for="lastname" class="col-sm-2 control-label">任务结束日期<font color="red">*</font></label>
                        <div class="col-sm-4">
                        	<input type="text" class="form-control" name="flowCurTime" placeholder="格式“yyyyMMddHHmmss”" maxlength="50" >
                        </div>
					</div>
                   
                   
                   <div class="form-group" >
                        <label for="lastname" class="col-sm-2 control-label">触发后续任务<font color="red">*</font></label>
                        <div class="col-sm-4">
                        	<select class="form-control" name="needTriggleNext" >
                				<option value="false" selected>否</option>
                				<option value="true" >是</option>
	                  		</select>
                        </div>
                        
					</div>
                   
                   
                   <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >执行</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="id" >
                            <input type="hidden" name="typeDaily" >
                            <input type="hidden" name="triggerType" >
                            
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>


<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/jobclient.index.1.js"></script>
</body>
</html>
