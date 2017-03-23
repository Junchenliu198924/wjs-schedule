$(function() {

	// 任务组列表选中, 任务列表初始化和选中
    var ifParam = true;
	$("#groupId").on("change", function () {

		var groupId = $(this).children('option:selected').val();
		$.ajax({
			type : 'POST',
            async: false,   // async, avoid js invoke pagelist before jobName data init
			url : base_url + '/joblog/getJobsByGroup',
			data : {"groupId":groupId},
			dataType : "json",
			success : function(data){
				if (data.resultCode == "success") {
					$("#jobId").html('<option value="" >请选择</option>');
                        $.each(data.data, function (n, value) {
                        $("#jobId").append('<option value="' + value.id + '" >' +value.jobName +"【"+ value.jobDesc + '】</option>');
                    });
                    if ($("#jobId").attr("paramVal")){
                        $("#jobId").find("option[value='" + $("#jobId").attr("paramVal") + "']").attr("selected",true);
                        $("#jobId").attr("paramVal")
                    }
				} else {
					ComAlertTec.show(data.resultMsg);
				}
			},
		});
	});
	
	
	if ($("#groupId").attr("paramVal")){
		$("#groupId").find("option[value='" + $("#groupId").attr("paramVal") + "']").attr("selected",true);
        $("#groupId").change();
        $("#groupId").attr("")
	}

	// 过滤时间
	$('#filterTime').daterangepicker({
		timePicker: true, 			//是否显示小时和分钟
		timePickerIncrement: 10, 	//时间的增量，单位为分钟
		timePicker12Hour : false,	//是否使用12小时制来显示时间
		format: 'YYYY-MM-DD HH:mm:ss',
		separator : ' - ',
		ranges : {
            '最近1小时': [moment().subtract('hours',1), moment()],
            '今日': [moment().startOf('day'), moment()],
            '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
            '最近7日': [moment().subtract('days', 6), moment()],
            '最近30日': [moment().subtract('days', 29), moment()]
        },
        opens : 'left', //日期选择框的弹出位置
        locale : {
        	customRangeLabel : '自定义',
            applyLabel : '确定',
            cancelLabel : '取消',
            fromLabel : '起始时间',
            toLabel : '结束时间',
            daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
            monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
            firstDay : 1
        }
	});
	$('#filterTime').val( moment(new Date()).add(-30, 'days').format("YYYY-MM-DD 00:00:00") + ' - ' + moment(new Date()).add(1, 'days').format("YYYY-MM-DD 00:00:00") );	// YYYY-MM-DD HH:mm:ss
	
	// init date tables
	var logTable = $("#joblog_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
	        url: base_url + "/joblog/pageList" ,
	        data : function ( d ) {
	        	var obj = {};
	        	obj.groupId = $('#groupId').val();
	        	obj.jobId = $('#jobId').val();
	        	obj.logId = $('#logId').val();
				obj.filterTime = $('#filterTime').val();
	        	obj.start = d.start;
	        	obj.limit = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": false,
	    "columns": [
	                { "data": 'id', "bSortable": false, "visible" : true},
	                { "data": 'groupId', "bSortable": false, "visible" : false},
	                { "data": 'jobId', "bSortable": false, "visible" : true},	                 
	                { "data": 'jobName', "visible" : true},
	                { "data": 'jobClassApplication', "visible" : false},
	                { "data": 'triggerType', "visible" : true},
					

	                { "data": 'cronExpression', "visible" : true},
	                { "data": 'execJobStatus', "visible" : true},
	                { 
	                	"data": 'jobStartTime', 
	                	"render": function ( data, type, row ) {
	                		return data != 0 ?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	}
	                },
	                { 
	                	"data": 'jobEndTime', 
	                	"render": function ( data, type, row ) {
	                		return data!= 0 ?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	}
	                },
	                { "data": 'needTriggleNext'},
	                { "data": 'forceTriggle'},
	                { "data": 'remark', "visible" : false},
	                
	                
//	                { 
//	                	"data": 'triggerMsg',
//	                	"render": function ( data, type, row ) {
//	                		return data?'<a class="logTips" href="javascript:;" >查看<span style="display:none;">'+ data +'</span></a>':"无";
//	                	}
//	                },
	                
	                
	                


	                { "data": 'cuckooClientIp'},
	                { "data": 'cuckooClientTag', "visible" : false},
	                { "data": 'typeDaily', "visible" : true},
	                { "data": 'txDate',
	                	"render": function ( data, type, row ) {
	                		return data == 0 ? "" : data;
	                	}, 
	                	"visible" : true
	                },
	                { 
	                	"data": 'flowLastTime',
	                	"render": function ( data, type, row ) {
	                		return data!=0?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	}, 
	                	"visible" : true
	                },
	                { 
	                	"data": 'flowCurTime',
	                	"render": function ( data, type, row ) {
	                		return data!=0?moment(new Date(data)).format("YYYY-MM-DD HH:mm:ss"):"";
	                	},
	                	"visible" : true
	                },

	                { "data": 'cuckooParallelJobArgs',"bSortable": false, "visible" : false},
	                
//	                { 
//	                	"data": 'handleMsg',
//	                	"render": function ( data, type, row ) {
//	                		return data?'<a class="logTips" href="javascript:;" >查看<span style="display:none;">'+ data +'</span></a>':"无";
//	                	}
//	                },
	                { "data": 'handleMsg' , "bSortable": false,
	                	"render": function ( data, type, row ) {
	                		// better support expression or string, not function
	                		return function () {

//	                			PENDING("PENDING", "等待执行"), 
//	                			RUNNING("RUNNING", "正在执行"), 
//	                			FAILED("FAILED", "执行失败"),
//	                			SUCCED("SUCCED", "执行成功"),
//	                			BREAK("BREAK", "断线");
	                			var temp = '<button class="btn btn-primary btn-xs detail"  type="button"  _id="'+ row.id +'" _execJobStatus="'+ row.execJobStatus+'"">详情</button> &nbsp; &nbsp;';

	                				temp += '<button class="btn btn-primary btn-xs redo"  type="button"  _id="'+ row.id +'" _execJobStatus="'+ row.execJobStatus+'">再次执行</button>';	
	                			if (row.execJobStatus != 'SUCCED'){	                			
//		                			if (row.execJobStatus == 'RUNNING'){
//		                				
//		                				temp += '<button class="btn btn-primary btn-xs redo"  type="button"   _id="'+ row.id +'">强制结束</button>';		                			
//		                			}else{
		                				
		                				temp += '<button class="btn btn-primary btn-xs reset"  type="button"   _id="'+ row.id +'" _execJobStatus="'+ row.execJobStatus+'">修改为成功</button>';		                			
//		                			}
		                		}
		                		
		                		return temp;	
	                		}
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
		logTable.fnDraw();
	});
	
	
	 
	$('#joblog_list').on('click', '.redo', function(){
//		var msg = $(this).find('span').html();
//		ComAlert.show(2, msg);
		// 再次执行
		var _id = $(this).attr('_id');
		var _execJobStatus = $(this).attr('_execJobStatus');
		ComConfirm.show("确认将任务"+_id+"【"+_execJobStatus+"】重新执行吗？确认后会新增一条执行信息", function(){
			$.ajax({
				type : 'POST',
				url : base_url + '/joblog/redo',
				data : {"logId":_id},
				dataType : "json",
				success : function(data){
					if (data.resultCode = "success") {
						ComAlert.show(1, '操作成功');
						logTable.fnDraw();
					} else {
						ComAlert.show(2, data.resultMsg);
					}
				},
			});
		});
	});
	 
	$('#joblog_list').on('click', '.reset', function(){
//		var msg = $(this).find('span').html();
//		ComAlertTec.show(msg);
		// 修改为成功
		var _id = $(this).attr('_id');
		var _execJobStatus = $(this).attr('_execJobStatus');
		ComConfirm.show("确认将任务"+_id+"【"+_execJobStatus+"】状态重置成功吗？", function(){
			$.ajax({
				type : 'POST',
				url : base_url + '/joblog/reset',
				data : {"logId":_id},
				dataType : "json",
				success : function(data){
					if (data.resultCode = "success") {
						ComAlert.show(1, '操作成功');
						logTable.fnDraw();
					} else {
						ComAlert.show(2, data.resultMsg);
					}
				},
			});
		});
	});
	
	// 查看执行器详细执行日志
	$('#joblog_list').on('click', '.detail', function(){
		var _id = $(this).attr('_id');
		
		window.open(base_url + '/joblog/logdetail?logId=' + _id);
		return;
		
		
	});
	
//	$('#joblog_list').on('click', '.logKill', function(){
//		var _id = $(this).attr('_id');
//		ComConfirm.show("确认主动终止任务?", function(){
//			$.ajax({
//				type : 'POST',
//				url : base_url + '/joblog/logKill',
//				data : {"id":_id},
//				dataType : "json",
//				success : function(data){
//					if (data.code == 200) {
//						ComAlert.show(1, '操作成功');
//						logTable.fnDraw();
//					} else {
//						ComAlert.show(2, data.msg);
//					}
//				},
//			});
//		});
//	});
	
});
