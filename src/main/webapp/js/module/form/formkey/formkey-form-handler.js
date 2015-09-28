/**
 * 动态Form办理功能
 */
$(function() {

	$('.handle').click(handle);
	$('.taskDelegate').click(taskDelegate);

});

/**
 * 打开办理对话框
 */
function handle() {
	// alert("!");
	var $ele = $(this);

	// 当前节点的英文名称
	var tkey = $(this).attr('tkey');

	// 当前节点的中文名称
	var tname = $(this).attr('tname');

	// 任务ID
	var taskId = $(this).attr('tid');

	var processInstanceId = $ele.parents('tr').find('.process-instance-id')
			.text();
//	alert(processInstanceId);

	$('#handleTemplate').html('').dialog({
		modal : true,
		width : 600,
		height : $.common.window.getClientHeight() / 2,
		title : '办理任务[' + tname + ']',
		open : function() {
			readFormKey.call(this, taskId, processInstanceId);
		},
		buttons : [ {
			text : '提交',
			click : function() {
				$('.formkey-form').submit();
			}
		}, {
			text : '关闭',
			click : function() {
				$(this).dialog('close');
			}
		} ]
	});
	$('button').attr('class', 'btn btn-success');
}

/**
 * 读取外置任务表单
 */
function readFormKey(taskId, processInstanceId) {
	var dialog = this;

	// 读取启动时的表单
	$.get(ctx + '/form/formkey/get-form/task/' + taskId + '/processInstanceId/'
			+ processInstanceId, function(form) {
		// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
		$(dialog).html(form.formData).wrap(
				"<form class='formkey-form' method='post' />");

		var $form = $('.formkey-form');

		// 设置表单action
		$form.attr('action', ctx + '/form/formkey/task/complete/' + taskId
				+ '/singleType');

		// 初始化日期组件
		$form.find('.datetime').datetimepicker({
			stepMinute : 5
		});
		$form.find('.date').datepicker();

		var $table = $form.find('table');
		$table.attr('class', 'table table-hover');

		var $td = $('td');
		$td.attr('color', "#000000");

		// 表单验证
		$form.validate($.extend({}, $.common.plugin.validator));
	});
}

function taskDelegate() {

	var $ele = $(this);
	var formType = $ele.parents('tr').find('.process-id').text();
	var task_type = $ele.parents('tr').find('.task-type').text();
	var processInstanceId = $ele.parents('tr').find('.process-instance-id')
			.text();

	var $ele = $(this);

	// 当前节点的英文名称
	var tkey = $(this).attr('tkey');

	// 当前节点的中文名称
	var tname = $(this).attr('tname');

	// 任务ID
	var taskId = $(this).attr('tid');

	$('#handleTemplate').html('').dialog({
		modal : true,
		width : 600,
		height : $.common.window.getClientHeight() / 2,
		title : '请人代办任务[' + tname + ']',
		open : function() {
			readDelegateUserFormKey.call(this, taskId, processInstanceId);
		},
		buttons : [ {
			text : '提交',
			click : function() {
				$('.formkey-form').submit();
			}
		}, {
			text : '关闭',
			click : function() {
				$(this).dialog('close');
			}
		} ]
	});

	$('button').attr('class', 'btn btn-success');

}



function readDelegateUserFormKey(taskId, processInstanceId) {
	var dialog = this;

	var trs = "<tr><td width='30%' style='text-align:right;vertical-align:middle'>"
			+ "选择代办人："
			+ "</td><td style='vertical-align:middle'><select name='delegateUser'>";
	$.getJSON(ctx + '/workflow/userlist', function(datas) {

		$.each(datas, function() {
			var id = this.id;
			var name = this.firstName + " " + this.lastName;
			trs += "<option value='" + id + "'>" + name + "</option>";

		});

		trs += "</select></td></tr>";

		// 读取启动时的表单
		$.get(ctx + '/form/formkey/get-form/task/' + taskId
				+ '/processInstanceId/' + processInstanceId, function(form) {
			// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
			$(dialog).html(form.formData).wrap(
					"<form class='formkey-form' method='post' />");

			var $form = $('.formkey-form');

			// 设置表单action
			$form.attr('action', ctx + '/form/dynamic/delegate/task/' + taskId + '/singleType');

			// 初始化日期组件
			$form.find('.datetime').datetimepicker({
				stepMinute : 5
			});
			$form.find('.date').datepicker();

			$form.find('table').attr('class', 'table table-hover');
			$form.find('table').prepend(trs);

			var $td = $('td');
			$td.attr('color', "#000000");

			// 表单验证
			$form.validate($.extend({}, $.common.plugin.validator));
		});
	});
}

