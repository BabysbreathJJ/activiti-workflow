<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<title>待办任务列表</title>
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
<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/blockui/jquery.blockUI.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/common.js" type="text/javascript"></script>
<script type="text/javascript">
	var processType = '${empty processType ? param.processType : processType}';
</script>
<script src="${ctx }/js/module/activiti/workflow.js"
	type="text/javascript"></script>
<script src="${ctx }/js/module/form/dynamic/dynamic-form-handler.js"
	type="text/javascript"></script>
<style type="text/css">
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

.label {
	width: 30%;
	color: #000000;
	text-align: right;
	vertical-align: middle;
}
</style>
</head>
<%
	response.setHeader("contentType", "text/html;charset=utf-8");
%>
<body style="background-color: #EAEAEA;">
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
					<th>任务ID</th>
					<th>任务Key</th>
					<th>任务名称</th>
					<th>流程定义ID</th>
					<th>流程实例ID</th>
					<th>优先级</th>
					<th>任务创建日期</th>
					<th>任务逾期日</th>
					<th>任务描述</th>
					<th>任务所属人</th>
					<th>操作</th>
				</tr>

				<c:forEach items="${page.result}" var="task">
					<tr>
						<td>${task.id }</td>
						<td>${task.taskDefinitionKey }</td>
						<td>${task.name }</td>
						<td class='process-id'>${task.processDefinitionId }</td>
						<td class='process-instance-id'>${task.processInstanceId }</td>
						<td>${task.priority }</td>
						<td>${task.createTime }</td>
						<td>${task.dueDate }</td>
						<td>${task.description }</td>
						<td>${task.owner }</td>
						<td><c:if test="${empty task.assignee }">

								<a class="claim"
									href="${ctx}/form/dynamic/task/claim/${task.id}/${page.type}">签收</a>

							</c:if> <%-- <c:choose> --%>

								<c:if test="${not empty task.assignee }">
									<c:if test="${not empty task.owner }">
										<a class="handle" tkey='${task.taskDefinitionKey }'
											tname='${task.name }' tid='${task.id }' href="#">办理</a>
									</c:if>
								</c:if>
								<c:if test="${empty task.owner}">
									<c:if test="${ not empty task.assignee}">
										<a class="handle" tkey='${task.taskDefinitionKey }'
											tname='${task.name }' tid='${task.id }' href="#">办理</a>&nbsp;|
										<a class="taskDelegate" tkey='${task.taskDefinitionKey }'
											tname='${task.name }' tid='${task.id }' href="#">请人代办</a>
									</c:if>
								</c:if>
							<%-- </c:choose> --%> <%-- <c:if test="${not empty task.assignee }">
								<a class="handle" tkey='${task.taskDefinitionKey }'
									tname='${task.name }' tid='${task.id }' href="#">办理</a>

							</c:if> --%></td>

						<td style="display: none" class='task-type'>${page.type}</td>
					</tr>
				</c:forEach>
		</table>

		<div style="margin: 0 auto; text-align: center;">
			<tags:pagination page="${page}" paginationSize="${page.pageSize}" />
		</div>

		<!-- 办理任务对话框 -->

		<div id="handleTemplate" class="template"></div>
	</div>
</body>
</html>