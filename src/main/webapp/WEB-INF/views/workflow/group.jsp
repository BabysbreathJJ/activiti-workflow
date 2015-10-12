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

<script type="text/javascript">
$(function() {
    $('.edit-group').click(function() {
        var $tr = $('#' + $(this).data('id'));
        $('#groupId').val($tr.find('.prop-id').text());
        $('#groupName').val($tr.find('.prop-name').text());
        $('#type').val($tr.find('.prop-type').text());
    });
});
</script>
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
					<small>组列表</small>
				</legend>

				<table style="margin: 0 auto" class="table table-bordered">
					<thead>
					<tr class="success">
                    <th>组ID</th>
                    <th>组名称</th>
                    <th>类型</th>
                    <th width="140">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.result }" var="group">
                    <tr id="${group.id}">
                        <td class="prop-id">${group.id}</td>
                        <td class="prop-name">${group.name}</td>
                        <td class="prop-type">${group.type}</td>
                        <td>
                            <a class="btn btn-default btn-small" href="${ctx}/identity/group/delete/${group.id}"><i class="icon-remove"></i>删除</a>
                            <a class="btn btn-success btn-small edit-group" data-id="${group.id}" href="#"><i class="icon-pencil"></i>编辑</a>
                        </td>
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
			<form action="${ctx }/identity/group/save" class="form-horizontal"
				method="post">
				<fieldset>
					<legend>
						<small>新增/编辑组</small>
					</legend>
					<div id="messageBox" class="alert alert-error input-large controls"
						style="display: none">输入有误，请先更正。</div>
				<div class="control-group">
                    <label for="groupId" class="control-label">组ID:</label>
                    <div class="controls">
                        <input type="text" id="groupId" name="groupId" class="required" />
                    </div>
                </div>
                <div class="control-group">
                    <label for="groupName" class="control-label">组名:</label>
                    <div class="controls">
                        <input type="text" id="groupName" name="groupName" class="required" />
                    </div>
                </div>
                <div class="control-group">
                    <label for="type" class="control-label">组类型:</label>
                    <div class="controls">
                        <select name="type" id="type" class="required">
                            <option value="security-role">安全角色</option>
                            <option value="feature-role">功能角色</option>
                        </select>
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




	</div>
</body>
</html>