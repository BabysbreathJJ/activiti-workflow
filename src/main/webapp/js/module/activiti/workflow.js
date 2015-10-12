function graphTrace(options) {

	var _defaults = {
		srcEle : this,
		pid : $(this).attr('pid'),
		pdid : $(this).attr('pdid')
	};
	var opts = $.extend(true, _defaults, options);

	// 处理使用js跟踪当前节点坐标错乱问题
	$('#changeImg')
			.live(
					'click',
					function() {
						$('#workflowTraceDialog').dialog('close');
						if ($('#imgDialog').length > 0) {
							$('#imgDialog').remove();
						}
						$(
								'<div/>',
								{
									'id' : 'imgDialog',
									title : '此对话框显示的图片是由引擎自动生成的，并用红色标记当前的节点<button id="diagram-viewer">Diagram-Viewer</button>',
									html : "<img src='" + ctx
											+ '/workflow/process/trace/auto/'
											+ opts.pid + "' />"
								})
								.appendTo('body')
								.dialog(
										{
											modal : true,
											resizable : false,
											dragable : false,
											width : document.documentElement.clientWidth * 0.95,
											height : document.documentElement.clientHeight * 0.95
										});
					});

	/*
	 * 用官方开发的Diagram-Viewer跟踪
	 */
	$('#diagram-viewer').live(
			'click',
			function() {
				$('#workflowTraceDialog').dialog('close');

				if ($('#imgDialog').length > 0) {
					$('#imgDialog').remove();
				}

				var url = ctx
						+ '/diagram-viewer/index.html?processDefinitionId='
						+ opts.pdid + '&processInstanceId=' + opts.pid;

				$(
						'<div/>',
						{
							'id' : 'imgDialog',
							title : '此对话框显示的图片是由引擎自动生成的，并用红色标记当前的节点',
							html : '<iframe src="' + url
									+ '" width="100%" height="'
									+ document.documentElement.clientHeight
									* 0.90 + '" />'
						}).appendTo('body').dialog({
					modal : true,
					resizable : false,
					dragable : false,
					width : document.documentElement.clientWidth * 0.95,
					height : document.documentElement.clientHeight * 0.95
				});
			});

	// 获取图片资源
	var imageUrl = ctx + "/workflow/resource/process-instance?pid=" + opts.pid
			+ "&type=image";
	$
			.getJSON(
					ctx + '/workflow/process/trace?pid=' + opts.pid,
					function(infos) {

						var positionHtml = "";

						// 生成图片
						var varsArray = new Array();
						$.each(infos, function(i, v) {
							var $positionDiv = $('<div/>', {
								'class' : 'activity-attr'
							}).css({
								position : 'absolute',
								left : (v.x - 1),
								top : (v.y - 1),
								width : (v.width - 2),
								height : (v.height - 2),
								backgroundColor : 'black',
								opacity : 0,
								zIndex : $.fn.qtip.zindex - 1
							});

							// 节点边框
							var $border = $('<div/>', {
								'class' : 'activity-attr-border'
							}).css({
								position : 'absolute',
								left : (v.x - 1),
								top : (v.y - 1),
								width : (v.width - 4),
								height : (v.height - 3),
								zIndex : $.fn.qtip.zindex - 2
							});

							if (v.currentActiviti) {
								$border.addClass('ui-corner-all-12').css({
									border : '3px solid red'
								});
							}
							positionHtml += $positionDiv.outerHTML()
									+ $border.outerHTML();
							varsArray[varsArray.length] = v.vars;
						});

						if ($('#workflowTraceDialog').length == 0) {
							$(
									'<div/>',
									{
										id : 'workflowTraceDialog',
										title : '查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button><button id="diagram-viewer">Diagram-Viewer</button>',
										html : "<div><img src='"
												+ imageUrl
												+ "' style='position:absolute; left:0px; top:0px;' />"
												+ "<div id='processImageBorder'>"
												+ positionHtml + "</div>"
												+ "</div>"
									}).appendTo('body');
						} else {
							$('#workflowTraceDialog img').attr('src', imageUrl);
							$('#workflowTraceDialog #processImageBorder').html(
									positionHtml);
						}

						// 设置每个节点的data
						$('#workflowTraceDialog .activity-attr').each(
								function(i, v) {
									$(this).data('vars', varsArray[i]);
								});

						// 打开对话框
						$('#workflowTraceDialog')
								.dialog(
										{
											modal : true,
											resizable : false,
											dragable : false,
											open : function() {
												$('#workflowTraceDialog')
														.dialog(
																'option',
																'title',
																'查看流程（按ESC键可以关闭）<button id="changeImg">如果坐标错乱请点击这里</button><button id="diagram-viewer">Diagram-Viewer</button>');
												$('#workflowTraceDialog').css(
														'padding', '0.2em');
												$(
														'#workflowTraceDialog .ui-accordion-content')
														.css('padding', '0.2em')
														.height(
																$(
																		'#workflowTraceDialog')
																		.height() - 75);

												// 此处用于显示每个节点的信息，如果不需要可以删除
												$('.activity-attr')
														.qtip(
																{
																	content : function() {
																		var vars = $(
																				this)
																				.data(
																						'vars');
																		var tipContent = "<table class='need-border'>";
																		$
																				.each(
																						vars,
																						function(
																								varKey,
																								varValue) {
																							if (varValue) {
																								tipContent += "<tr><td class='label'>"
																										+ varKey
																										+ "</td><td>"
																										+ varValue
																										+ "<td/></tr>";
																							}
																						});
																		tipContent += "</table>";
																		return tipContent;
																	},
																	position : {
																		at : 'bottom left',
																		adjust : {
																			x : 3
																		}
																	}
																});
												// end qtip
											},
											close : function() {
												$('#workflowTraceDialog')
														.remove();
											},
											width : document.documentElement.clientWidth * 0.95,
											height : document.documentElement.clientHeight * 0.95
										});

					});
}

// 用户添加组
$(function() {
	$('.set-group').click(showGroupDialog);
});
function showGroupDialog() {
	var $ele = $(this);
	var userId = $ele.parents('tr').find('.prop-id').text();
	var myGroups = $ele.parents('tr').find('.groups').text();
	alert(myGroups);

	$('<div/>', {
		'class' : 'group-dialog',
		title : '编辑所属组',
		html : '<span class="ui-loading">正在读取表单……</span>'
	}).dialog({
		modal : true,
		width : 600,
		height : $.common.window.getClientHeight() / 2,
		open : function() {
			// 获取json格式的表单数据，就是流程定义中的所有field

			readGroups.call(this, myGroups, userId);
		}
//		buttons : [ {
//			text : '确定',
//			click : sendStartupRequestFormKey
//		} ]
	});
	$('button').attr('class', 'btn btn-success');

}

function readGroups(myGroups,userId) {
	var dialog = this;

	// 清空对话框内容
	$('.group-dialog')
			.html(
					"<form class='group-form' method='post'><table class='group-table table table-hover'></table></form>");
	var $form = $('.group-form');

	// 设置表单提交id
	$form.attr('action', ctx + '/form/dynamic/start-process/'
			+ processDefinitionId + '/' + process_type);

	// 读取启动时的表单
	$.getJSON(ctx + '/identity/group/list',
			function(data) {
				var trs = "";
				
				$.each(data, function() {
					
					trs += "<tr>" + "<td><input type='checkbox'	name='group' value='"+data.id+" />"+data.name;
					
					trs += "</td></tr>";
				});
				trs+="<input type='hidden' name='userId' value='"+usrId+"/>";
				// 添加table内容
				$('.group-table').html(trs);


				// 表单验证
				$form.validate($.extend({}, $.common.plugin.validator));
			});
}