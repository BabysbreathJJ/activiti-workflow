/**
 * 动态表单Javascript，负责读取表单元素、启动流程
 */
$(function() {
	$('.startup-process').button({
		icons : {
			primary : 'ui-icon-play'
		}
	}).click(showStartupProcessDialog);
});

/**
 * 打开启动流程
 */

function showStartupProcessDialog() {
	var $ele = $(this);
	var formType = $ele.parents('tr').find('.process-id').text();
	var process_type = $ele.parents('tr').find('.process-type').text();
	var process_key = $ele.parents('tr').find('.process-key').text();

	if (formType.indexOf('formkey') > 0) {

		$(
				'<div/>',
				{
					'class' : 'dynamic-form-dialog',
					title : '启动流程['
							+ $ele.parents('tr').find('.process-name').text()
							+ ']',
					html : '<span class="ui-loading">正在读取表单……</span>'
				}).dialog({
			modal : true,
			width : 600,
			height : $.common.window.getClientHeight() / 2,
			open : function() {
				// 获取json格式的表单数据，就是流程定义中的所有field

				readFormKey.call(this, formType);
			},
			buttons : [ {
				text : '启动流程',
				click : sendStartupRequestFormKey
			} ]
		});
		$('button').attr('class', 'btn btn-success');
	} else if (formType.indexOf("message-start-event") >= 0) {
		$(
				'<div/>',
				{
					'class' : 'dynamic-form-dialog',
					title : '启动流程['
							+ $ele.parents('tr').find('.process-name').text()
							+ ']',
					html : '<span class="ui-loading">正在读取表单……</span>'
				}).dialog({
			modal : true,
			width : 600,
			height : $.common.window.getClientHeight() / 2,
			open : function() {
				readMessage.call(this, formType, process_type);
			},
			buttons : [ {
				text : '启动流程',
				click : sendStartupRequest
			} ]
		});
		$('button').attr('class', 'btn btn-success');
	} else if (formType.indexOf("message-intermediate-event-catch") >= 0) {
		$(
				'<div/>',
				{
					'class' : 'dynamic-form-dialog',
					title : '启动流程['
							+ $ele.parents('tr').find('.process-name').text()
							+ ']',
					html : '<span class="ui-loading">正在读取表单……</span>'
				}).dialog({
			modal : true,
			width : 600,
			height : $.common.window.getClientHeight() / 2,
			open : function() {
				readMessageIntermediate.call(this, process_key, process_type);
			},
			buttons : [ {
				text : '启动流程',
				click : sendStartupRequest
			} ]
		});
		$('button').attr('class', 'btn btn-success');
	} else {

		$(
				'<div/>',
				{
					'class' : 'dynamic-form-dialog',
					title : '启动流程['
							+ $ele.parents('tr').find('.process-name').text()
							+ ']',
					html : '<span class="ui-loading">正在读取表单……</span>'
				}).dialog({
			modal : true,
			width : 600,
			height : $.common.window.getClientHeight() / 2,
			open : function() {
				// 获取json格式的表单数据，就是流程定义中的所有field

				readFormFields.call(this, formType, process_type);
			},
			buttons : [ {
				text : '启动流程',
				click : sendStartupRequest
			} ]
		});

		$('button').attr('class', 'btn btn-success');

	}
	$('button').attr('class', 'btn btn-success');

}

function readMessage(process_key, process_type) {
	var dialog = this;
	$('.dynamic-form-dialog')
			.html(
					"<form class='dynamic-form' method='post'><p>这是一个消息开始事件</p></form>");
	var $form = $('.dynamic-form');
	$form.attr('action', ctx + '/form/dynamic/start-process/message/'
			+ process_key + '/' + process_type);
	$form.validate($.extend({}, $.common.plugin.validator));

}

function readMessageIntermediate(process_key, process_type) {
	var dialog = this;
	$('.dynamic-form-dialog')
			.html(
					"<form class='dynamic-form' method='post'><p>这是一个消息中间件捕获流程,启动流程后，发送消息</p></form>");
	var $form = $('.dynamic-form');
	
	$form.attr('action', ctx + '/form/dynamic/start-process/message/'
			+ process_key + '/' + process_type);
	$form.validate($.extend({}, $.common.plugin.validator));

}

/**
 * 读取表单字段
 */

function readFormFields(processDefinitionId, process_type) {
	var dialog = this;

	// 清空对话框内容
	$('.dynamic-form-dialog')
			.html(
					"<form class='dynamic-form' method='post'><table class='dynamic-form-table table table-hover'></table></form>");
	var $form = $('.dynamic-form');

	// 设置表单提交id
	$form.attr('action', ctx + '/form/dynamic/start-process/'
			+ processDefinitionId + '/' + process_type);

	// 添加隐藏域
	// if ($('#processType').length == 0) {
	// $('<input/>', {
	// 'id' : 'processType',
	// 'name' : 'processType',
	// 'type' : 'hidden'
	// }).val(processType).appendTo($form);
	// }

	// 读取启动时的表单
	$.getJSON(ctx + '/form/dynamic/get-form/start/' + processDefinitionId,
			function(data) {
				var trs = "";
				$.each(data.form.formProperties, function() {
					var className = this.required === true ? "required" : "";
					trs += "<tr>" + createFieldHtml(data, this, className);
					if (this.required === true) {
						trs += "<span style='color:red'>*</span>";
					}
					trs += "</td></tr>";
				});

				// 添加table内容
				$('.dynamic-form-table').html(trs);

				// 初始化日期组件
				$form.find('.dateISO').datepicker();

				// 表单验证
				$form.validate($.extend({}, $.common.plugin.validator));
			});
}

/**
 * form对应的string/date/long/enum/boolean类型表单组件生成器 fp_的意思是form paremeter
 */
var formFieldCreator = {
	string : function(formData, prop, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name
				+ "：</td><td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text'  style='width:80%' id='"
				+ prop.id
				+ "' name='fp_"
				+ prop.id
				+ "' class='"
				+ className
				+ "' />";
		return result;
	},
	date : function(formData, prop, className) {
		var result = "<td style='text-align:right;vertical-align:middle'>"
				+ prop.name
				+ "：</td><td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text' id='"
				+ prop.id + "' name='fp_" + prop.id + "' class='dateISO "
				+ className + "' />";
		return result;
	},
	'enum' : function(formData, prop, className) {
		console.log(prop);
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name + "：</td>";
		if (prop.writable === true) {
			result += "<td style='text-align:left;padding-left:15px;vertical-align:middle'><select id='"
					+ prop.id
					+ "' name='fp_"
					+ prop.id
					+ "' class='"
					+ className + "'>";
			// result += "<option>" + datas + "</option>";

			$.each(formData['enum_' + prop.id], function(k, v) {
				result += "<option value='" + k + "'>" + v + "</option>";
			});

			result += "</select>";
		} else {
			result += "<td>" + prop.value;
		}
		return result;
	},
	'users' : function(formData, prop, className) {
		var result = "<td width='30%' style='text-align:right;vertical-align:middle'>"
				+ prop.name
				+ "：</td><td style='text-align:left;padding-left:15px;vertical-align:middle'><input type='text' id='"
				+ prop.id
				+ "' name='fp_"
				+ prop.id
				+ "' class='"
				+ className
				+ "' />";
		return result;
	}
};

/**
 * 生成一个field的html代码
 */

function createFieldHtml(formData, prop, className) {
	return formFieldCreator[prop.type.name](formData, prop, className);
}

/**
 * 发送启动流程请求
 */

function sendStartupRequest() {
	if ($(".dynamic-form").valid()) {
		$('.dynamic-form').submit();
	}
}

/**
 * 读取流程启动表单
 */
function readFormKey(processDefinitionId) {
	var dialog = this;

	// 读取启动时的表单
	$.get(ctx + '/form/formkey/get-form/start/' + processDefinitionId,
			function(form) {
				// 获取的form是字符行，html格式直接显示在对话框内就可以了，然后用form包裹起来
				$(dialog).html(form).wrap(
						"<form class='formkey-form form-horizontal' method='post' />");

				var $form = $('.formkey-form');

				// 设置表单action
				$form.attr('action', ctx + '/form/formkey/start-process/'
						+ processDefinitionId + '/allType');

				// 初始化日期组件
				if($form.hasClass('.datetime')){
					$form.find('.datetime').datetimepicker({
						stepMinute : 5
					});
				}
				if($form.hasClass('.date')){
					$form.find('.date').datepicker();
				}
				

				// 表单验证
//				$form.validate($.extend({}, $.common.plugin.validator));
			});
}

/**
 * 提交表单
 * 
 * @return {[type]} [description]
 */
function sendStartupRequestFormKey() {
	
		$('.formkey-form').submit();
}
