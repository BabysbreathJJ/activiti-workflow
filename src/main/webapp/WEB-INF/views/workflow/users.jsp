<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/common/global.jsp"%>
<title>动态Form流程列表</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-base-styles.jsp"%>
<%@ include file="/common/include-jquery-ui-theme.jsp"%>
<link
	href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css"
	type="text/css" rel="stylesheet" />
<link href="${ctx }/js/common/plugins/qtip/jquery.qtip.min.css"
	type="text/css" rel="stylesheet" />

<%@ include file="/common/include-custom-styles.jsp"%>

<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
<script src="${ctx }/js/common/bootstrap.js" type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/validate/jquery.validate.pack.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/validate/messages_cn.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/common.js" type="text/javascript"></script>
<%-- <script src="${ctx }/js/module/activiti/workflow.js"
	type="text/javascript"></script> --%>

<style>
th,td {
	text-align: center;
}

.table>thead>tr>th,.table>tbody>tr>th,.table>thead>tr>td,.table>tbody>tr>td
	{
	vertical-align: middle;
}

td {
	color: #000000;
	vertical-align: middle;
}

input,select {
	line-height: 1.5em;
	height: 2em;
	boreder-radius: 4px;
}

select {
	width: 180px;
}

.user {
	margin: 0 auto;
	width: 90%;
	background-color: #ffffff;
	min-height: 1000px
}

.left {display； block;
	width: 55%;
	margin-left: 20px;
}

.right {
	display: block;
	width: 35%;
	float: right;
	marigin-right: 20px;
}

legend {
	display: block;
	width: 100%;
	padding: 0;
	margin-bottom: 20px;
	font-size: 21px;
	line-height: 40px;
	color: #333333;
	border: 0;
	border-bottom: 1px solid #e5e5e5;
}

legend+.control-group {
	margin-top: 20px;
	-webkit-margin-top-collapse: separate;
}

legend small {
	font-size: 15px;
	color: #999999;
}

body {
	color: #000000;
}

.control-label {
	float: left;
	text-align: right;
	width: 160px;
}

.controls {
	*display: inline-block;
	*padding-left: 20px;
	margin-left: 180px;
	*margin-left: 0;
}

.controls:first-child {
	*padding-left: 180px;
}

.control-group {
	margin-bottom: 10px;
}

legend+.control-group {
	margin-top: 20px;
	-webkit-margin-top-collapse: separate;
}

.form-horizontal .control-group {
	margin-bottom: 20px;
	*zoom: 1;
}

.form-horizontal .control-group:before,.form-horizontal .control-group:after
	{
	display: table;
	line-height: 0;
	content: "";
}

.form-horizontal .control-group:after {
	clear: both;
}

.form-horizontal .form-actions {
	padding-left: 180px;
}

.form-actions {
	padding: 19px 20px 20px;
	margin-top: 20px;
	margin-bottom: 20px;
	/* background-color: #f5f5f5; */
	border-top: 1px solid #e5e5e5;
	*zoom: 1;
	margin-right: 20px;
}

.form-actions:before,.form-actions:after {
	display: table;
	line-height: 0;
	content: "";
}

.form-actions:after {
	clear: both;
}

.controls input {
	width: 200px;
}
</style>

<script type="text/javascript">
	$(function() {
		$('.edit-user').click(function() {
			var $tr = $('#' + $(this).data('id'));
			$('#userId').val($tr.find('.prop-id').text());
			$('#firstName').val($tr.find('.prop-firstName').text());
			$('#lastName').val($tr.find('.prop-lastName').text());
			$('#email').val($tr.find('.prop-email').text());
		});

		$('.set-group').click(showGroupDialog);
		$("#groupInfo").hide();

		function readGroups(userId) {
		/* 	alert(userId); */

			var userInfo = "<input type='hidden' name='userId' value='"+userId+"'/>";
			/* alert(userInfo); */

			$("#userInfo").html(userInfo);

			/* alert($('#groupInfo').html()); */

			$(".group-dialog").html($('#groupInfo').html());
		}

		function showGroupDialog() {
			var $ele = $(this);
			var userId = $ele.parents('tr').find('.prop-id').text();
			var myGroups = $ele.parents('tr').find('.groups').text();

			$('<div/>', {
				'class' : 'group-dialog',
				title : '编辑所属组',
				html : '<span class="ui-loading">正在读取组信息……</span>'
			}).dialog({
				modal : true,
				width : 400,
				height : $.common.window.getClientHeight() / 2,
				open : function() {
					readGroups.call(this, userId);

				},
				buttons : [ {
					text : '确定',
					click : function() {
						$('.groupUpdate').submit();
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

	});
</script>

</head>
<body style="background-color: #EAEAEA; padding-bottom: 55px">
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
			setTimeout(function() {
				$('#message').hide('slow');
			}, 5000);
		</script>
	</c:if>

	<div class='user'>
		<div class="left">
			<fieldset>
				<legend>
					<small>用户列表</small>
				</legend>

				<table style="margin: 0 auto" class="table table-bordered">
					<thead>
						<tr class="success">
							<th>用户ID</th>
							<th>姓名</th>
							<th>Email</th>
							<th>所属组</th>
							<!-- <th>所属组个数</th> -->
							<th width="130">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${page.result }" var="user">
							<tr id="${user.id}">
								<td class="prop-id">${user.id}</td>
								<td>${user.firstName}${user.lastName}<span
									class="prop-firstName" style="display: none">${user.firstName}</span>
									<span class="prop-lastName" style="display: none">${user.lastName}</span>
								</td>
								<td class="prop-email">${user.email}</td>
								<c:set var="groupNames" value="${''}" />
								<c:set var="groupIds" value="${''}" />
								<c:forEach items="${groupOfUserMap[user.id]}" var="group"
									varStatus="row">
									<c:set var="groupNames" value="${groupNames}${group.name}" />
									<c:set var="groupIds" value="${groupIds}${group.id}" />
									<c:if
										test="${row.index + 1 < fn:length(groupOfUserMap[user.id])}">
										<c:set var="groupNames" value="${groupNames}${','}" />
										<c:set var="groupIds" value="${groupIds}${','}" />
									</c:if>
								</c:forEach>
								<c:if test="${empty groupNames}">
									<c:set var="groupNames" value="${'还未设置所属组，请单击设置'}" />
								</c:if>
								<td title="${groupNames}" class="groups"><a href='#'
									class="set-group" data-groupids="${groupIds}"
									data-userid="${user.id}" data-toggle="modal">${groupNames}</a><br></td>
								<%-- <td title="${groupNames}" class="groups"><a href="#"
									class="set-group" data-groupids="${groupIds}"
									data-userid="${user.id}" data-toggle="modal">共${fn:length(groupOfUserMap[user.id])}个组</a><br>
								</td> --%>
								<td><a class="btn btn-default btn-small"
									href="${ctx}/identity/user/delete/${user.id}"><i></i>删除</a> <a
									class="btn btn-success btn-small edit-user"
									data-id="${user.id}" href="#"><i class="icon-pencil"></i>编辑</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div style="margin: 0 auto; text-align: center;">
					<tags:pagination page="${page}" paginationSize="${page.pageSize}" />
				</div>
			</fieldset>
		</div>


		<!-- 新增、编辑用户的Model -->
		<div class="right">
			<form action="${ctx }/identity/user/save" class="form-horizontal"
				method="post">
				<fieldset>
					<legend>
						<small>新增/编辑用户</small>
					</legend>
					<div id="messageBox" class="alert alert-error input-large controls"
						style="display: none">输入有误，请先更正。</div>
					<div class="control-group">
						<label for="userId" class="control-label">用户ID:</label>
						<div class="controls">
							<input type="text" id="userId" name="userId" class="required" />
						</div>
					</div>
					<div class="control-group">
						<label for="lastName" class="control-label">姓:</label>
						<div class="controls">
							<input type="text" id="lastName" name="lastName" class="required" />
						</div>
					</div>
					<div class="control-group">
						<label for="firstName" class="control-label">名:</label>
						<div class="controls">
							<input type="text" id="firstName" name="firstName"
								class="required" />
						</div>
					</div>
					<div class="control-group">
						<label for="email" class="control-label">Email:</label>
						<div class="controls">
							<input type="text" id="email" name="email" class="required" />
						</div>
					</div>
					<div class="control-group">
						<label for="password" class="control-label">密码:</label>
						<div class="controls">
							<input type="password" id="password" name="password"
								class="required" />
						</div>
					</div>
					<div class="form-actions">
						<button type="reset" class="btn btn-default">
							<i class="icon-remove"></i>重置
						</button>
						<button type="submit" class="btn btn-success">
							<i class="icon-ok-sign"></i>保存
						</button>
					</div>
				</fieldset>
			</form>
		</div>
		<div id="groupInfo">
			<form class='groupUpdate' action="${ctx}/identity/group/set"
				method="POST">

				<div style="margin-left: 20px">
					<table>
						<c:forEach items="${allGroup}" var="group">
							<tr>
								<td width='10%' align="right"><input type="checkbox"
									name="group" value="${group.id}" /></td>
								<td style="text-align: left">${group.name}</td>
							</tr>
						</c:forEach>
					</table>
					<div id="userInfo"></div>
				</div>
			</form>
		</div>




	</div>
</body>
</html>