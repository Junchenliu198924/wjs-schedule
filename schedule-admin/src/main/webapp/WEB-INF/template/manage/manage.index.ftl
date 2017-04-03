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
			<h1>调度管理<small>管理台</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">
			
			 在这里管理用户啦，哈哈哈。在分组管理里面管理权限啦，哈哈哈
			 <br/>
			 设置admin用户，可以查看管理所有权限
			 <br/>
			 其他用户权限放在groupId里面，通过拦截器中增加groupIds的方式处理。
			 <br/>
			 通过统一的URL规则来进行操作级别的权限管理，例如：
			 <br/>
			 查询：pageList,新增 add,修改：modify，addOrModify，delete。对操作管理（可读、可写管理）
			 <br/>
			 最后，分组下拉框，也需要进行权限管理
	    </section>
	</div>

     
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<!-- DataTables -->
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/js/workstudio.index.1.js"></script>
</body>
</html>
