/* 动态表单Javascript 
 */
$(function() {
	$('.design-forms').button({
		
	}).click(showFormsDialog);
});

/**
 * 打开启动流程
 */

function showFormsDialog() {
	var $ele = $(this);
	$('<div/>', {
		'class': 'dynamic-form-dialog',
		title: '设计表单',
		html: '<span class="ui-loading">正在读取数据……</span>'
	}).dialog({
		modal: true,
		width: 800,
		height: $.common.window.getClientHeight()/2,
		open: function() {
			// 获取json格式的表单数据，就是流程定义中的所有field
			var processDefinitionId = $ele.parents('tr').find('.process-id').text();
			readForm.call(this, processDefinitionId);
		},
		buttons: [{
			text: '确定',
//			click: 
		}]
	});
	$('button').attr('class','btn btn-success');
}

/**
 * 读取流程启动表单
 */
function readForm(processDefinitionId) {
	var dialog = this;

	// 读取启动时的表单
	$.get(ctx + '/form/formkey/form-design/getFormNames?processDefinitionId=' + processDefinitionId, function(data) {
		console.log(data.length);
		var content = "选择表单：<select>";
		for(var i = 0; i < data.length; i++)
			{
				content+="<option>"+data[i]+"</option>";
			}
		content+="</select>";
		$(dialog).html(content);
	});
}
