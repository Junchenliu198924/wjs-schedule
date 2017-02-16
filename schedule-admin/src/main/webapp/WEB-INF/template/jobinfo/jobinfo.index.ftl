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
			<h1>调度管理<small>任务调度中心</small></h1>
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
	                	<span class="input-group-addon">分组名称</span>
                		<select class="form-control" id="jobGroupId" >
                			<#list jobGroupList as group>
                				<option value="${group.id}" >${group.groupName}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
                <div class="col-xs-4">
                    <div class="input-group">
	                	<span class="input-group-addon">执行应用</span>
	                	
                		<select class="form-control" id="jobClassApplication" >
                			<#list jobAppList?keys as app>
                				<option value="${app}" >${jobAppList[app]}</option>
                			</#list>
	                  	</select>
                    </div>
                </div>
                
                <div class="col-xs-4">
	              	<div class="input-group">
	                	<span class="input-group-addon">任务ID</span>
                		<input type="text" class="form-control" id="jobId" value="${id}" autocomplete="on" >
	              	</div>
	            </div>
          	</div>
	    	
	    	<div class="row">
	    		<div class="col-xs-4">
	              	<div class="input-group">
	                	<span class="input-group-addon">任务状态</span>
                		<select class="form-control" id="jobStatus" >
                			<#list jobStatusList as list>
                				<option value="${list.value}" >${list.description}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
                <div class="col-xs-4">
                    <div class="input-group">
	                	<span class="input-group-addon">执行状态</span>
                		<select class="form-control" id="jobExecStatus" >
                			<#list jobExecStatus as list>
                				<option value="${list.value}" >${list.description}</option>
                			</#list>
	                  	</select>
                    </div>
                </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">+新增任务</button>
	            </div>
          	</div>
          	
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header">
			            	<h3 class="box-title">调度列表</h3>
			            </div>
			            <div class="box-body">
			              	<table id="job_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
					            		<th name="id" >id</th>
					                	<th name="groupId" >分组ID</th>
					                	<th name="groupName" >分组名称</th>
					                  	<th name="jobName" >任务名称</th> 
					                  	<th name="jobClassApplication" >执行应用</th>
					                  	<th name="jobDesc" >描述</th> 
					                  	<th name="triggerType" >触发类型</th>
					                  	<th name="cronExpression" >Cron</th> 
					                  	<th name="typeDaily" >是否日切任务</th> 
					                  	<th name="txDate" >业务日期</th> 
					                  	<th name="offset" >业务偏移日期</th> 
					                  	<th name="jobStatus" >任务状态</th> 
					                  	<th name="cuckooParallelJobArgs" >任务参数</th> 
					                  	<th name="execJobStatus" >执行状态</th>
					                  	<th name="flowLastTime" >流式任务上一次时间参数</th>
					                  	<th name="flowCurTime" >流式任务当前时间参数</th> 
					                  	<th>操作</th>
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
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">执行器<font color="red">*</font></label>
						<div class="col-sm-4">
							<select class="form-control" name="jobGroup" >
		            			<#list JobGroupList as group>
		            				<option value="${group.id}" >${group.title}</option>
		            			</#list>
		                  	</select>
						</div>
                        <label for="lastname" class="col-sm-2 control-label">任务描述<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="请输入“描述”" maxlength="50" ></div>
					</div>
					<div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <div class="input-group">
								<input type="text" class="form-control" name="executorHandler" placeholder="请输入“JobHandler”" maxlength="100" >
								<span class="input-group-addon"><b>GLUE</b>&nbsp;<input type="checkbox" class="ifGLUE" ></span>
                                <input type="hidden" name="glueSwitch" value="0" >
                            </div>
						</div>
						<label for="firstname" class="col-sm-2 control-label">执行参数<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorParam" placeholder="请输入“执行参数”" maxlength="100" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">Cron<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobCron" placeholder="请输入“Cron”" maxlength="20" ></div>
                        <label for="lastname" class="col-sm-2 control-label">子任务Key<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="childJobKey" placeholder="请输入子任务的任务Key,如存在多个逗号分隔" maxlength="100" ></div>
                    </div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">报警邮件<font color="red">*</font></label>
						<div class="col-sm-4"><input type="text" class="form-control" name="alarmEmail" placeholder="请输入“报警邮件”，多个邮件地址逗号分隔" maxlength="100" ></div>
                        <label for="lastname" class="col-sm-2 control-label">负责人<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="请输入“负责人”" maxlength="50" ></div>
					</div>
                    <hr>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</div>

<input type="hidden" name="glueRemark" value="GLUE代码初始化" >
<textarea name="glueSource" style="display:none;" >
package com.xxl.job.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xxl.job.core.handler.IJobHandler;

public class DemoGlueJobHandler extends IJobHandler {
	private static transient Logger logger = LoggerFactory.getLogger(DemoGlueJobHandler.class);

	@Override
	public void execute(String... params) throws Exception {
		logger.info("XXL-JOB, Hello World.");
	}

}
</textarea>
					
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新任务</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">执行器<font color="red">*</font></label>
                        <div class="col-sm-4">
							<input type="text" class="form-control jobGroupTitle" maxlength="50" readonly >
						</div>
                        <label for="lastname" class="col-sm-2 control-label">任务描述<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="请输入“描述”" maxlength="50" ></div>
					</div>
                    <div class="form-group">
                        <label for="firstname" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
                        <div class="col-sm-4">
                            <div class="input-group">
                                <input type="text" class="form-control" name="executorHandler" placeholder="请输入“JobHandler”" maxlength="100" >
                                <span class="input-group-addon"><b>GLUE</b>&nbsp;<input type="checkbox" class="ifGLUE" ></span>
                                <input type="hidden" name="glueSwitch" value="0" >
                            </div>
                        </div>
                        <label for="firstname" class="col-sm-2 control-label">执行参数<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="executorParam" placeholder="请输入“执行参数”" maxlength="100" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">Cron<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="jobCron" placeholder="请输入“Cron”" maxlength="20" ></div>
                        <label for="lastname" class="col-sm-2 control-label">子任务Key<font color="black">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="childJobKey" placeholder="请输入子任务的任务Key,如存在多个逗号分隔" maxlength="100" ></div>
                    </div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">报警邮件<font color="red">*</font></label>
						<div class="col-sm-4"><input type="text" class="form-control" name="alarmEmail" placeholder="请输入“报警邮件”，多个邮件地址逗号分隔" maxlength="100" ></div>
                        <label for="lastname" class="col-sm-2 control-label">负责人<font color="red">*</font></label>
                        <div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="请输入“负责人”" maxlength="50" ></div>
					</div>
					<hr>
					<div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="jobGroup" >
                            <input type="hidden" name="jobName" >
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
<script src="${request.contextPath}/static/js/jobinfo.index.1.js"></script>
</body>
</html>
