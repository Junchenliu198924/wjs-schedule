$(function() {
	// init date tables
	var jobTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
			url: base_url + "/jobinfo/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.groupId = $('#groupId').val();
	        	obj.jobClassApplication = $('#jobClassApplication').val();
	        	obj.jobId = $('#jobId').val();
	        	obj.jobStatus = $('#jobStatus').val();
	        	obj.jobExecStatus = $('#jobExecStatus').val();
	        	obj.start = d.start;
	        	obj.limit = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// X轴滚动条，取消自适应
	    "columns": [
	                { "data": 'id', "bSortable": false, "visible" : true}, 
	                { "data": 'groupId', "bSortable": false, "visible" : false},
	                { 
	                	"data": 'groupId'
	                	, "visible" : true 
	                	,"render": function ( data, type, row ) {
	            			var groupMenu = $("#groupId").find("option");
	            			for ( var index in $("#groupId").find("option")) {
	            				if ($(groupMenu[index]).attr('value') == data) {
									return $(groupMenu[index]).html();
								}
							}
	            			return data;
	            		}
            		},
					{ "data": 'jobName', "visible" : true},
	                { "data": 'jobClassApplication', "visible" : true},
	                { "data": 'jobDesc', "visible" : false},
	                { "data": 'triggerType', "visible" : true},
	                { "data": 'cronExpression', "visible" : true},
	                { "data": 'typeDaily', "visible" : true},
	                
//	                { "data": 'txDate', "visible" : false},
	                { "data": 'offset', "visible" : false},
	                { "data": 'jobStatus', "visible" : true},
	               
					 
//	                { 
//	                	"data": 'updateTime', 
//	                	"visible" : false, 
//	                	"render": function ( data, type, row ) {
//	                		return data?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
//	                	}
//	               },
	               
//	                { 
//	                	"data": 'jobStatus', 
//	                	"visible" : true,
//	                	"render": function ( data, type, row ) {
//	                		if ('NORMAL' == data) {
//	                			return '<small class="label label-success" ><i class="fa fa-clock-o"></i>'+ data +'</small>'; 
//							} else if ('PAUSED' == data){
//								return '<small class="label label-default" title="暂停" ><i class="fa fa-clock-o"></i>'+ data +'</small>'; 
//							} else if ('BLOCKED' == data){
//								return '<small class="label label-default" title="阻塞[串行]" ><i class="fa fa-clock-o"></i>'+ data +'</small>'; 
//							}
//	                		return data;
//	                	}
//	                },
	                { "data": 'cuckooParallelJobArgs', "visible" : true},
//	                { "data": 'execJobStatus', "visible" : true},
//	                { 
//	                	"data": 'flowLastTime', 
//	                	"visible" : false ,
//	                	"render": function ( data, type, row ){
//	                		return data != 0 ?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
//	                	}
//	                },
//	                { 
//	                	"data": 'flowCurTime', 
//	                	"visible" : false ,
//	                	"render": function ( data, type, row ){
//	                		return data != 0 ?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
//	                	}
//	                },
	                
	                { "data": '操作' ,
	                	"render": function ( data, type, row ) {
	                		return function(){
	                			// status
	                			var pause_resume = "";
	                			if ('RUNNING' == row.jobStatus) {
	                				pause_resume = '<button class="btn btn-primary btn-xs job_operate" type="job_pause" type="button">暂停</button>  ';
								} else if ('PAUSE' == row.jobStatus){
									pause_resume = '<button class="btn btn-primary btn-xs job_operate" type="job_resume" type="button">恢复</button>  ';
								}
	                			// log url
	                			var logUrl = base_url +'/joblog?jobGroup='+ row.jobGroup +'&jobName='+ row.jobName;
	                			
	                			// log url
	                			var codeBtn = "";
	                			if(row.glueSwitch > 0){
									var codeUrl = base_url +'/jobcode?jobGroup='+ row.jobGroup +'&jobName='+ row.jobName;
									codeBtn = '<button class="btn btn-warning btn-xs" type="button" onclick="javascript:window.open(\'' + codeUrl + '\')" >GLUE</button>  '
								}
	                			
	                			var groupName = "";
	                			var groupMenu = $("#groupId").find("option");
		            			for ( var index in $("#groupId").find("option")) {
		            				if ($(groupMenu[index]).attr('value') == row.groupId) {
		            					groupName = $(groupMenu[index]).html();
		            					break;
									}
								}
								// html
								var html = '<p id="'+ row.id +'" '+
								' groupName="'+ groupName +'" '+
								' groupId="'+ row.groupId +'" '+
								' jobName="'+ row.jobName +'" '+
								' jobClassApplication="'+ row.jobClassApplication +'" '+
								' jobDesc="'+ row.jobDesc +'" '+
								' triggerType="'+ row.triggerType +'" '+
								' cronExpression="'+ row.cronExpression +'" '+
								' typeDaily="'+ row.typeDaily +'" '+
								' offset="'+ row.offset +'" '+
								' jobStatus="'+ row.jobStatus +'" '+
								' cuckooParallelJobArgs="'+ row.cuckooParallelJobArgs +'" '+
//								' txDate="'+ row.txDate +'" '+
//								' execJobStatus="'+ row.execJobStatus +'" '+
//								' flowLastTime="'+ row.flowLastTime +'" '+
//								' flowCurTime="'+ row.flowCurTime +'" '+
								'>'+
								'<button class="btn btn-primary btn-xs trigger"  type="button">执行</button>  '+
								pause_resume +
								'<button class="btn btn-primary btn-xs" type="job_del" type="button" onclick="javascript:window.open(\'' + logUrl + '\')" >日志</button><br>  '+
								'<button class="btn btn-warning btn-xs update" type="button">编辑</button>  '+
								codeBtn +
								'<button class="btn btn-danger btn-xs job_operate" type="job_del" type="button">删除</button>  '+
								'</p>';

	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});
	
	// 搜索按钮
	$('#searchBtn').on('click', function(){
		jobTable.fnDraw();
	});
	
	$(".box-header :button").on('click', function(){
		
		var url;
		var type = $(this).attr("type");
		if ("job_pause_all" == type) {
			typeName = "暂停全部";
			url = base_url + "/jobinfo/paushAll";
		} else if ("job_resume_all" == type) {
			typeName = "回复全部";
			url = base_url + "/jobinfo/resumeAll";
		}
		
		ComConfirm.show("确认" + typeName, function(){
			$.ajax({
				type : 'POST',
				url : url,
				dataType : "json",
				success : function(data){
					if (data.resultCode == "success") {
						ComAlert.show(1, typeName + data.resultMsg, function(){
							jobTable.fnDraw();
						});
					} else {
						ComAlert.show(1, typeName + "失败," + data.resultMsg);
					}
				},
			});
		});
	});
	
  
	
	
	// job operate
	$("#job_list").on('click', '.job_operate',function() {
		var typeName;
		var url;
		var needFresh = false;

		var type = $(this).attr("type");
		
		if ("job_pause" == type) {
			typeName = "暂停";
			url = base_url + "/jobinfo/pause";
			needFresh = true;
		} else if ("job_resume" == type) {
			typeName = "恢复";
			url = base_url + "/jobinfo/resume";
			needFresh = true;
		} else if ("job_del" == type) {
			typeName = "删除";
			url = base_url + "/jobinfo/remove";
			needFresh = true;
//		} else if ("job_trigger" == type) {
//			typeName = "执行";
//			url = base_url + "/jobinfo/trigger";
//			return;
		} else {
			
			return;
		}
		
		var id = $(this).parent('p').attr("id");
		var groupName = $(this).parent('p').attr("groupName");
		var jobName = $(this).parent('p').attr("jobName");
		
		ComConfirm.show("确认" + typeName +" ["+ groupName +"]-[" +jobName+ "]?", function(){
			$.ajax({
				type : 'POST',
				url : url,
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.resultCode == "success") {
						ComAlert.show(1, typeName + data.resultMsg, function(){
							if (needFresh) {
								//window.location.reload();
								jobTable.fnDraw();
							}
						});
					} else {
						ComAlert.show(1, typeName + "失败," + data.resultMsg);
					}
				},
			});
		});
	});
	
	// jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
	jQuery.validator.addMethod("myValid01", function(value, element) {
		var length = value.length;
		var valid = /^[a-zA-Z][a-zA-Z0-9_]*$/;
		return this.optional(element) || valid.test(value);
	}, "只支持英文字母开头，只含有英文字母、数字和下划线");
	
	// 新增
	$(".add").click(function(){
		
		$('#editModal').modal({backdrop: false, keyboard: false}).modal('show');
		// 修改标题
		$("#editModal .modal-header h4[name='title']").html("新增任务");
		$("#editModal .form input[name='id']").val("");
		
	});
	var editModalValidate = $("#editModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
			jobDesc : {
				required : true,
				maxlength: 50
			},
            jobCron : {
            	required : true
            },
			executorHandler : {
				required : false
			},
            alarmEmail : {
            	required : true
            },
			author : {
				required : true
			}
        }, 
        messages : {  
            jobDesc : {
            	required :"请输入“描述”."
            },
            jobCron : {
            	required :"请输入“Cron”."
            },
			executorHandler : {
				required : "请输入“jobHandler”."
			},
            alarmEmail : {
            	required : "请输入“报警邮件”."
            },
            author : {
            	required : "请输入“负责人”."
            }
        },
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
        	$.post(base_url + "/jobinfo/add",  $("#editModal .form").serialize(), function(data, status) {
        		if (data.resultCode == "success") {
					$('#editModal').modal('hide');
					setTimeout(function () {
						ComAlert.show(1, "新增任务成功", function(){
							jobTable.fnDraw();
							//window.location.reload();
						});
					}, 315);
    			} else {
    				ComAlert.show(2, "新增失败"+ data.resultMsg);
    			}
    		});
		}
	});
	$("#editModal").on('hide.bs.modal', function () {
		$("#editModal .form")[0].reset();
		editModalValidate.resetForm();
		$("#editModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

		$("#editModal .form input[name='executorHandler']").removeAttr("readonly");
	});

	
	

	// 编辑框触发类型监听
	$("#editModal .form select[name='triggerType']").change(function(){
		var triggerType = $("#editModal .form select[name='triggerType']").val();
		if("CRON" == triggerType){
			$("#editModal .form div[name='cronDiv']").removeClass("hide");
			$("#editModal .form div[name='triggerJobDiv']").addClass("hide");
		}else if("JOB" == triggerType){
//			,triggerJobDiv
			$("#editModal .form div[name='cronDiv']").addClass("hide");
			$("#editModal .form div[name='triggerJobDiv']").removeClass("hide");
		}else{
			alert("unknow trigger type!!!");
		}
	});
	

	// 编辑框是否为日切任务监听
	$("#editModal .form select[name='typeDaily']").change(function(){
		var typeDaily = $("#editModal .form select[name='typeDaily']").val();
		if("NO" == typeDaily){
			$("#editModal .form div[name='offsetDiv']").addClass("hide");
			$("#editModal .form input[name='offset']").val("");
		}else if("YES" == typeDaily){
//			,triggerJobDiv
			$("#editModal .form div[name='offsetDiv']").removeClass("hide");
		}else{
			alert("unknow typeDaily!!!");
		}
	});
	
	
	
	// 更新
	$("#job_list").on('click', '.update',function() {
		// 修改标题
		$("#editModal .modal-header h4[name='title']").html("修改任务");
		$("#editModal .form input[name='id']").val($(this).parent('p').attr("id"));
		// base data
		$("#editModal .form select[name='groupId']").val($(this).parent('p').attr("groupId"));
		$("#editModal .form select[name='jobClassApplication']").val($(this).parent('p').attr("jobClassApplication"));
		
		$("#editModal .form input[name='jobName']").val($(this).parent('p').attr("jobName"));
		$("#editModal .form input[name='cuckooParallelJobArgs']").val($(this).parent('p').attr("cuckooParallelJobArgs"));
		
		$("#editModal .form input[name='jobCron']").val($(this).parent('p').attr("jobCron"));
		$("#editModal .form input[name='author']").val($(this).parent('p').attr("author"));

		$("#editModal .form select[name='triggerType']").val($(this).parent('p').attr("triggerType")).trigger('change');
//		$("#editModal .form select[name='triggerType']").find("option[value='"+$(this).parent('p').attr("triggerType")+"']").attr("selected",true);
		
		$("#editModal .form input[name='cronExpression']").val($(this).parent('p').attr("cronExpression"));
		$.post(base_url + "/jobinfo/getPreJobIdByJobId", 
			{"jobId" : $(this).parent('p').attr("id")}
			, function(data, status) {
				if (data.resultCode == "success") {
					$("#editModal .form input[name='preJobId']").val(data.data); 
				}  
		});
		
		
		$("#editModal .form select[name='typeDaily']").val($(this).parent('p').attr("typeDaily")).trigger('change');
		$("#editModal .form input[name='offset']").val($(this).parent('p').attr("offset")); 

        $.post(base_url + "/jobinfo/getDependencyIdsByJobId", 
    			{"jobId" : $(this).parent('p').attr("id")}
    			, function(data, status) {
    				if (data.resultCode == "success") {
    			        $("#editModal .form input[name='dependencyIds']").val(data.data); 
    				}  
    		});
        $("#editModal .form input[name='jobDesc']").val($(this).parent('p').attr("jobDesc"));

		// jobGroupTitle
		var jobGroupTitle = $("#editModal .form select[name='jobGroup']").find("option[value='" + $(this).parent('p').attr("jobGroup") + "']").text();
		$("#editModal .form .jobGroupTitle").val(jobGroupTitle);


		// show
		$('#editModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	
	
	
	
	// 执行
	$("#job_list").on('click', '.trigger',function() {
		
		// header - 
		$("#triggerModal .form input[name='triggerHeader']").html($(this).parent('p').attr("typeDaily") + " 任务");
		if("YES" == $(this).parent('p').attr("typeDaily")){
			$("#triggerModal .form div[name='dailyParam']").removeClass("hide");
			$("#triggerModal .form div[name='cronParam']").addClass("hide");
		}else{
			$("#triggerModal .form div[name='dailyParam']").addClass("hide");
			$("#triggerModal .form div[name='cronParam']").removeClass("hide");
		}
		// base data
		$("#triggerModal .form input[name='id']").val($(this).parent('p').attr("id"));
		$("#triggerModal .form input[name='triggerType']").val($(this).parent('p').attr("triggertype"));
		$("#triggerModal .form input[name='typeDaily']").val($(this).parent('p').attr("typeDaily"));
		

		// show
		$('#triggerModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var triggerModalValidate = $("#triggerModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,

		
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			// post
    		$.post(base_url + "/jobinfo/trigger", $("#triggerModal .form").serialize(), function(data, status) {
    			if (data.resultCode == "success") {
					$('#triggerModal').modal('hide');
					setTimeout(function () {
						ComAlert.show(1, "添加执行完成", function(){
							//window.location.reload();
							jobTable.fnDraw();
						});
					}, 315);
    			} else {
    				if (data.resultMsg) {
    					ComAlert.show(2, data.resultMsg);
					} else {
						ComAlert.show(2, "更新失败");
					}
    			}
    		});
		}
	});
	$("#triggerModal").on('hide.bs.modal', function () {
		$("#triggerModal .form")[0].reset()
	});
	

});
