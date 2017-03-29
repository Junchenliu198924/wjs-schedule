$(function() {   
	
	// 搜索按钮
	$('#searchBtn').on('click', function(){

		var _id = $(this).attr('_id');
		
		// show
		
		$.ajax({
			type : 'POST',
			url : base_url + '/joblog/execview',
			data : {"logId":_id},
			dataType : "json",
			success : function(data){
				if (data.resultCode = "success") {
					
					// 增加当前任务信息
					var curJob = data.data.curJob;

					var preJob = data.data.preJob;
					var depJobs = data.data.depJobs;
					var nextJobs = data.data.nextJobs;
					
					if(curJob == null){
						ComAlert.show(2, "无法获得当前日志信息");
						return;
					}
					
					// 业务时间显示
					if(curJob.typeDaily == 'YES'){

						$('#triggerHeader').html("任务依赖关系，业务时间："+ curJob.txDate);
					}else{
						$('#triggerHeader').html("任务依赖关系，业务时间："+ curJob.jobEndTimeDesc+'~'+curJob.jobStartTimeDesc);
					}
					
					
					// 删除所有信息
					$('#modalContainer').html("");
					
					// 添加当前任务
					var curContainer = $('<div class="form-group  modal-header" > </div>' ) ;
					curContainer.append(createJob("当前任务",curJob));
					$('#modalContainer').append(curContainer);
					
					// 添加前置任务
					var preContainer = $('<div class="form-group  modal-header" > </div>');
					curContainer.before(preContainer);
					
					if(preJob != null){
						preContainer.append(createJob("触发任务",preJob));
					}
					if(depJobs != null && depJobs.length > 0){
						for(var i = 0 ; i < depJobs.length ; i++){
							preContainer.append(createJob("依赖任务",depJobs[i]));
						}
					}
					

					// 添加后续任务
					
					var nextContainer = $('<div class="form-group  modal-header" > </div>');
					curContainer.after(nextContainer);
					if(nextJobs != null && nextJobs.length > 0){
						for(var i = 0 ; i < nextJobs.length ; i++){
							nextContainer.append(createJob("后续任务",nextJobs[i]));
						}
					}
					
					
					//  显示
					$('#dependencyModal').modal({backdrop: false, keyboard: false}).modal('show');
				} else {
					ComAlert.show(2, data.resultMsg);
				}
			},
		});
	});
	
	function createJob(jobTitle, jobInfo){
		
		var html = '<div class="col-sm-4 form-group "> '+
			   			'<div> '+
			   			jobTitle +
						'</div> '+
			   			'<div> '+
			   			jobInfo.groupName + '-'+jobInfo.jobName+
						'</div> ';
		html +='<div> '+
			jobInfo.execJobStatusDesc
			+ jobInfo.jobStartTimeDesc +'<br/>'
			+ jobInfo.jobExecTimeDesc+'~'+jobInfo.jobEndTimeDesc+
				'</div> ';
		html +=	 '</div> ';
		
		
		return $(html);
	}
	
	
});
