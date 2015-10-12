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

	var $ele = $(this);
	var formType = $ele.parents('tr').find('.process-id').text();
	var task_type = $ele.parents('tr').find('.task-type').text();
	var processInstanceId = $ele.parents('tr').find('.process-instance-id')
			.text();
	if (formType.indexOf('formkey') > 0) {
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
	} else {
		var $ele = $(this);

		// 当前节点的英文名称
		var tkey = $(this).attr('tkey');

		// 当前节点的中文名称
		var tname = $(this).attr('tname');

		// 任务ID
		var taskId = $(this).attr('tid');

		$('#handleTemplate').html('').dialog(
				{
					modal : true,
					width : 600,
					height : $.common.window.getClientHeight() / 2,
					title : '办理任务[' + tname + ']',
					open : function() {
						readFormFields.call(this, taskId, task_type,
								processInstanceId);
					},
					buttons : [ {
						text : '提交',
						click : function() {
							$('.dynamic-form').submit();
						}
					}, {
						text : '关闭',
						click : function() {
							$(this).dialog('close');
						}
					} ]
				});

	}
	$('button').attr('class', 'btn btn-success');

}

function taskDelegate() {

	var $ele = $(this);
	var formType = $ele.parents('tr').find('.process-id').text();
	var task_type = $ele.parents('tr').find('.task-type').text();
	var processInstanceId = $ele.parents('tr').find('.process-instance-id')
			.text();
	if (formType.indexOf('formkey') > 0) {
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
	} else {
		var $ele = $(this);

		// 当前节点的英文名称
		var tkey = $(this).attr('tkey');

		// 当前节点的中文名称
		var tname = $(this).attr('tname');

		// 任务ID
		var taskId = $(this).attr('tid');

		$('#handleTemplate').html('').dialog(
				{
					modal : true,
					width : 600,
					height : $.common.window.getClientHeight() / 2,
					title : '请人办理任务[' + tname + ']',
					open : function() {
						readDelegateUser.call(this, taskId, task_type,
								processInstanceId);
					},
					buttons : [ {
						text : '提交',
						click : function() {
							$('.dynamic-form').submit();
						}
					}, {
						text : '关闭',
						click : function() {
							$(this).dialog('close');
						}
					} ]
				});

	}
	$('button').attr('class', 'btn btn-success');

}

/**
 * 读取表单字段
 */
function readFormFields(taskId, task_type, processInstanceId) {
	var dialog = this;

	// 清空对话框内容
	$(dialog)
			.html(
					"<form class='dynamic-form' method='post'><table class='dynamic-form-table table table-hover'></table></form>");
	var $form = $('.dynamic-form');

	// 设置表单提交id
	$form.attr('action', ctx + '/form/dynamic/task/complete/' + taskId + '/'
			+ task_type);

	// 读取启动时的表单

	$.getJSON(
					ctx + '/form/dynamic/get-form/task/' + taskId
							+ '/processInstanceId/' + processInstanceId,
					function(datas) {
						var trs = "";
						trs += "<tr><td width='30%' style='text-align:right;vertical-align:middle'>申请人：</td><td style='text-align:left;padding-left:15px;vertical-align:middle'>"
								+ datas.applicant + "</td>+</tr>";
						// alert(JSON.stringify(datas));
						$.each(datas.taskFormData.formProperties, function() {

							var className = this.required === true ? "required"
									: "";
							this.value = this.value ? this.value : "";
							trs += "<tr>"
									+ createFieldHtml(this, datas, className)
							if (this.required === true) {
								trs += "<span style='color:red'>*</span>";
							}
							trs += "</td></tr>";
						});

						// 添加table内容
						$('.dynamic-form-table').html(trs).find('tr').hover(
								function() {
									$(this).addClass('ui-state-hover');
								}, function() {
									$(this).removeClass('ui-state-hover');
								});

						// 初始化日期组件
						$form.find('.date').datepicker();

						// 表单验证
						$form.validate($.extend({}, $.common.plugin.validator));
					});
}

/**
 * form对应的string/date/long/enum/boolean类型表单组件生成器 fp_的意思是form paremeter
 */
var formFieldCreator = {
	'string' : function(prop, datas, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle''>"
				+ prop.name + "：</td>";
		if (prop.writable === true) {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text' id='"
					+ prop.id
					+ "' name='fp_"
					+ prop.id
					+ "' class='"
					+ className + "' value='" + prop.value + "' />";
		} else {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'>"
					+ prop.value;
		}
		return result;
	},
	'date' : function(prop, datas, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name + "：</td>";
		if (prop.writable === true) {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text' id='"
					+ prop.id
					+ "' name='fp_"
					+ prop.id
					+ "' class='date "
					+ className + "' value='" + prop.value + "'/>";
		} else {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'>"
					+ prop.value;
		}
		return result;
	},
	'enum' : function(prop, datas, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name + "：</td>";
		if (prop.writable === true) {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'><select id='"
					+ prop.id
					+ "' name='fp_"
					+ prop.id
					+ "' class='"
					+ className + "'>";
			$.each(datas[prop.id], function(k, v) {
				result += "<option value='" + k + "'>" + v + "</option>";
			});
			result += "</select>";
		} else {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'>"
					+ prop.value;
		}
		return result;
	},
	'users' : function(prop, datas, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name + "：</td>";
		if (prop.writable === true) {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text' id='"
					+ prop.id
					+ "' name='fp_"
					+ prop.id
					+ "' class='"
					+ className + "' value='" + prop.value + "' />";
		} else {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'>"
					+ prop.value;
		}
		return result;
	}
};

/**
 * 生成一个field的html代码
 */
function createFieldHtml(prop, className) {
	return formFieldCreator[prop.type.name](prop, className);
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
				+ '/allType');

		// 初始化日期组件
		$form.find('.datetime').datetimepicker({
			stepMinute : 5
		});
		$form.find('.date').datepicker();

		$form.find('table').attr('class', 'table table-hover');

		var $td = $('td');
		$td.attr('color', "#000000");

		// 表单验证
		$form.validate($.extend({}, $.common.plugin.validator));
	});
}

/**
 * 读取用户字段字段
 */
function readDelegateUser(taskId, task_type, processInstanceId) {
	var dialog = this;

	// 清空对话框内容
	$(dialog)
			.html(
					"<form class='dynamic-form' method='post'><table class='dynamic-form-table table table-hover'></table></form>");
	var $form = $('.dynamic-form');

	// 设置表单提交id
	$form.attr('action', ctx + '/form/dynamic/delegate/task/' + taskId + '/'
			+ task_type);

	// 读取启动时的表单
	var trs = "<tr><td width='30%' style='text-align:right;vertical-align:middle'>"
			+ "选择代办人："
			+ "</td><td style='text-align:left;padding-left:15px;vertical-align:middle'><select name='delegateUser'>";

	$.getJSON(
					ctx + '/workflow/userlist',
					function(datas) {

						$.each(datas, function() {
							var id = this.id;
							var name = this.firstName + " " + this.lastName;
							trs += "<option value='" + id + "'>" + name
									+ "</option>";

						});

						trs += "</select></td></tr>";

						$.getJSON(
										ctx + '/form/dynamic/get-form/task/'
												+ taskId
												+ '/processInstanceId/'
												+ processInstanceId,
										function(datas) {

											trs += "<tr><td width='30%' style='text-align:right;vertical-align:middle'>申请人：</td><td style='text-align:left;padding-left:15px;vertical-align:middle'>"
													+ datas.applicant
													+ "</td>+</tr>";

											$.each(datas.taskFormData.formProperties,
															function() {
												if(this.writable == false){
																var className = this.required === true ? "required": "";
																this.value = this.value ? this.value: "";
																trs += "<tr>"+ createFieldHtml(this,datas,className)
																if (this.required === true) {
																	trs += "<span style='color:red'>*</span>";
																}
																trs += "</td></tr>";
												}});

											// 添加table内容
											$('.dynamic-form-table')
													.html(trs)
													.find('tr')
													.hover(
															function() {
																$(this)
																		.addClass(
																				'ui-state-hover');
															},
															function() {
																$(this)
																		.removeClass(
																				'ui-state-hover');
															});

											// 初始化日期组件
											$form.find('.date').datepicker();

											// 表单验证
											$form.validate($.extend({},
													$.common.plugin.validator));
										});

					});

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
			$form.attr('action', ctx + '/form/dynamic/delegate/task/' + taskId + '/allType');

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
