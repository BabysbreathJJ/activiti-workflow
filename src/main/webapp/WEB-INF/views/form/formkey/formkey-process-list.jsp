<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<title>外置表单流程列表</title>
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
<script
	src="${ctx }/js/module/form/formkey/formkey-form-process-list.js"
	type="text/javascript"></script>
<style type="text/css"]>
th, td {
	text-align: center;
}

td {
	color: #000000;
	vertical-align: middle;
}

input, select {
	line-height: 1.5em;
	height: 2em;
	boreder-radius: 4px;
}

select {
	width: 180px;
}
</style>
</head>

<body style="background-color: #EAEAEA;padding-bottom: 55px">
	<div
		style="background-color: #ffffff; width: 80%; margin: 0 auto; min-height: 1000px">
		<c:if test="${not empty message}">
			<div id="message" class="alert alert-success">${message}</div>
			<!-- 自动隐藏提示信息 -->
			<script type="text/javascript">
				setTimeout(function() {
					$('#message').hide('slow');
				}, 5000);
			</script>
		</c:if>
		<table width="100%" style="margin: 0 auto"
			class="table table-bordered">
			<thead>
				<tr class="success">
					<th>ID</th>
					<th>DID</th>
					<th>名称</th>
					<th>KEY</th>
					<th>版本号</th>
					<th>XML</th>
					<th>图片</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.result }" var="process">
					<tr>
						<td class='process-id'>${process.id }</td>
						<td>${process.deploymentId }</td>
						<td class='process-name'>${process.name }</td>
						<td>${process.key }</td>
						<td>${process.version }</td>
						<td><a target="_blank"
							href='${ctx }/workflow/resource/read?processDefinitionId=${process.id}&resourceType=xml'>${process.resourceName }</a></td>
						<td><a target="_blank"
							href='${ctx }/workflow/resource/read?processDefinitionId=${process.id}&resourceType=image'>${process.diagramResourceName }</a></td>
						<td><a class="startup-process">启动</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div style="margin: 0 auto; text-align: center;">
			<tags:pagination page="${page}" paginationSize="${page.pageSize}" />
		</div>
	</div>
</body>
</html>
